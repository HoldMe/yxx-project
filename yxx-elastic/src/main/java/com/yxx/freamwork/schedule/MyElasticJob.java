package com.yxx.freamwork.schedule;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * desc 定时任务
 * </p>
 *
 * @author wangpan
 * @date 2019/10/28
 */
@ElasticJobConf(name = "MySimpleJob", cron = "0/10 * * * * ?",
        shardingItemParameters = "0=0,1=1", description = "简单任务")
public class MyElasticJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        // 分片数量
        String shardingParameter = shardingContext.getShardingParameter();
        int value = Integer.parseInt(shardingParameter) > 2 ? 4 : 2;
        for(int i=0; i < 10000; i++){
            if(i % value > 0){
                System.out.println(new SimpleDateFormat("YYY-mm-DD HH:mm:ss").format(new Date()) + ": 开始执行任务");
            }
        }
    }
}
