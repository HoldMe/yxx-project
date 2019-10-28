package com.yxx.freamwork;

import com.cxytiandi.elasticjob.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableElasticJob
public class YxxElasticApplication {
    public static void main(String[] args) {
        SpringApplication.run(YxxElasticApplication.class, args);
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
        }
    }

}
