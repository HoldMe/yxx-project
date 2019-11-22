package com.yxx.framework.yxxgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableDiscoveryClient
@EnableHystrix
@SpringBootApplication
public class YxxGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxxGatewayApplication.class, args);
    }

}
