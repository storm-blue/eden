# 魔法系统文档

## 📚 概述

魔法系统是星星城的特殊功能，允许秦小淮施展各种魔法，产生特殊效果。每种魔法每天有固定的施展次数，每天凌晨0点自动刷新。

## ✨ 功能特点

1. **权限控制**: 只有秦小淮可以访问和施展魔法
2. **次数限制**: 每种魔法每天有固定的施展次数
3. **自动刷新**: 每天凌晨0点自动刷新所有魔法的次数
4. **视觉特效**: 施展魔法时播放精美的动画特效
5. **即时生效**: 魔法效果立即应用到游戏数据中

## 🎯 当前支持的魔法

### 1. 天降食物 (FOOD_RAIN)

- **描述**: 施展魔法后，将会有10000份食物从天而降，储存到星星城的食物仓库中
- **每日次数**: 3次
- **效果**: 
  - 食物数量 +10000
  - 播放天降食物动画特效（30个食物emoji从天而降）
- **特效时长**: 3秒

### 2. 改变天气 (CHANGE_WEATHER)

- **描述**: 施展魔法后，星星城的天气将立即改变为随机的新天气，包括晴天、雨天、雪天等
- **每日次数**: 3次
- **效果**: 
  - 立即刷新天气为随机新天气
  - 播放天气变化动画特效（光效、天气图标、魔法粒子）
- **特效时长**: 2秒

## 🏗️ 系统架构

### 后端架构

#### 1. 数据库表结构

```sql
CREATE TABLE magic (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,      -- 魔法代码
    name VARCHAR(100) NOT NULL,            -- 魔法名称
    description TEXT,                       -- 魔法描述
    daily_limit INTEGER NOT NULL DEFAULT 3, -- 每日可施展次数
    remaining_uses INTEGER NOT NULL DEFAULT 3, -- 当日剩余次数
    last_refresh_at TIMESTAMP NOT NULL,    -- 上次刷新时间
    created_at TIMESTAMP NOT NULL          -- 创建时间
)
```

#### 2. 核心类

- **实体类**: `Magic.java` - 魔法实体
- **Mapper**: `MagicMapper.java` / `MagicMapper.xml` - 数据访问层
- **Service**: `MagicService.java` - 业务逻辑层
  - `castMagic()`: 施展魔法
  - `refreshAllMagicDailyUses()`: 刷新所有魔法次数（定时任务调用）
  - `executeMagicEffect()`: 执行魔法效果
  - `executeFoodRainMagic()`: 执行天降食物魔法效果
  - `executeChangeWeatherMagic()`: 执行改变天气魔法效果
- **Controller**: `MagicController.java` - API接口层
- **Task**: `DailyRefreshTask.java` - 定时任务（每日凌晨0点刷新）

#### 3. API接口

##### 3.1 获取魔法列表

```
GET /api/magic/list?userId={userId}
```

**请求参数**:
- `userId`: 用户名（必须为"秦小淮"）

**响应**:
```json
{
  "success": true,
  "magics": [
    {
      "id": 1,
      "code": "FOOD_RAIN",
      "name": "天降食物",
      "description": "施展魔法后，将会有10000份食物从天而降...",
      "dailyLimit": 3,
      "remainingUses": 2,
      "lastRefreshAt": "2024-10-16T00:00:00",
      "createdAt": "2024-10-16T00:00:00"
    }
  ]
}
```

##### 3.2 施展魔法

```
POST /api/magic/cast
Content-Type: application/x-www-form-urlencoded
```

**请求参数**:
- `code`: 魔法代码（如: FOOD_RAIN）
- `userId`: 用户名（必须为"秦小淮"）

**响应**:
```json
{
  "success": true,
  "message": "魔法施展成功",
  "remainingUses": 2
}
```

**错误响应**:
```json
{
  "success": false,
  "message": "今日魔法次数已用完，请明日再来"
}
```

### 前端架构

#### 1. 组件结构

- **主组件**: `LuckyWheel.jsx` - 包含魔法管理入口
- **魔法弹窗**: `MagicModal.jsx` - 魔法管理界面
- **样式文件**: `MagicModal.css` - 弹窗样式
- **动画样式**: `LuckyWheel.css` - 特效动画（foodFall）

#### 2. 状态管理

```javascript
const [showMagicModal, setShowMagicModal] = useState(false)  // 显示魔法管理弹窗
const [magics, setMagics] = useState([])                     // 魔法列表
const [loadingMagics, setLoadingMagics] = useState(false)    // 加载状态
const [castingMagic, setCastingMagic] = useState(null)       // 正在施展的魔法
const [showFoodRain, setShowFoodRain] = useState(false)      // 天降食物特效
```

#### 3. 核心函数

- `fetchMagics()`: 获取魔法列表
- `castMagic(code)`: 施展魔法
- `triggerFoodRainEffect()`: 触发天降食物特效
- `triggerWeatherChangeEffect()`: 触发改变天气特效

#### 4. UI组件

##### 魔法管理按钮
- 位置: 星星城页面底部（命令按钮右侧）
- 样式: 紫色渐变按钮 "✨ 施展魔法"
- 权限: 仅秦小淮可见

