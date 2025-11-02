# ✨ 魔法系统

## 📋 概述

魔法系统是星星城的一个重要功能，允许秦小淮施展各种魔法来影响星星城的状态。每个魔法都有每日使用次数限制，每天凌晨12点自动刷新。

## 🎯 魔法列表

### 1. 🌧️ 天降食物 (FOOD_RAIN)
- **效果**: 增加20000份食物到星星城，并增加2点幸福度
- **每日次数**: 3次
- **刷新时间**: 每天凌晨12点
- **特效**: 天上一道圣光降下，食物数字滚动增加
- **描述**: 施展魔法后，将会有20000份食物从天而降，储存到星星城的食物仓库中，同时增加2点幸福度。

### 2. 🌤️ 改变天气 (CHANGE_WEATHER)
- **效果**: 立即刷新星星城天气
- **每日次数**: 3次
- **刷新时间**: 每天凌晨12点
- **特效**: 天气粒子特效
- **描述**: 施展魔法后，星星城的天气将立即改变为随机的新天气，包括晴天、雨天、雪天等。

### 3. 👹 驱逐巨人 (BANISH_GIANT)
- **效果**: 立即停止巨人进攻
- **每日次数**: 1次
- **刷新时间**: 每天凌晨12点
- **特效**: 巨人逐渐暗淡消失
- **描述**: 施展魔法后，正在进攻的巨人将被驱逐，巨人进攻立即停止，巨人逐渐暗淡消失。

## 🔧 技术实现

### 后端实现

#### 数据库表结构
```sql
CREATE TABLE magic (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    daily_limit INTEGER NOT NULL DEFAULT 3,
    remaining_uses INTEGER NOT NULL DEFAULT 3,
    last_refresh_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT (datetime('now', 'localtime')),
    updated_at DATETIME NOT NULL DEFAULT (datetime('now', 'localtime'))
)
```

#### 魔法常量
```java
public static final String MAGIC_FOOD_RAIN = "FOOD_RAIN";
public static final String MAGIC_CHANGE_WEATHER = "CHANGE_WEATHER";
public static final String MAGIC_BANISH_GIANT = "BANISH_GIANT";
```

#### 魔法执行逻辑
```java
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
```

### 前端实现

#### 魔法状态管理
```javascript
const [magics, setMagics] = useState([]) // 魔法列表
const [castingMagic, setCastingMagic] = useState(null) // 正在施展的魔法
const [showMagicModal, setShowMagicModal] = useState(false) // 魔法弹窗
```

#### 特效状态管理
```javascript
const [showFoodRain, setShowFoodRain] = useState(false) // 天降食物特效
const [showWeatherChange, setShowWeatherChange] = useState(false) // 改变天气特效
const [isGiantBanishing, setIsGiantBanishing] = useState(false) // 巨人驱逐特效
```

#### 魔法施展函数
```javascript
const castMagic = async (code) => {
    // 权限检查
    if (userName !== '秦小淮') {
        alert('只有秦小淮可以施展魔法')
        return
    }

    // 调用后端API
    const response = await fetch('/api/magic/cast', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: new URLSearchParams({
            code: code,
            userId: userName
        })
    })

    // 触发特效
    if (code === 'FOOD_RAIN') {
        triggerFoodRainEffect()
    } else if (code === 'CHANGE_WEATHER') {
        triggerWeatherChangeEffect()
    } else if (code === 'BANISH_GIANT') {
        triggerBanishGiantEffect()
    }
}
```

## 🎨 视觉效果

### 天降食物特效
- **圣光效果**: 从天而降的金色光束
- **数字滚动**: 食物数量向上滚动增加
- **持续时间**: 3秒

### 改变天气特效
- **粒子效果**: 彩色天气粒子飘散
- **持续时间**: 2秒

### 驱逐巨人特效
- **暗淡效果**: 巨人逐渐变暗
- **透明度**: 从1渐变到0
- **过渡时间**: 3秒
- **火焰消失**: 脚底火焰和烟雾同时消失

## 🔄 定时刷新

### 每日刷新机制
- **刷新时间**: 每天凌晨12点
- **刷新内容**: 所有魔法的剩余次数重置为每日限制
- **实现方式**: `DailyRefreshTask` 定时任务

### 刷新逻辑
```java
@Transactional
public void refreshAllMagicDailyUses() {
    List<Magic> magics = magicMapper.selectAll();
    for (Magic magic : magics) {
        magicMapper.refreshDailyUses(magic.getCode());
        logger.info("定时刷新魔法次数: {}, 刷新为: {}", magic.getCode(), magic.getDailyLimit());
    }
}
```

## 🎮 游戏平衡

### 使用限制
- **权限限制**: 只有秦小淮可以使用魔法
- **次数限制**: 每个魔法都有每日使用次数限制
- **冷却机制**: 施展魔法后需要等待特效结束

### 策略考虑
- **天降食物**: 适合在食物短缺时使用
- **改变天气**: 适合在需要特定天气时使用
- **驱逐巨人**: 适合在巨人进攻时紧急使用

## 📊 API接口

### 获取魔法列表
```
GET /api/magic/list
```

### 施展魔法
```
POST /api/magic/cast
Content-Type: application/x-www-form-urlencoded

code=FOOD_RAIN&userId=秦小淮
```

### 管理员刷新魔法次数
```
POST /api/magic/refresh
Authorization: Bearer {admin_token}
```

## 🔮 未来扩展

### 可能的魔法
- **治愈魔法**: 恢复人口或幸福指数
- **防御魔法**: 减少巨人伤害
- **召唤魔法**: 召唤友军单位
- **时间魔法**: 加速或减缓时间流逝

### 魔法升级系统
- **魔法等级**: 不同等级的魔法效果不同
- **魔法材料**: 需要特定材料才能施展
- **魔法研究**: 通过研究解锁新魔法

## 📝 总结

魔法系统为星星城增加了丰富的策略性和趣味性，让玩家可以通过施展魔法来应对各种挑战。每个魔法都有其独特的用途和视觉效果，为游戏体验增添了更多乐趣。