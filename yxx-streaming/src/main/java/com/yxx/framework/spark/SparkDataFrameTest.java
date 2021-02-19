package com.yxx.framework.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Properties;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2021/1/14
 */
public class SparkDataFrameTest {

    private static final Logger log = LoggerFactory.getLogger(SparkDataFrameTest.class);

    private SparkSession sparkSession;

    SparkDataFrameTest() {
        this.sparkSession = SparkSession.builder()
                .config(new SparkConf().setMaster("local[2]").setAppName(SparkDataFrameTest.class.getName())).getOrCreate();
    }

    public void sparkJdbcFrame() {
        String url = "jdbc:mysql://192.168.31.106:3306/lansen";
        Properties properties = new Properties();
        properties.put("driver","com.mysql.cj.jdbc.Driver");
        properties.setProperty("username", "sg");
        properties.setProperty("password", "Ssg-DKJ832-QSEF");
        Dataset<Row> dataset = sparkSession.read().jdbc(url, "sys_log", properties);
        dataset.select("*").show(10);
    }

    public void sparkHive() {
        SparkSession sc = SparkSession.builder()
                .config("spark.sql.warehouse.dir", "").enableHiveSupport().getOrCreate();
        sc.sql("create table if not exists t_user(id String, user_name String) using hive");
        sc.sql("load local data inpath '/home/data/user.text' into table t_user");
    }

    public void sparkStreaming() throws InterruptedException {
        JavaStreamingContext jsc = new JavaStreamingContext(
                new StreamingContext(sparkSession.sparkContext(), new Duration(5000)));
        JavaDStream<String> data = jsc.textFileStream("F:/softPackage/spark-2.4.4-bin-hadoop2.6/data/mllib/kmeans_data.txt");
        JavaPairDStream<String, Integer> wordCount = data.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((k1, k2) -> k1 + k2);
        wordCount.print();
        jsc.start();
        jsc.awaitTermination();
    }

    public void sparkNetWorkStreaming(String ip, int port) throws InterruptedException {
        JavaStreamingContext jsc = new JavaStreamingContext(
               new StreamingContext(sparkSession.sparkContext(), new Duration(5000)));
        JavaReceiverInputDStream<String> data = jsc.socketTextStream(ip, port);
        JavaPairDStream<String, Integer> wordCount = data.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((k1, k2) -> k1 + k2);
        wordCount.print();
        jsc.start();
        jsc.awaitTermination();
    }

    public static void main(String[] args) throws InterruptedException {
        new SparkDataFrameTest().sparkNetWorkStreaming("127.0.0.1", 8080);
    }

}
