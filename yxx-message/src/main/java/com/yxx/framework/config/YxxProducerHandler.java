package com.yxx.framework.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/10/18
 */
@Service
public class YxxProducerHandler {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String queue,String context){
        amqpTemplate.convertAndSend(queue,context);
    }
}
