package com.yxx.framework.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/4
 */
@Configuration
public class MySparkContextConfig {

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf(){
        return new SparkConf().setAppName("spark_test").setMaster("local");
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext() throws Exception {
        return new JavaSparkContext(sparkConf());
    }
}
