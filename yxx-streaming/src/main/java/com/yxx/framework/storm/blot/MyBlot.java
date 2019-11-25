package com.yxx.framework.storm.blot;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/6
 */
public class MyBlot extends BaseRichBolt {

    private Map conf;
    private TopologyContext context;
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.conf = map;
        this.context = topologyContext;
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String log = tuple.getStringByField("log");
        System.out.println("log========" + log);
        // 不需要跳转下个blot时，declareOutputFields不需要实现
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
