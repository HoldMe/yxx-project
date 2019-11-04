package com.yxx.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
public class YxxStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxxStreamingApplication.class, args);
    }

}
