package com.eden.lottery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * 静态资源配置
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${avatar.upload.path:./uploads/avatars/}")
    private String avatarUploadPath;

    @Value("${memorial.upload.path:./uploads/memorial/}")
    private String memorialUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置头像文件访问路径（7天缓存）
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:" + avatarUploadPath)
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                    .cachePublic()
                    .mustRevalidate());

        // 配置纪念堂文件访问路径（7天缓存）
        registry.addResourceHandler("/uploads/memorial/**")
                .addResourceLocations("file:" + memorialUploadPath)
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                    .cachePublic()
                    .mustRevalidate());
    }
}
