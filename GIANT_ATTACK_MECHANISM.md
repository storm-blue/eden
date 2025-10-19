# 🏰 巨人进攻功能

## 📋 概述

巨人进攻功能是星星城的一个重要游戏机制，增加了游戏的挑战性和策略性。巨人会定期进攻星星城，对人口造成持续伤害。

## 🎯 机制规则

### 进攻触发
- **触发频率**: 每6小时检查一次
- **触发概率**: 1/8 (12.5%)的概率
- **检查时间**: 每6小时的0分0秒
- **持续时间**: 直到手动结束或人口为0

### 伤害机制
- **伤害频率**: 每10分钟造成一次伤害
- **人口损失**: 人口总数的0.5%
- **幸福指数下降**: 每10分钟幸福指数下降1
- **最低限制**: 人口和幸福指数都不会低于0
- **自动结束**: 当人口为0时，巨人进攻自动结束

### 计算公式
```
if (巨人正在进攻) {
    人口减少数量 = round(当前人口 * 0.005)  // 0.5%
    新人口 = max(0, 当前人口 - 人口减少数量)
    新幸福指数 = max(0, 当前幸福指数 - 1)  // 每10分钟下降1
}
```

## 🎨 前端显示

### 巨人位置
- **显示位置**: 每个等级都有独立的巨人位置配置
- **图片路径**: `/picture/giant.png`
- **显示条件**: 仅当巨人正在进攻时显示
- **配置方式**: 在 `getBuildingPositions` 函数中为每个等级配置 `giant` 位置

#### 各等级巨人位置配置
| 等级 | 城市名称 | 巨人位置 (top, left) | 说明 |
|------|----------|---------------------|------|
| LV1 | 晨曦小镇 | `23%, 48%` | 与城堡位置相同 |
| LV2 | 繁花之城 | `32%, 50%` | 与城堡位置相同 |
| LV3 | 星辰都市 | `30%, 49%` | 与城堡位置相同 |
| LV4 | 云端之城 | `25%, 50%` | 与城堡位置相同 |
| LV5 | 梦幻星城 | `28%, 52%` | 与城堡位置相同 |
| LV6 | 月光之城 | `30%, 50%` | 与城堡位置相同 |
| LV7 | 银河之都 | `25%, 48%` | 与城堡位置相同 |
| LV8 | 永恒圣域 | `22%, 45%` | 与城堡位置相同 |

#### 配置示例
```javascript
const lv1Positions = {
    castle: {top: '23%', left: '48%'},
    city_hall: {top: '12%', left: '72%'},
    palace: {top: '8%', left: '23%'},
    white_dove_house: {top: '31%', left: '61%'},
    park: {top: '50%', left: '40%'},
    giant: {top: '23%', left: '48%'}  // LV1巨人位置
}
```

#### 椭圆火焰配置
```javascript
// 多圈火焰椭圆参数
const rings = [
    { count: 12, radiusX: 25, radiusY: 15 }, // 内圈
    { count: 16, radiusX: 35, radiusY: 20 }, // 中圈
    { count: 20, radiusX: 45, radiusY: 25 }   // 外圈
];

// 多圈烟雾椭圆参数
const smokeRings = [
    { count: 15, radiusX: 30, radiusY: 20 }, // 内圈烟雾
    { count: 20, radiusX: 40, radiusY: 25 }, // 中圈烟雾
    { count: 25, radiusX: 50, radiusY: 30 }  // 外圈烟雾
];
```

#### Z-Index层级设置
```javascript
// 层级从高到低
巨人本体层: zIndex: 30     // 巨人图片、攻击特效、粒子
火焰特效层: zIndex: 15     // 独立的火焰和烟雾层
```

### 视觉效果
- **巨人图片**: 120x120px，带红色阴影，在巨人本体层（zIndex: 30）
- **攻击特效**: 红色脉冲光环，在巨人本体层
- **粒子特效**: 8个红色粒子爆炸效果，在巨人本体层
- **火焰特效**: 48个脚底火焰，3圈椭圆围绕巨人脚，橙色到金色渐变，独立层（zIndex: 15）
- **烟雾特效**: 60个脚底烟雾，3圈椭圆范围内向上飘散，独立层（zIndex: 15）
- **动画效果**: 巨人静止，火焰闪烁，烟雾上升

### 动画效果
```css
/* 攻击脉冲 */
@keyframes giantAttackPulse {
    0% { transform: translate(-50%, -50%) scale(1); opacity: 0.2; }
    50% { transform: translate(-50%, -50%) scale(1.1); opacity: 0.4; }
    100% { transform: translate(-50%, -50%) scale(1); opacity: 0.2; }
}

/* 粒子爆炸 */
@keyframes giantParticleExplode {
    0% { opacity: 0; transform: translate(-50%, -50%) scale(0); }
    20% { opacity: 1; transform: translate(-50%, -50%) scale(1); }
    100% { opacity: 0; transform: translate(-50%, -50%) scale(2); }
}

/* 脚底火焰闪烁 */
@keyframes giantFireFlicker {
    0% { transform: scaleY(1) scaleX(1); opacity: 0.8; }
    25% { transform: scaleY(1.2) scaleX(0.8); opacity: 1; }
    50% { transform: scaleY(0.9) scaleX(1.1); opacity: 0.9; }
    75% { transform: scaleY(1.1) scaleX(0.9); opacity: 0.7; }
    100% { transform: scaleY(1) scaleX(1); opacity: 0.8; }
}

/* 脚底烟雾上升 */
@keyframes giantSmokeRise {
    0% { opacity: 0; transform: translateY(0) scale(0.5); }
    20% { opacity: 0.6; transform: translateY(-10px) scale(0.8); }
    50% { opacity: 0.4; transform: translateY(-25px) scale(1.2); }
    80% { opacity: 0.2; transform: translateY(-40px) scale(1.5); }
    100% { opacity: 0; transform: translateY(-60px) scale(2); }
}
```

