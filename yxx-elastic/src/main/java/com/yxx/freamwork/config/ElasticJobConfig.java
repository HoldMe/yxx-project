package com.yxx.freamwork.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.yxx.freamwork.schedule.MyElasticJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/10/31
 */
@Configuration
public class ElasticJobConfig {

    @Value("${elastic.job.cron}")
    private String cron;
    @Value("${elastic.job.shardingTotalCount}")
    private String shardingTotalCount;
    @Value("${elastic.job.shardingItemParameters}")
    private String shardingItemParameters;

    @Autowired
    private JobRegistryCenterConfig registryCenterConfig;
    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ElasticJobConfig(){}

    @Bean
    public SimpleJob elasticJob(){
        return new MyElasticJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler simpleJobSchedule(final SimpleJob simpleJob){
        return new SpringJobScheduler(elasticJob(),zookeeperRegistryCenter,
                getLiteJobConfiguration(simpleJob.getClass(),cron,Integer.valueOf(shardingTotalCount),shardingItemParameters));
    }

    /**
     * 任务配置
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters){
        return LiteJobConfiguration
                .newBuilder(
                        new SimpleJobConfiguration(
                                JobCoreConfiguration.newBuilder(
                                        jobClass.getName(),cron,shardingTotalCount)
                                        .shardingItemParameters(shardingItemParameters)
                                        .build()
                                ,jobClass.getCanonicalName()
                        )
                )
                .overwrite(true)
                .build();
    }
}
