package com.eden.lottery.service;

import com.eden.lottery.entity.GiantAttack;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.mapper.GiantAttackMapper;
import com.eden.lottery.mapper.StarCityMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 巨人进攻服务
 */
@Service
public class GiantAttackService {

    private static final Logger logger = LoggerFactory.getLogger(GiantAttackService.class);

    @Resource
    private GiantAttackMapper giantAttackMapper;

    @Resource
    private StarCityMapper starCityMapper;

    /**
     * 检查是否应该触发巨人进攻
     * 每6小时有1/5的概率触发
     */
    @Transactional
    public void checkGiantAttack() {
        try {
            // 获取当前巨人进攻状态
            GiantAttack currentAttack = giantAttackMapper.getCurrentGiantAttack();

            // 如果已经有活跃的进攻，不触发新的
            if (currentAttack != null && currentAttack.getIsActive()) {
                logger.info("巨人正在进攻中，跳过新的进攻检查");
                return;
            }

            // 1/5的概率触发巨人进攻
            Random random = new Random();
            if (random.nextInt(5) == 0) {
                startGiantAttack();
            } else {
                logger.info("巨人进攻检查：未触发进攻 (1/5概率)");
            }
        } catch (Exception e) {
            logger.error("检查巨人进攻失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 开始巨人进攻
     */
    @Transactional
    public void startGiantAttack() {
        try {
            // 结束所有活跃的进攻
            giantAttackMapper.endAllActiveAttacks(LocalDateTime.now(), LocalDateTime.now());

            // 创建新的巨人进攻
            GiantAttack giantAttack = new GiantAttack(true);
            giantAttack.setStartTime(LocalDateTime.now());
            giantAttack.setLastDamageTime(LocalDateTime.now());

            giantAttackMapper.insertGiantAttack(giantAttack);

            logger.info("巨人进攻开始！进攻时间: {}", giantAttack.getStartTime());
        } catch (Exception e) {
            logger.error("开始巨人进攻失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理巨人进攻伤害
     * 每10分钟造成0.5%的人口损失
     */
    @Transactional
    public void processGiantDamage() {
        try {
            GiantAttack currentAttack = giantAttackMapper.getCurrentGiantAttack();

            if (currentAttack == null || !currentAttack.getIsActive()) {
                return;
            }

            // 检查是否到了造成伤害的时间（每10分钟）
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastDamage = currentAttack.getLastDamageTime();

            if (lastDamage != null && now.isBefore(lastDamage.plusMinutes(10))) {
                return;
            }

            // 获取星星城数据
            StarCity starCity = starCityMapper.getStarCity();
            if (starCity == null) {
                return;
            }

            Long population = starCity.getPopulation();
            Integer happiness = starCity.getHappiness();

            if (population <= 0) {
                return;
            }

            // 计算伤害（0.5%的人口）
            Long damage = Math.round(population * 0.005);
            Long newPopulation = Math.max(0, population - damage);

            // 计算幸福指数下降（每10分钟下降1）
            Integer newHappiness = Math.max(0, happiness - 1);

            // 更新人口和幸福指数
            starCity.setPopulation(newPopulation);
            starCity.setHappiness(newHappiness);
            starCity.setUpdateTime(now);
            starCityMapper.updateStarCity(starCity);

            // 更新巨人进攻记录
            currentAttack.setLastDamageTime(now);
            currentAttack.setUpdateTime(now);
            giantAttackMapper.updateGiantAttack(currentAttack);

            logger.info("巨人造成伤害：人口减少{} (0.5%)，幸福指数下降1，当前人口: {}，当前幸福指数: {}",
                    damage, newPopulation, newHappiness);

            // 如果人口为0，结束巨人进攻
            if (newPopulation == 0) {
                endGiantAttack();
            }
        } catch (Exception e) {
            logger.error("处理巨人伤害失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 结束巨人进攻
     */
    @Transactional
    public void endGiantAttack() {
        try {
            GiantAttack currentAttack = giantAttackMapper.getCurrentGiantAttack();

            if (currentAttack != null && currentAttack.getIsActive()) {
                currentAttack.setIsActive(false);
                currentAttack.setEndTime(LocalDateTime.now());
                currentAttack.setUpdateTime(LocalDateTime.now());

                giantAttackMapper.updateGiantAttack(currentAttack);

                logger.info("巨人进攻结束！结束时间: {}", currentAttack.getEndTime());
            }
        } catch (Exception e) {
            logger.error("结束巨人进攻失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取当前巨人进攻状态
     */
    public GiantAttack getCurrentGiantAttack() {
        return giantAttackMapper.getCurrentGiantAttack();
    }

    /**
     * 检查巨人是否正在进攻
     */
    public boolean isGiantAttacking() {
        GiantAttack currentAttack = getCurrentGiantAttack();
        return currentAttack != null && currentAttack.getIsActive();
    }

    /**
     * 获取所有巨人进攻历史记录
     */
    public List<GiantAttack> getAllGiantAttackHistory() {
        return giantAttackMapper.getAllGiantAttackHistory();
    }
}
