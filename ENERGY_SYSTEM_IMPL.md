# 精力系统实现方案

## ✅ 已完成的工作

### 1. 数据库层
- ✅ User 表添加字段：
  - `energy` INTEGER NOT NULL DEFAULT 15 - 当前精力值
  - `max_energy` INTEGER NOT NULL DEFAULT 15 - 最大精力值
  - `energy_refresh_time` DATETIME - 精力刷新时间

- ✅ Magic 表添加字段：
  - `energy_cost` INTEGER NOT NULL DEFAULT 5 - 精力消耗

### 2. 实体类
- ✅ `User.java` 添加精力相关字段和 getter/setter
- ✅ `Magic.java` 添加精力消耗字段和 getter/setter

### 3. 数据库初始化
- ✅ `PrizeInitService.java` 添加表结构迁移逻辑
- ✅ 为三种魔法设置精力消耗：
  - 天降食物：5点精力
  - 改变天气：3点精力
  - 驱逐巨人：8点精力

## 📋 剩余工作

### 4. Mapper 层（需要实现）
需要在 `UserMapper.java` 中添加方法：

```java
/**
 * 获取用户精力信息
 */
@Select("SELECT energy, max_energy, energy_refresh_time FROM users WHERE user_id = #{userId}")
Map<String, Object> getUserEnergy(String userId);

/**
 * 更新用户精力
 */
@Update("UPDATE users SET energy = #{energy}, energy_refresh_time = #{energyRefreshTime}, update_time = datetime('now', 'localtime') WHERE user_id = #{userId}")
void updateUserEnergy(@Param("userId") String userId, @Param("energy") Integer energy, @Param("energyRefreshTime") LocalDateTime energyRefreshTime);

/**
 * 刷新所有用户的精力
 */
@Update("UPDATE users SET energy = max_energy, energy_refresh_time = datetime('now', 'localtime'), update_time = datetime('now', 'localtime')")
void refreshAllUsersEnergy();
```

需要在 `MagicMapper.java` 中添加 energy_cost 到查询结果：

```java
@Results({
    @Result(property = "id", column = "id"),
    @Result(property = "code", column = "code"),
    @Result(property = "name", column = "name"),
    @Result(property = "description", column = "description"),
    @Result(property = "dailyLimit", column = "daily_limit"),
    @Result(property = "remainingUses", column = "remaining_uses"),
    @Result(property = "energyCost", column = "energy_cost"),  // 新增
    @Result(property = "lastRefreshAt", column = "last_refresh_at"),
    @Result(property = "createdAt", column = "created_at")
})
```

### 5. Service 层（需要实现）
修改 `MagicService.java`：

```java
/**
 * 检查并消耗精力
 */
private void checkAndConsumeEnergy(String userId, String magicCode) {
    // 1. 获取魔法的精力消耗
    Magic magic = magicMapper.selectByCode(magicCode);
    if (magic == null) {
        throw new RuntimeException("魔法不存在");
    }
    
    Integer energyCost = magic.getEnergyCost();
    
    // 2. 获取用户当前精力
    Map<String, Object> energyInfo = userMapper.getUserEnergy(userId);
    Integer currentEnergy = (Integer) energyInfo.get("energy");
    
    // 3. 检查精力是否足够
    if (currentEnergy < energyCost) {
        throw new RuntimeException("精力不足！当前精力: " + currentEnergy + "，需要: " + energyCost);
    }
    
    // 4. 消耗精力
    Integer newEnergy = currentEnergy - energyCost;
    userMapper.updateUserEnergy(userId, newEnergy, LocalDateTime.now());
    
    logger.info("用户 {} 施展魔法 {}，消耗精力 {}，剩余精力 {}", 
                userId, magicCode, energyCost, newEnergy);
}

/**
 * 修改 castMagic 方法，添加精力检查
 */
public Map<String, Object> castMagic(String userId, String magicCode) {
    // 1. 检查用户权限
    if (!"秦小淮".equals(userId)) {
        throw new RuntimeException("只有秦小淮可以施展魔法");
    }
    
    // 2. 检查并消耗精力（新增）
    checkAndConsumeEnergy(userId, magicCode);
    
    // 3. 检查剩余次数
    Magic magic = magicMapper.selectByCode(magicCode);
    if (magic.getRemainingUses() <= 0) {
        throw new RuntimeException("今日施展次数已用完");
    }
    
    // 4. 减少剩余次数
    magicMapper.decreaseRemainingUses(magicCode);
    
    // 5. 执行魔法效果
    executeMagicEffect(magicCode);
    
    // 6. 返回结果
    return Map.of(
        "success", true,
        "message", "魔法施展成功"
    );
}
```

修改 `DailyRefreshTask.java`：

