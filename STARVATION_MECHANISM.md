# 🌟 星星城人口饥饿机制

## 📋 概述

星星城人口饥饿机制是一个重要的游戏平衡系统，确保玩家需要合理管理食物资源，避免人口过度增长导致资源短缺。

## 🎯 机制规则

### 饥饿条件
- **触发条件**: 当星星城的食物数量 < 人口数量时
- **执行频率**: 每小时检查一次（每小时的0分0秒）
- **人口减少**: 每次检查时人口减少0.5%
- **最低限制**: 人口不会低于0

### 计算公式
```
if (食物 < 人口) {
    减少数量 = round(当前人口 * 0.005)
    新人口 = max(0, 当前人口 - 减少数量)
}
```

## 🎨 前端特效

### 饥饿警告显示

当食物 < 人口时，星星城右下角的信息面板会显示饥饿警告特效：

#### 视觉效果
- **警告框**: 红色半透明背景，红色边框
- **警告文字**: "⚠️ 食物不足！人口正在下降"
- **详细信息**: "每小时人口-0.5% | 当前: X < Y"
- **脉冲动画**: 背景颜色和大小周期性变化
- **粒子特效**: 8个红色粒子随机闪烁
- **边框闪烁**: 边框颜色周期性变化

#### 动画效果
```css
/* 饥饿警告脉冲 */
@keyframes starvationPulse {
    0%, 100% { background: rgba(255, 0, 0, 0.3); transform: scale(1); }
    50% { background: rgba(255, 0, 0, 0.5); transform: scale(1.02); }
}

/* 饥饿粒子闪烁 */
@keyframes starvationParticle {
    0%, 100% { opacity: 0; transform: scale(0) translateY(0); }
    50% { opacity: 1; transform: scale(1) translateY(-5px); }
}

/* 边框闪烁 */
@keyframes starvationBorder {
    0%, 100% { opacity: 0.8; border-color: rgba(255, 0, 0, 0.8); }
    50% { opacity: 1; border-color: rgba(255, 100, 100, 1); }
}
```

#### 显示条件
```javascript
{starCityData && starCityData.food < starCityData.population && (
    <div className="starvation-warning">
        {/* 饥饿警告内容 */}
    </div>
)}
```

### 与其他特效的关系

- **特殊居住组合**: 显示在饥饿警告上方
- **升级信息**: 显示在饥饿警告下方
- **天气特效**: 独立显示，不影响饥饿警告

## 🏗️ 技术实现

### 后端架构

#### 1. 核心服务
- **StarCityService.checkAndHandleStarvation()**: 执行饥饿检查逻辑
- **HourlyRefreshTask.hourlyPopulationBonus()**: 每小时定时任务

#### 2. 执行流程
```
每小时定时任务
    ↓
1. 检查饥饿条件 (checkAndHandleStarvation)
    ↓
2. 执行特殊居住组合人口增长
    ↓
3. 记录日志
```

#### 3. 关键代码
```java
@Transactional
public void checkAndHandleStarvation() {
    StarCity starCity = getStarCity();
    Long food = starCity.getFood();
    Long population = starCity.getPopulation();

    if (food < population) {
        Long decreaseAmount = Math.round(population * 0.005);
        Long newPopulation = Math.max(0, population - decreaseAmount);
        starCity.setPopulation(newPopulation);
        starCityMapper.updateStarCity(starCity);
        
        logger.info("人口饥饿：食物({}) < 人口({})，人口减少{} (0.5%)，当前人口: {}", 
                   food, population, decreaseAmount, newPopulation);
    }
}
```

### 定时任务配置
```java
@Scheduled(cron = "0 0 * * * ?")  // 每小时的0分0秒执行
public void hourlyPopulationBonus() {
    // 1. 饥饿检查
    starCityService.checkAndHandleStarvation();
    
    // 2. 特殊居住组合人口增长
    // ...
}
```

## 🧪 测试

### 测试脚本
使用 `test-starvation.sh` 脚本进行测试：

```bash
./test-starvation.sh
```

### 测试内容
1. **获取当前数据**: 食物、人口、幸福度
2. **检查饥饿条件**: 判断是否满足食物 < 人口
3. **触发饥饿检查**: 调用管理员API手动执行
4. **验证结果**: 确认人口变化符合预期
5. **边界测试**: 验证人口为0时不再减少

### 测试场景
- ✅ **饥饿场景**: 食物 < 人口，人口减少0.5%
- ✅ **充足场景**: 食物 >= 人口，人口不变
- ✅ **边界场景**: 人口为0时不再减少

## 📊 游戏平衡

### 策略影响
1. **资源管理**: 玩家需要平衡人口增长和食物生产
2. **魔法使用**: "天降食物"魔法变得更加重要
3. **居住规划**: 特殊居住组合的人口增长需要谨慎使用

### 与其他系统的关系
- **每日更新**: 每天增加5000食物，人口增长为食物的1/8
- **魔法系统**: "天降食物"魔法可以紧急补充10000食物
- **特殊居住**: 每小时人口增长加成需要考虑食物供应

## 🔧 管理员功能

### API接口
- **POST** `/api/admin/test-hourly-tasks`: 手动触发每小时任务测试

### 使用场景
- 开发测试
- 问题排查
- 系统验证

## 📈 监控和日志

### 日志记录
```
INFO: 人口饥饿：食物(5000) < 人口(8000)，人口减少40 (0.5%)，当前人口: 7960
INFO: 每小时特殊居住组合人口增长和饥饿检查完成
```

### 监控指标
- 饥饿检查执行次数
- 人口减少次数
- 食物与人口比例

## 🎮 玩家体验

### 游戏提示
- 玩家可以通过星星城页面查看当前食物和人口
- 饥饿机制会在后台自动运行
- 建议玩家合理规划资源分配

### 应对策略
1. **预防**: 保持食物储备充足
2. **应急**: 使用"天降食物"魔法
3. **规划**: 控制人口增长速度

## 🔄 未来扩展

### 可能的改进
1. **饥饿警告**: 当食物接近人口时给出提示
2. **饥饿等级**: 根据食物短缺程度调整人口减少量
3. **特殊事件**: 饥饿期间的特殊事件和奖励

---

**注意**: 饥饿机制是游戏平衡的重要组成部分，确保玩家需要合理管理资源，增加游戏的策略性和挑战性。
