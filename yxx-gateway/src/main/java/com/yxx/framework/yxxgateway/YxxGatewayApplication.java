package com.yxx.framework.yxxgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YxxGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxxGatewayApplication.class, args);
    }

}
