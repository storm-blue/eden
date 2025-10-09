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
     * cron表达式: 0 0 0/6 * * ? 表示每天的0点、6点、12点、18点执行
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

            // 随机选择新天气
            String oldWeather = starCity.getWeather();
            String newWeather = WEATHER_TYPES[random.nextInt(WEATHER_TYPES.length)];

            // 确保新天气与旧天气不同
            int attempts = 0;
            while (newWeather.equals(oldWeather) && attempts < 10) {
                newWeather = WEATHER_TYPES[random.nextInt(WEATHER_TYPES.length)];
                attempts++;
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

