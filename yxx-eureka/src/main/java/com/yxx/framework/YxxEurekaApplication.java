package com.yxx.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class YxxEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxxEurekaApplication.class, args);
    }

}
