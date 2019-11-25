package com.yxx.framework.yxxgateway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/25
 */
@Configuration
public class MyRabbitmqConfig {

    @Bean
    public Queue logQueue(){
        return new Queue("log");
    }
}
