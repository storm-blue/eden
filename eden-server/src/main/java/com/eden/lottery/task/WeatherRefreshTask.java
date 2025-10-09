package com.eden.lottery.task;

import com.eden.lottery.entity.StarCity;
import com.eden.lottery.mapper.StarCityMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 天气刷新定时任务
 * 每6小时随机刷新星星城天气
 */
@Service
public class WeatherRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(WeatherRefreshTask.class);

    @Resource
    private StarCityMapper starCityMapper;

    private final Random random = new Random();

    // 天气类型数组
    private static final String[] WEATHER_TYPES = {
            "sunny",   // 晴天
            "rainy",   // 雨天
            "snowy",   // 雪天
            "cloudy",  // 多云
            "night"    // 夜晚
    };

    /**
     * 每6小时执行一次天气刷新
     * 晚上7点到凌晨5点之间强制为夜晚天气
     */
    //@Scheduled(cron = "0 0 0/6 * * ?")
    @Scheduled(fixedDelay = 360, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void refreshWeather() {
        try {
            logger.info("开始刷新星星城天气...");

            // 获取当前星星城数据
            StarCity starCity = starCityMapper.getStarCity();

            if (starCity == null) {
                logger.warn("星星城数据不存在，跳过天气刷新");
                return;
            }

            String oldWeather = starCity.getWeather();
            String newWeather;
            
            // 获取当前小时
            LocalDateTime now = LocalDateTime.now();
            int currentHour = now.getHour();
            
            // 判断是否在夜晚时段（19:00-05:00）
            boolean isNightTime = currentHour >= 19 || currentHour < 5;
            
            if (isNightTime) {
                // 晚上7点到凌晨5点之间，强制设置为夜晚
                newWeather = "night";
                logger.info("当前时段为夜晚（{}点），设置天气为night", currentHour);
            } else {
                // 白天时段（05:00-19:00），从其他天气中随机选择
                String[] dayWeatherTypes = {"sunny", "rainy", "snowy", "cloudy"};
                newWeather = dayWeatherTypes[random.nextInt(dayWeatherTypes.length)];
                
                logger.info("当前时段为白天（{}点），随机选择天气", currentHour);
            }

            // 更新天气
            starCity.setWeather(newWeather);
            starCity.setUpdateTime(LocalDateTime.now());
            starCityMapper.updateStarCity(starCity);

            logger.info("星星城天气已刷新: {} -> {}", oldWeather, newWeather);
        } catch (Exception e) {
            logger.error("天气刷新失败", e);
        }
    }
}