## 🔧 后端实现

### 数据库表结构
```sql
CREATE TABLE giant_attack (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    is_active BOOLEAN NOT NULL DEFAULT 0,
    start_time DATETIME,
    end_time DATETIME,
    last_damage_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT (datetime('now', 'localtime')),
    update_time DATETIME NOT NULL DEFAULT (datetime('now', 'localtime'))
);
```

### 核心服务类
- **GiantAttackService**: 巨人进攻业务逻辑
- **GiantAttackMapper**: 数据库操作接口
- **GiantAttackController**: API控制器

### 定时任务
```java
// 每6小时检查巨人进攻
@Scheduled(cron = "0 0 */6 * * ?")
public void checkGiantAttack()

// 每10分钟处理巨人伤害
@Scheduled(cron = "0 */10 * * * ?")
public void processGiantDamage()
```

## 🌐 API接口

### 获取巨人进攻状态
```
GET /api/giant-attack/status
Response: {
    "isAttacking": boolean,
    "startTime": "2024-01-01T00:00:00",
    "endTime": "2024-01-01T01:00:00",
    "lastDamageTime": "2024-01-01T00:10:00"
}
```

### 手动触发巨人进攻（管理员）
```
POST /api/giant-attack/trigger
Headers: Authorization: Bearer <admin_token>
Response: {
    "success": true,
    "message": "巨人进攻已触发"
}
```

### 手动结束巨人进攻（管理员）
```
POST /api/giant-attack/end
Headers: Authorization: Bearer <admin_token>
Response: {
    "success": true,
    "message": "巨人进攻已结束"
}
```

### 手动处理巨人伤害（管理员）
```
POST /api/giant-attack/damage
Headers: Authorization: Bearer <admin_token>
Response: {
    "success": true,
    "message": "巨人伤害已处理"
}
```

## 🧪 测试

### 测试脚本
```bash
./test-giant-attack.sh
```

### 测试场景
- ✅ **触发测试**: 手动触发巨人进攻
- ✅ **伤害测试**: 验证0.5%人口损失
- ✅ **状态测试**: 验证巨人进攻状态变化
- ✅ **边界测试**: 验证人口为0时的处理
- ✅ **结束测试**: 验证手动结束功能

### 测试数据示例
```
人口: 100,000
预期伤害: 500 (0.5%)
实际伤害: 500
结果: ✅ 通过

人口: 1,000,000
预期伤害: 5,000 (0.5%)
实际伤害: 5,000
结果: ✅ 通过
```

## 📊 游戏平衡

### 策略影响
1. **资源管理**: 玩家需要储备足够的人口
2. **防御策略**: 考虑巨人进攻的时间窗口
3. **恢复计划**: 巨人进攻后的重建策略
4. **风险控制**: 避免在巨人进攻期间进行高风险操作

### 平衡考虑
- **频率**: 每6小时检查，不会过于频繁
- **概率**: 1/8概率，保持一定的随机性
- **伤害**: 0.5%比例，不会造成毁灭性打击
- **持续时间**: 可手动结束，给玩家控制权

## 📈 监控和日志

### 日志记录
```
INFO: 巨人进攻开始！进攻时间: 2024-01-01T00:00:00
INFO: 巨人造成伤害：人口减少500 (0.5%)，当前人口: 99500
INFO: 巨人进攻结束！结束时间: 2024-01-01T01:00:00
```

### 监控指标
- 巨人进攻触发次数
- 巨人进攻持续时间
- 人口损失总量
- 巨人进攻结束原因

## 🎮 玩家体验

### 视觉反馈
- 巨人图片显示在城堡位置
- 红色攻击特效和粒子效果
- 动画效果增强沉浸感

### 策略深度
- 增加游戏的挑战性
- 需要长期规划和管理
- 提供新的游戏目标

### 平衡性
- 不会过于频繁影响游戏体验
- 给玩家足够的应对时间
- 保持游戏的趣味性

## 🔮 未来扩展

### 可能的改进
1. **不同等级巨人**: 根据星星城等级调整巨人强度
2. **防御机制**: 添加防御建筑或魔法
3. **预警系统**: 提前通知巨人进攻
4. **奖励机制**: 成功抵御巨人进攻的奖励
5. **巨人类型**: 不同类型的巨人有不同的攻击模式

### 配置选项
- 巨人进攻概率调整
- 伤害比例调整
- 进攻频率调整
- 巨人位置配置
