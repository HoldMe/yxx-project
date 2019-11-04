package com.yxx.framework.listener;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * desc 监听服务
 * </p>
 *
 * @author wangpan
 * @date 2019/10/28
 */
@Configuration
public class MyRabbitListener {

    @Autowired
    private JavaSparkContext sc;

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void execute(String content) {
        System.out.println("get msg: " + content);
        sparkCalculateTest();
    }

    public void sparkCalculateTest(){
        JavaRDD<String> lines = sc.textFile("F:/pythonWorkspace/scrapy_project/douban/douban.json");
        lines.foreach(str -> System.out.println(str));
    }

    public void statisticTop10(JavaRDD<Object> source){
        source.foreach(str -> System.out.println(str));
    }
}
