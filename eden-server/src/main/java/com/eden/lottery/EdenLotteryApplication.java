package com.eden.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🎪 Eden抽奖系统启动类
 * 
 * @author Eden Team
 */
@SpringBootApplication
public class EdenLotteryApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        System.out.println("🎪 启动Eden抽奖系统...");
        SpringApplication.run(EdenLotteryApplication.class, args);
        System.out.println("🎯 Eden抽奖系统启动成功! 访问地址: http://localhost:5000");
    }

    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
