package com.yxx.framework.storm.spout;

import com.rabbitmq.client.*;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * desc spout发射器
 * </p>
 *
 * @author wangpan
 * @date 2019/11/8
 */
public class RabbitmqSpout implements IRichSpout {

    private final Logger LOGGER = LoggerFactory.getLogger(RabbitmqSpout.class);

    private Map<String, Object> conf;
    private TopologyContext topologyContext;
    private SpoutOutputCollector spoutOutputCollector;

    private Connection connection;
    private Channel channel;
    private static String QUEUE_NAME = "log";
    private final Map<String, Long> unconfirmedMap = Collections.synchronizedMap(new HashMap<String, Long>());

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.conf = map;
        this.topologyContext = topologyContext;
        this.spoutOutputCollector = spoutOutputCollector;
        // 连接rabbitmap
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        try{
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        }catch (Exception e){
            System.out.println("连接失败");
        }
    }

    @Override
    public void close() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        try {
            GetResponse queue = channel.basicGet(QUEUE_NAME, true);
            if (queue == null){
            }else{
                AMQP.BasicProperties props = queue.getProps();
                String messageId = UUID.randomUUID().toString();
                Long deliveryTag = queue.getEnvelope().getDeliveryTag();
                String body = new String(queue.getBody());

                unconfirmedMap.put(messageId, deliveryTag);
                LOGGER.info("RabbitmqSpout: {}, {}, {}, {}", body, messageId, deliveryTag, props);

                this.spoutOutputCollector.emit(new Values(body), messageId);
            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    /**
     * spout发射的Tuple备blot处理失败
     * @param o
     */
    @Override
    public void ack(Object o) {
        String messageId = o.toString();
        Long deliveryTag = unconfirmedMap.get(messageId);
        LOGGER.info("消息处理成功: {}, {}, {}\n\n", messageId, deliveryTag, unconfirmedMap.size());
        try {
            unconfirmedMap.remove(messageId);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * spout发射的Tuple备blot处理失败
     * @param o
     */
    @Override
    public void fail(Object o) {
        String messageId = o.toString();
        Long deliveryTag = unconfirmedMap.get(messageId);
        LOGGER.info("消息处理失败: {}, {}, {}\n\n", messageId, deliveryTag, unconfirmedMap.size());
        try {
            unconfirmedMap.remove(messageId);
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * spout数据命名
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("log"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
