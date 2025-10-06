package com.eden.lottery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 配置静态资源映射
     * 允许访问管理后台页面
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射根路径下的静态文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
                
        // 配置音频文件缓存（1天）
        registry.addResourceHandler("/audio/**")
                .addResourceLocations("classpath:/static/audio/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                    .cachePublic()
                    .mustRevalidate());
    }
}