##### 魔法管理弹窗
- 显示所有可用魔法
- 显示每个魔法的剩余次数 (X/3)
- 可展开查看魔法详情
- 次数用完的魔法显示"已用完"并禁用按钮
- 支持移动端横屏显示

##### 天降食物特效
- 30个随机食物emoji从天而降
- 每个食物随机位置、随机延迟、随机速度
- 边下落边旋转（0-360度）
- 特效持续3秒后自动消失

##### 改变天气特效
- 中心光效脉冲动画（蓝白色渐变）
- 5个天气图标依次出现（☀️🌧️❄️☁️🌙）
- 20个魔法粒子随机飘浮
- 特效持续2秒后自动消失

## 🧪 测试

### 运行测试脚本

```bash
./test-magic.sh
```

### 测试内容

1. 获取魔法列表
2. 获取施展前的食物数量和天气
3. 施展天降食物魔法
4. 验证食物数量增加10000
5. 验证天降食物魔法次数减少
6. 施展改变天气魔法
7. 验证天气是否改变
8. 验证改变天气魔法次数减少
9. 连续施展直到用完次数
10. 验证次数用完后无法施展

### 预期结果

✅ 所有测试通过，魔法系统正常工作

## 📝 使用流程

### 秦小淮施展魔法

1. 进入星星城页面
2. 点击底部"✨ 施展魔法"按钮
3. 在弹窗中查看可用魔法和剩余次数
4. 点击"施展魔法"按钮
5. 观看精美的特效动画
6. 食物数量立即增加10000（天降食物）或天气立即改变（改变天气）
7. 魔法剩余次数-1

### 次数刷新

- 每天凌晨0点自动刷新所有魔法次数为初始值
- 由定时任务 `DailyRefreshTask` 执行

## 🔧 扩展新魔法

### 1. 后端添加新魔法

#### 在 `PrizeInitService.java` 添加初始化

```java
String insertMagic = """
    INSERT INTO magic (code, name, description, daily_limit, remaining_uses, last_refresh_at, created_at)
    VALUES ('NEW_MAGIC', '魔法名称', 
            '魔法描述', 
            5, 5, datetime('now', 'localtime'), datetime('now', 'localtime'))
    """;
statement.execute(insertMagic);
```

#### 在 `MagicService.java` 添加魔法效果

```java
public static final String MAGIC_NEW_MAGIC = "NEW_MAGIC";

private void executeMagicEffect(String code) {
    switch (code) {
        case MAGIC_FOOD_RAIN:
            executeFoodRainMagic();
            break;
        case MAGIC_NEW_MAGIC:
            executeNewMagic();  // 新魔法逻辑
            break;
        default:
            logger.warn("未知的魔法代码: {}", code);
    }
}

private void executeNewMagic() {
    // 实现新魔法的效果
}
```

### 2. 前端添加新魔法特效

#### 在 `LuckyWheel.jsx` 添加特效触发

```javascript
// 触发特效
if (code === 'FOOD_RAIN') {
    triggerFoodRainEffect()
} else if (code === 'NEW_MAGIC') {
    triggerNewMagicEffect()
}
```

#### 在 `LuckyWheel.jsx` 添加特效渲染

```javascript
{/* 新魔法特效 */}
{showNewMagic && (
    <div style={{ /* 特效样式 */ }}>
        {/* 特效内容 */}
    </div>
)}
```

#### 在 `LuckyWheel.css` 添加动画

```css
@keyframes newMagicAnimation {
    /* 动画关键帧 */
}
```

## 🎨 特效设计建议

### 动画原则
1. **流畅性**: 使用CSS动画，避免卡顿
2. **可见性**: 特效要明显但不遮挡主要内容
3. **时长控制**: 2-5秒为宜，不宜过长
4. **性能优化**: 控制元素数量，避免过多DOM操作

### 常用动画效果
- 从上到下: `translateY`
- 旋转: `rotate`
- 缩放: `scale`
- 淡入淡出: `opacity`
- 模糊: `filter: blur()`
- 发光: `box-shadow`

## 🐛 故障排查

### 问题: 魔法次数不刷新

**检查**:
1. 定时任务是否正常运行
2. 数据库 `last_refresh_at` 字段是否更新
3. 服务器时间是否正确

### 问题: 特效不显示

**检查**:
1. 状态 `showFoodRain` 是否正确设置
2. CSS动画是否正确加载
3. 浏览器控制台是否有错误

### 问题: 食物数量未增加

**检查**:
1. 后端日志查看魔法是否执行成功
2. 数据库 `star_city` 表的 `food` 字段
3. API响应是否正确

## 📱 移动端适配

- 魔法按钮位置自适应
- 弹窗强制横屏显示
- 特效大小根据设备调整
- 触摸友好的交互设计

## 🔐 安全性

- 所有魔法API都验证用户权限
- 只有秦小淮可以访问和施展
- 防止恶意刷新次数
- 事务保证数据一致性

## 📊 数据统计

可以在后端添加魔法使用统计：
- 每种魔法的施展次数
- 施展时间分布
- 效果累计（如总共产生的食物）

## 🎉 总结

魔法系统为游戏增添了趣味性和互动性，通过精美的视觉特效和实用的游戏效果，提升了用户体验。系统设计灵活，易于扩展新魔法，后续可以添加更多有趣的魔法效果！

