package com.yxx.framework.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yxx.framework.dto.Movies;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * desc 监听服务
 * </p>
 *
 * @author wangpan
 * @date 2019/10/28
 */
@Configuration
public class MyRabbitListener implements Serializable {

    private static final long serialVersionUID = -1275437007882875556L;

    @Autowired
    private JavaSparkContext sc;

    private SparkSession sparkSession;

    private JavaStreamingContext jsc;

    @RabbitListener(queues = "log")
    @RabbitHandler
    public void execute(String  content) {
        System.out.println("get msg: " + content);
//        streamRabbitMsg();
    }

    /**
     * 初始化sparkContext
     */
    private void init(){
        if (this.sparkSession == null){
            SparkConf conf = new SparkConf().setAppName("spark_test").setMaster("local");
            this.sparkSession = SparkSession.builder().config(conf).getOrCreate();
        }
    }

    /**
     * 初始化sparkContext
     */
    private void initJsc(){
        if (this.jsc == null){
            SparkConf conf = new SparkConf().setAppName("spark_test").setMaster("local");
            this.jsc = new JavaStreamingContext(conf,new Duration(1000));
        }
    }

    /**
     * spark接收消息
     */
//    private void streamRabbitMsg(){
////        initJsc();
////        Map<String, String> params = new HashMap<>();
////        params.put("hosts", "localhost");
////        params.put("port", "5672");
////        params.put("userName", "guest");
////        params.put("password", "guest");
////        params.put("queueName", "log");
////        params.put("durable", "false");
////        Function<QueueingConsumer.Delivery, String> handler = message -> new String(message.getBody());
////        JavaReceiverInputDStream<String>sparkStreaming = RabbitMQUtils.createJavaStream(jsc,params);
////        sparkStreaming.print();
////        jsc.start();
////        jsc.awaitTermination();
////    }

    /**
     * 读取文本作为spark SQL数据源
     */
    private void readTxt(){
        JavaRDD<String> lines = sc.textFile("F:/pythonWorkspace/scrapy_project/douban/douban.json");
        JavaRDD map = lines.map(new Function<String, Movies>() {
            @Override
            public Movies call(String s) throws Exception {
                JsonObject asJsonObject = new JsonParser().parse(s).getAsJsonObject();
                Movies m = new Gson().fromJson(s, Movies.class);
                return m;
            }
        });
        List<StructField> fileds = new ArrayList<>();
        fileds.add(DataTypes.createStructField("title", DataTypes.StringType,true));
        fileds.add(DataTypes.createStructField("desc", DataTypes.StringType,true));
        fileds.add(DataTypes.createStructField("stars", DataTypes.FloatType,true));
        fileds.add(DataTypes.createStructField("quote", DataTypes.StringType,true));
        StructType structType = DataTypes.createStructType(fileds);

        Dataset<Row> dataFrame = sparkSession.createDataFrame(lines, Movies.class);
        dataFrame.createOrReplaceTempView("movies");
        Dataset<Row> sql = sparkSession.sql("select * from movies limit 5");
        sql.show();
    }

    /**
     * 读取json作为spark SQL数据源
     */
    private void readJson(){
        Dataset<Row> json = sparkSession.read().format("json").load("F:/pythonWorkspace/scrapy_project/douban/douban.json");
        json.select("*").filter("title like '%天空%'").show(10);
    }
}