```java
/**
 * 每日刷新任务（凌晨12点执行）
 */
@Scheduled(cron = "0 0 0 * * ?")
public void dailyRefresh() {
    logger.info("开始执行每日刷新任务...");
    
    try {
        // 刷新抽奖次数
        userMapper.refreshDailyDraws();
        logger.info("抽奖次数刷新完成");
        
        // 刷新魔法次数
        magicMapper.refreshDailyUses();
        logger.info("魔法次数刷新完成");
        
        // 刷新精力（新增）
        userMapper.refreshAllUsersEnergy();
        logger.info("用户精力刷新完成");
        
        // 刷新星星城数据
        starCityService.dailyUpdate();
        logger.info("星星城数据刷新完成");
        
        logger.info("每日刷新任务执行完成");
    } catch (Exception e) {
        logger.error("每日刷新任务执行失败", e);
    }
}
```

### 6. Controller 层（需要实现）
在 `UserController.java` 中添加 API：

```java
/**
 * 获取用户精力信息
 */
@GetMapping("/energy/{userId}")
public ResponseEntity<Map<String, Object>> getUserEnergy(@PathVariable String userId) {
    try {
        Map<String, Object> energyInfo = userMapper.getUserEnergy(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", energyInfo);
        
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "获取精力信息失败: " + e.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}
```

### 7. 前端显示（需要实现）
在 `LuckyWheel.jsx` 的魔法弹窗中显示精力：

```javascript
// 在魔法列表上方显示精力信息
{userEnergy && (
    <div style={{
        marginBottom: '15px',
        padding: '12px',
        background: 'linear-gradient(135deg, #667eea, #764ba2)',
        borderRadius: '8px',
        textAlign: 'center'
    }}>
        <div style={{fontSize: '14px', marginBottom: '5px'}}>
            ⚡ 当前精力
        </div>
        <div style={{fontSize: '24px', fontWeight: 'bold'}}>
            {userEnergy.energy} / {userEnergy.maxEnergy}
        </div>
    </div>
)}

// 在每个魔法按钮上显示精力消耗
<div style={{
    fontSize: '12px',
    color: userEnergy.energy >= magic.energyCost ? '#FFD700' : '#FF6B6B',
    marginTop: '4px'
}}>
    ⚡ 消耗 {magic.energyCost} 精力
</div>

// 施展魔法前检查精力
const castMagic = async (code) => {
    // 检查精力是否足够
    const magic = magics.find(m => m.code === code);
    if (userEnergy.energy < magic.energyCost) {
        alert(`精力不足！需要 ${magic.energyCost} 点精力，当前只有 ${userEnergy.energy} 点`);
        return;
    }
    
    // 施展魔法...
    const response = await fetch('/api/magic/cast', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: userName, code })
    });
    
    if (response.success) {
        // 刷新精力信息
        fetchUserEnergy();
    }
};
```

### 8. 文档（需要创建）
创建 `ENERGY_SYSTEM.md` 文档说明精力系统的规则和使用方法。

## 🎮 精力系统规则

### 基本规则
- 每个用户每天有 **15点精力**
- 每天凌晨12点自动恢复到满值
- 施展魔法会消耗对应的精力
- 精力不足时无法施展魔法

### 魔法精力消耗
| 魔法 | 精力消耗 | 每日次数 |
|------|----------|----------|
| 天降食物 | 5点 | 3次 |
| 改变天气 | 3点 | 3次 |
| 驱逐巨人 | 8点 | 1次 |

### 计算示例
假设秦小淮今天要施展魔法：
- 早上施展"天降食物"：15 - 5 = 10点精力剩余
- 中午施展"改变天气"：10 - 3 = 7点精力剩余
- 下午施展"改变天气"：7 - 3 = 4点精力剩余
- 晚上无法施展"天降食物"（需要5点，只有4点）
- 晚上可以施展"改变天气"（需要3点，有4点）：4 - 3 = 1点精力剩余
- 无法再施展任何魔法，等待第二天凌晨恢复

## 📝 实现检查清单

- [x] 数据库表结构迁移
- [x] User 实体类修改
- [x] Magic 实体类修改
- [x] PrizeInitService 初始化
- [ ] UserMapper 添加精力方法
- [ ] MagicMapper 添加 energy_cost 字段
- [ ] MagicService 添加精力检查
- [ ] DailyRefreshTask 添加精力刷新
- [ ] UserController 添加精力 API
- [ ] 前端显示精力信息
- [ ] 前端检查精力是否足够
- [ ] 创建系统文档
- [ ] 测试精力系统

## 🚀 下一步行动

由于当前响应已经很长，建议分步骤完成剩余工作：

1. 先完成 Mapper 层的修改
2. 然后完成 Service 层的修改
3. 再完成 Controller 层的修改
4. 最后完成前端的修改和测试

需要我继续实现哪一部分？

