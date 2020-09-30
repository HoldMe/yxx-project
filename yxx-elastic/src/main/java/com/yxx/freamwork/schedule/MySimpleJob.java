package com.yxx.freamwork.schedule;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <p>
 * desc 定时任务
 * </p>
 *
 * @author wangpan
 * @date 2019/10/28
 */
@ElasticJobConf(name = "MySimpleJob", cron = "0/5 * * * * ?",
        shardingItemParameters = "0=0,1=1", description = "简单任务")
@Service("MySimpleJob")
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("执行任务%s",this.getClass().getName()));
    }
}
