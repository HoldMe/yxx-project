package com.yxx.framework.yxxgateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author anders
 * @date 2018-12-05 17:57
 * 跨域配置CORS
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }

}
