package com.yxx.framework.schedule;

import com.yxx.framework.config.YxxProducerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * desc 定时器服务
 * </p>
 *
 * @author wangpan
 * @date 2019/10/18
 */
@Service
public class MsgScheduleService{

    @Autowired
    private YxxProducerHandler producerHandler;

    @Value("aaa")
    private String aaa;

    @Value("bbb")
    private String bbb;

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendTask(){
        System.out.println("====================" + aaa);
        producerHandler.send("hello",new Date().toString()+"==============================="+aaa);
    }
}
