package com.yxx.freamwork.config;

import com.cxytiandi.elasticjob.parser.JobConfParser;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 任务注册中心
 * </p>
 *
 * @author wangpan
 * @date 2019/10/31
 */
@Configuration
public class JobRegistryCenterConfig {
    @Value("${elastic.job.zk.serverList}")
    private String serverList;
    @Value("${elastic.job.zk.namespace}")
    private String nameSpace;

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(){
        System.out.println(String.format("serverList:%s",serverList));
        System.out.println(String.format("namespace:%s",nameSpace));
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList,nameSpace));
    }

    @Bean
    public JobConfParser jobConfParser() {
        return new JobConfParser();
    }
}
