package com.eden.lottery.service;

import com.eden.lottery.entity.Magic;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.mapper.MagicMapper;
import com.eden.lottery.mapper.StarCityMapper;
import com.eden.lottery.mapper.UserMapper;
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
    private UserMapper userMapper;
    
    @Autowired
    private WeatherRefreshTask weatherRefreshTask;
    
    @Autowired
    private GiantAttackService giantAttackService;

    // 魔法代码常量
    public static final String MAGIC_FOOD_RAIN = "FOOD_RAIN";
    public static final String MAGIC_CHANGE_WEATHER = "CHANGE_WEATHER";
    public static final String MAGIC_BANISH_GIANT = "BANISH_GIANT";

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

        // 检查并消耗精力
        checkAndConsumeEnergy(userId, magic);

        logger.info("魔法已施展: {} by {}，消耗精力: {}", code, userId, magic.getEnergyCost());

        // 执行魔法效果
        executeMagicEffect(code);
    }

    /**
     * 检查并消耗精力
     */
    private void checkAndConsumeEnergy(String userId, Magic magic) {
        // 获取魔法的精力消耗
        Integer energyCost = magic.getEnergyCost();
        if (energyCost == null || energyCost == 0) {
            // 该魔法不需要消耗精力
            return;
        }

        // 获取用户当前精力
        Integer currentEnergy = userMapper.getUserEnergy(userId);
        if (currentEnergy == null) {
            throw new IllegalArgumentException("获取用户精力信息失败");
        }

        // 检查精力是否足够
        if (currentEnergy < energyCost) {
            throw new IllegalArgumentException(
                String.format("精力不足！需要 %d 点精力，当前只有 %d 点", energyCost, currentEnergy)
            );
        }

        // 消耗精力
        Integer newEnergy = currentEnergy - energyCost;
        userMapper.updateUserEnergy(userId, newEnergy, LocalDateTime.now());

        logger.info("用户 {} 施展魔法 {}，消耗精力 {}，剩余精力 {}", 
                    userId, magic.getCode(), energyCost, newEnergy);
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
            case MAGIC_BANISH_GIANT:
                executeBanishGiantMagic();
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
     * 执行驱逐巨人魔法效果
     */
    private void executeBanishGiantMagic() {
        // 检查是否有巨人正在进攻
        if (giantAttackService.isGiantAttacking()) {
            // 结束巨人进攻
            giantAttackService.endGiantAttack();
            logger.info("驱逐巨人魔法生效: 巨人进攻已停止，巨人逐渐暗淡消失");
        } else {
            logger.info("驱逐巨人魔法施展: 当前没有巨人进攻，魔法无效");
        }
    }

    /**
     * 更新魔法精力消耗（管理员功能）
     */
    @Transactional
    public void updateMagicEnergyCost(String code, Integer energyCost) {
        Magic magic = magicMapper.selectByCode(code);
        if (magic == null) {
            throw new IllegalArgumentException("魔法不存在: " + code);
        }
        
        magicMapper.updateEnergyCost(code, energyCost);
        logger.info("魔法精力消耗已更新: code={}, energyCost={}", code, energyCost);
    }

}

