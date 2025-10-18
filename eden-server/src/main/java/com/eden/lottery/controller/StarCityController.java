package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.service.StarCityService;
import com.eden.lottery.service.SpecialResidenceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 星星城控制器
 */
@RestController
@RequestMapping("/api/star-city")
public class StarCityController {

    @Resource
    private StarCityService starCityService;

    @Resource
    private SpecialResidenceService specialResidenceService;

    /**
     * 获取星星城数据
     */
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getStarCityInfo() {
        try {
            StarCity starCity = starCityService.getStarCity();
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", starCity.getId());
            result.put("population", starCity.getPopulation());
            result.put("food", starCity.getFood());
            result.put("happiness", starCity.getHappiness());
            result.put("level", starCity.getLevel());
            result.put("weather", starCity.getWeather());
            result.put("lastUpdateTime", starCity.getLastUpdateTime());
            
            // 格式化显示
            result.put("populationFormatted", starCityService.formatNumber(starCity.getPopulation()));
            result.put("foodFormatted", starCityService.formatNumber(starCity.getFood()));
            result.put("levelInfo", starCityService.getLevelInfo(starCity.getLevel()));
            result.put("canUpgrade", starCityService.canUpgrade(starCity));
            
            // 下一等级所需条件
            if (starCity.getLevel() < 10) {
                Map<String, Object> nextLevel = getNextLevelRequirements(starCity.getLevel() + 1);
                result.put("nextLevelRequirements", nextLevel);
            }
            
            return ApiResponse.success("获取星星城信息成功", result);
        } catch (Exception e) {
            return ApiResponse.error("获取星星城信息失败: " + e.getMessage());
        }
    }

    /**
     * 手动触发每日更新（测试用）
     */
    @PostMapping("/daily-update")
    public ApiResponse<String> triggerDailyUpdate() {
        try {
            starCityService.dailyUpdate();
            return ApiResponse.success("每日更新完成", "星星城数据已更新");
        } catch (Exception e) {
            return ApiResponse.error("每日更新失败: " + e.getMessage());
        }
    }

    /**
     * 用户捐献奖品给星星城
     */
    @PostMapping("/donate")
    public ApiResponse<Map<String, Object>> donate(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String prizeType = request.get("prizeType");
            
            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.error("用户ID不能为空");
            }
            
            if (prizeType == null || prizeType.trim().isEmpty()) {
                return ApiResponse.error("奖品类型不能为空");
            }
            
            // 检查奖品类型是否有效
            if (!prizeType.equals("🍰 吃的～") && !prizeType.equals("🥤 喝的～") && !prizeType.equals("🎁 随机礼物")) {
                return ApiResponse.error("无效的奖品类型");
            }
            
            boolean success = starCityService.processDonation(userId, prizeType);
            
