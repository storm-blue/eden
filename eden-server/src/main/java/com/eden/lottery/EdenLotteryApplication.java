package com.eden.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🎪 Eden抽奖系统启动类
 *
 * @author Eden Team
 */
@SpringBootApplication
@EnableScheduling
public class EdenLotteryApplication {

    public static void main(String[] args) {
        System.out.println("🎪 启动Eden抽奖系统...");
        SpringApplication.run(EdenLotteryApplication.class, args);

        System.out.println("🎯 Eden抽奖系统启动成功! 访问地址: http://localhost:5000");
    }
}
