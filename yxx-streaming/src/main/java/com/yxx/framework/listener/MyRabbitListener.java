package com.yxx.framework.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void execute(String content) {
        System.out.println("get msg: " + content);
    }
}
