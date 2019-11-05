package com.yxx.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YxxStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxxStreamingApplication.class, args);
    }

}