            if (success) {
                // 返回更新后的星星城数据
                StarCity starCity = starCityService.getStarCity();
                Map<String, Object> result = new HashMap<>();
                result.put("message", "捐献成功！感谢您对星星城的贡献！");
                
                // 直接返回完整的星星城数据格式，与 /info 接口保持一致
                result.put("id", starCity.getId());
                result.put("population", starCity.getPopulation());
                result.put("food", starCity.getFood());
                result.put("happiness", starCity.getHappiness());
                result.put("level", starCity.getLevel());
                result.put("lastUpdateTime", starCity.getLastUpdateTime());
                
                // 格式化显示
                result.put("populationFormatted", starCityService.formatNumber(starCity.getPopulation()));
                result.put("foodFormatted", starCityService.formatNumber(starCity.getFood()));
                result.put("levelInfo", starCityService.getLevelInfo(starCity.getLevel()));
                result.put("canUpgrade", starCityService.canUpgrade(starCity));
                
                // 下一等级所需条件
                if (starCity.getLevel() < 10) {
                    Map<String, Object> nextLevel = getNextLevelRequirements(starCity.getLevel() + 1);
                    result.put("nextLevelRequirements", nextLevel);
                }
                
                return ApiResponse.success("捐献成功", result);
            } else {
                return ApiResponse.error("捐献失败，您可能没有该奖品或奖品已用完");
            }
        } catch (Exception e) {
            return ApiResponse.error("捐献失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户可捐献的奖品列表
     */
    @GetMapping("/donation-prizes/{userId}")
    public ApiResponse<List<Map<String, Object>>> getDonationPrizes(@PathVariable String userId) {
        try {
            List<Map<String, Object>> prizes = starCityService.getUserDonationPrizes(userId);
            return ApiResponse.success("获取可捐献奖品成功", prizes);
        } catch (Exception e) {
            return ApiResponse.error("获取可捐献奖品失败: " + e.getMessage());
        }
    }

    /**
     * 测试API：直接获取用户的所有抽奖记录
     */
    @GetMapping("/test-records/{userId}")
    public ApiResponse<Map<String, Object>> getTestRecords(@PathVariable String userId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("message", "测试API正常工作");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("backendVersion", "v2.0-fixed-donation-format");
            
            return ApiResponse.success("测试成功", result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", e.getMessage());
            errorResult.put("userId", userId);
            return ApiResponse.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试捐献API的数据格式
     */
    @GetMapping("/test-donation-format")
    public ApiResponse<Map<String, Object>> testDonationFormat() {
        try {
            // 模拟捐献成功后的返回格式
            StarCity starCity = starCityService.getStarCity();
            Map<String, Object> result = new HashMap<>();
            result.put("message", "这是测试捐献格式的API");
            
            // 应该返回的格式（与 /info 接口一致）
            result.put("id", starCity.getId());
            result.put("population", starCity.getPopulation());
            result.put("food", starCity.getFood());
            result.put("happiness", starCity.getHappiness());
            result.put("level", starCity.getLevel());
            result.put("weather", starCity.getWeather());
            result.put("lastUpdateTime", starCity.getLastUpdateTime());
            
            // 格式化显示
            result.put("populationFormatted", starCityService.formatNumber(starCity.getPopulation()));
            result.put("foodFormatted", starCityService.formatNumber(starCity.getFood()));
            result.put("levelInfo", starCityService.getLevelInfo(starCity.getLevel()));
            result.put("canUpgrade", starCityService.canUpgrade(starCity));
            
            // 下一等级所需条件
            if (starCity.getLevel() < 10) {
                Map<String, Object> nextLevel = getNextLevelRequirements(starCity.getLevel() + 1);
                result.put("nextLevelRequirements", nextLevel);
            }
            
            return ApiResponse.success("测试捐献格式成功", result);
        } catch (Exception e) {
            return ApiResponse.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 获取下一等级要求
     */
    private Map<String, Object> getNextLevelRequirements(int level) {
        Map<String, Object> requirements = new HashMap<>();
        switch (level) {
            case 2:
                requirements.put("population", 200000L);
                requirements.put("food", 200000L);
                requirements.put("happiness", 30);
                break;
            case 3:
                requirements.put("population", 400000L);
                requirements.put("food", 400000L);
                requirements.put("happiness", 50);
                break;
            case 4:
                requirements.put("population", 700000L);
                requirements.put("food", 700000L);
                requirements.put("happiness", 80);
                break;
            case 5:
                requirements.put("population", 1000000L);
                requirements.put("food", 1000000L);
                requirements.put("happiness", 100);
                break;
            case 6:
                requirements.put("population", 1500000L);
                requirements.put("food", 1500000L);
                requirements.put("happiness", 150);
                break;
            case 7:
                requirements.put("population", 3000000L);
                requirements.put("food", 3000000L);
                requirements.put("happiness", 300);
                break;
            case 8:
                requirements.put("population", 5000000L);
                requirements.put("food", 5000000L);
                requirements.put("happiness", 500);
                break;
            case 9:
                requirements.put("population", 10000000L);
                requirements.put("food", 10000000L);
                requirements.put("happiness", 1000);
                break;
            case 10:
                requirements.put("population", 20000000L);
                requirements.put("food", 20000000L);
                requirements.put("happiness", 2000);
                break;
            default:
                return null;
        }
        
        requirements.put("populationFormatted", starCityService.formatNumber((Long) requirements.get("population")));
        requirements.put("foodFormatted", starCityService.formatNumber((Long) requirements.get("food")));
        
        return requirements;
    }
    
    /**
     * 获取特殊居住组合状态
     */
    @GetMapping("/special-combos")
    public ApiResponse<Map<String, Object>> getSpecialCombos() {
        try {
            List<Map<String, Object>> activeCombos = specialResidenceService.getActiveSpecialCombos();
            int hourlyBonus = specialResidenceService.calculateHourlyPopulationBonus();
            
            Map<String, Object> result = new HashMap<>();
            result.put("activeCombos", activeCombos);
            result.put("totalHourlyBonus", hourlyBonus);
            result.put("hasSpecialCombos", !activeCombos.isEmpty());
            
            return ApiResponse.success("获取特殊居住组合状态成功", result);
        } catch (Exception e) {
            return ApiResponse.error("获取特殊居住组合状态失败: " + e.getMessage());
        }
    }
}
