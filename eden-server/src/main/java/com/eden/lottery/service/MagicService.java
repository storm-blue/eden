package com.eden.lottery.service;

import com.eden.lottery.entity.Magic;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.mapper.MagicMapper;
import com.eden.lottery.mapper.StarCityMapper;
import com.eden.lottery.task.WeatherRefreshTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 魔法服务
 */
@Service
public class MagicService {

    private static final Logger logger = LoggerFactory.getLogger(MagicService.class);

    @Autowired
    private MagicMapper magicMapper;

    @Autowired
    private StarCityMapper starCityMapper;
    
    @Autowired
    private WeatherRefreshTask weatherRefreshTask;

    // 魔法代码常量
    public static final String MAGIC_FOOD_RAIN = "FOOD_RAIN";
    public static final String MAGIC_CHANGE_WEATHER = "CHANGE_WEATHER";

    /**
     * 获取所有魔法
     */
    public List<Magic> getAllMagics() {
        return magicMapper.selectAll();
    }

    /**
     * 根据代码获取魔法
     */
    public Magic getMagicByCode(String code) {
        return magicMapper.selectByCode(code);
    }

    /**
     * 施展魔法
     */
    @Transactional
    public void castMagic(String code, String userId) {
        // 验证用户权限（仅秦小淮可施展魔法）
        if (!"秦小淮".equals(userId)) {
            throw new IllegalArgumentException("只有秦小淮可以施展魔法");
        }

        Magic magic = magicMapper.selectByCode(code);
        if (magic == null) {
            throw new IllegalArgumentException("魔法不存在");
        }

        // 检查是否需要刷新次数（跨天了）
        checkAndRefreshDailyUses(magic);

        // 检查剩余次数
        if (magic.getRemainingUses() <= 0) {
            throw new IllegalArgumentException("今日魔法次数已用完，请明日再来");
        }

        // 减少剩余次数
        magicMapper.decreaseRemainingUses(code);

        logger.info("魔法已施展: {} by {}, 剩余次数: {}", code, userId, magic.getRemainingUses() - 1);

        // 执行魔法效果
        executeMagicEffect(code);
    }

    /**
     * 检查并刷新每日次数
     */
    private void checkAndRefreshDailyUses(Magic magic) {
        LocalDateTime lastRefresh = magic.getLastRefreshAt();
        LocalDate today = LocalDate.now();

        if (lastRefresh == null || lastRefresh.toLocalDate().isBefore(today)) {
            // 需要刷新次数
            magicMapper.refreshDailyUses(magic.getCode());
            logger.info("魔法次数已刷新: {}, 刷新为: {}", magic.getCode(), magic.getDailyLimit());

            // 重新查询以更新内存中的对象
            Magic refreshedMagic = magicMapper.selectByCode(magic.getCode());
            magic.setRemainingUses(refreshedMagic.getRemainingUses());
            magic.setLastRefreshAt(refreshedMagic.getLastRefreshAt());
        }
    }

    /**
     * 执行魔法效果
     */
    private void executeMagicEffect(String code) {
        switch (code) {
            case MAGIC_FOOD_RAIN:
                executeFoodRainMagic();
                break;
            case MAGIC_CHANGE_WEATHER:
                executeChangeWeatherMagic();
                break;
            default:
                logger.warn("未知的魔法代码: {}", code);
        }
    }

    /**
     * 执行天降食物魔法效果
     */
    private void executeFoodRainMagic() {
        // 增加10000食物
        StarCity starCity = starCityMapper.getStarCity();
        if (starCity == null) {
            logger.error("未找到秦小淮的星星城数据");
            return;
        }

        Long oldFood = starCity.getFood();
        Long newFood = oldFood + 10000;
        starCity.setFood(newFood);
        starCityMapper.updateStarCity(starCity);

        logger.info("天降食物魔法生效: 食物从 {} 增加到 {}", oldFood, newFood);
    }
    
    /**
     * 执行改变天气魔法效果
     */
    private void executeChangeWeatherMagic() {
        // 调用天气刷新任务立即改变天气
        weatherRefreshTask.refreshWeather();
        logger.info("改变天气魔法生效: 天气已立即刷新");
    }

    /**
     * 刷新所有魔法的每日次数（定时任务调用）
     */
    @Transactional
    public void refreshAllMagicDailyUses() {
        List<Magic> magics = magicMapper.selectAll();
        for (Magic magic : magics) {
            magicMapper.refreshDailyUses(magic.getCode());
            logger.info("定时刷新魔法次数: {}, 刷新为: {}", magic.getCode(), magic.getDailyLimit());
        }
    }
}

