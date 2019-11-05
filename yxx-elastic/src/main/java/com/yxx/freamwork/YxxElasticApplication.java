package com.yxx.freamwork;

//import com.cxytiandi.elasticjob.annotation.EnableElasticJob;
import com.dangdang.ddframe.job.lite.console.filter.GlobalConfigurationFilter;
import com.dangdang.ddframe.job.lite.console.restful.JobOperationRestfulApi;
import com.dangdang.ddframe.job.restful.RestfulServer;
import com.dangdang.ddframe.job.security.WwwAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.google.common.base.Optional;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableElasticJob
public class YxxElasticApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(YxxElasticApplication.class, args);
        startConsoleServer();
//        try {
//            new CountDownLatch(1).await();
//        } catch (InterruptedException e) {
//        }
    }

    /**
     * 启动elastic控制台
     * @throws Exception
     */
    private static void startConsoleServer() throws Exception{
        int port = 8899;
        RestfulServer restfulServer = new RestfulServer(port);
        restfulServer.addFilter(GlobalConfigurationFilter.class, "*.html")
                .addFilter(WwwAuthFilter.class, "/")
                .addFilter(WwwAuthFilter.class, "*.html")
                .start(JobOperationRestfulApi.class.getPackage().getName(), Optional.of("console"));
    }

}
