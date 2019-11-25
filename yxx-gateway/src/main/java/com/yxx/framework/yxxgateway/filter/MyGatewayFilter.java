package com.yxx.framework.yxxgateway.filter;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/22
 */
@Component
public class MyGatewayFilter implements GlobalFilter {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();
        String body = uri.getRawQuery();
        String msg = host+" "+port+" "+path;
        amqpTemplate.convertAndSend("log",msg);
        System.out.println("queue msg:  " + msg);
        return chain.filter(exchange);
    }
}
