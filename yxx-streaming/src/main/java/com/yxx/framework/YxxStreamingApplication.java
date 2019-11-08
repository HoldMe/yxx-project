package com.yxx.framework;

import com.yxx.framework.storm.blot.MyBlot;
import com.yxx.framework.storm.spout.RabbitmqSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class YxxStreamingApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(YxxStreamingApplication.class, args);

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("spout1",new RabbitmqSpout());
        topologyBuilder.setBolt("blot1",new MyBlot()).shuffleGrouping("spout1");

        // 本地集群
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test",new Config(),topologyBuilder.createTopology());
    }

}
