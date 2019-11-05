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
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void execute(String  content) {
        System.out.println("get msg: " + content);
        sparkSqlTest();
    }

    public void sparkSqlTest(){
        JavaRDD<String> lines = sc.textFile("F:/pythonWorkspace/scrapy_project/douban/douban.json");
        SparkConf conf = new SparkConf().setAppName("spark_test").setMaster("local");
        SparkSession session = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> json = session.read().format("json").load("F:/pythonWorkspace/scrapy_project/douban/douban.json");
        json.select("*").filter("title like '%天空%'").show(10);
    }

    public void statisticTop10(JavaRDD<Object> source){
        source.foreach(str -> System.out.println(str));
    }

    private void readTxt(SparkSession session, JavaRDD lines){
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

        Dataset<Row> dataFrame = session.createDataFrame(lines, Movies.class);
        dataFrame.createOrReplaceTempView("movies");
        Dataset<Row> sql = session.sql("select * from movies limit 5");
        sql.show();
    }
}
