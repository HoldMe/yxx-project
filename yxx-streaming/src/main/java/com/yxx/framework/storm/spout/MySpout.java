package com.yxx.framework.storm.spout;

import com.github.cgdon.fqueue.FQueue;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/6
 */
public class MySpout extends BaseRichSpout {

    private Map<String, Object> conf;
    private TopologyContext topologyContext;
    private SpoutOutputCollector spoutOutputCollector;
    private FQueue queue;

    /**
     * 初始化
     * @param map
     * @param topologyContext topology上下文
     * @param spoutOutputCollector spout发射器
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.conf = map;
        this.topologyContext = topologyContext;
        this.spoutOutputCollector = spoutOutputCollector;
        try{
            this.queue = new FQueue("abc",1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 数据发射给blot
     */
    @Override
    public void nextTuple() {
        try{
            byte[] poll = this.queue.poll();
            if(poll!=null && poll.length >0){
                this.spoutOutputCollector.emit(new Values(new String(poll)));
            } else {
                this.createData();
            }
        }catch (Exception e) {
            System.out.println("读取文件失败");
        }
    }

    /**
     * 声明输出字段
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("log"));
    }

    private void createData(){
        for (int i=0;i<1000;i++){
            this.queue.offer(UUID.randomUUID().toString().getBytes());
        }
    }
}
