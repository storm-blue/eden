# 🌦️ 星星城天气系统说明

## ✨ 系统概述

星星城现在拥有完整的动态天气系统，包含五种天气类型，每6小时自动随机切换。每种天气都有独特的视觉特效！

---

## 🎯 天气类型

### 1. ☀️ 晴天 (sunny)
- **视觉特效**：
  - 🌞 右上角有一个发光的太阳
  - ✨ 太阳周围有12条旋转的光线
  - 💛 金黄色辐射光晕效果
- **动画**：
  - 太阳缓慢旋转（20秒一圈）
  - 光线随太阳一起旋转

### 2. 🌧️ 雨天 (rainy)
- **视觉特效**：
  - 💧 80个雨滴（桌面）/ 50个（移动端）
  - 🌫️ 顶部40%区域有淡蓝色雨雾
  - 💦 地面20个水花（桌面）/ 12个（移动端）
- **动画**：
  - 雨滴垂直下落，速度：0.8-1.2秒
  - 水花在地面扩散消失

### 3. ❄️ 雪天 (snowy)
- **视觉特效**：
  - ❄️ 60个雪花（桌面）/ 40个（移动端）
  - 🌨️ 雪花大小随机：8-16px
  - ✨ 雪花飘舞轨迹带旋转
- **动画**：
  - 雪花缓慢飘落，速度：3-5秒
  - 边下落边旋转360度
  - 轻微水平飘移

### 4. ☁️ 多云 (cloudy)
- **视觉特效**：
  - ☁️ 6朵云（桌面）/ 4朵（移动端）
  - 🌥️ 云朵半透明（opacity: 0.6）
  - 📏 云朵在不同高度分布
- **动画**：
  - 云朵从左向右缓慢飘动
  - 每朵云速度不同（20-40秒横穿屏幕）
  - 错落有致的时间延迟

### 5. 🌙 夜晚 (night)
- **视觉特效**：
  - 🌙 右上角一轮发光的月亮
  - ⭐ 25颗闪烁的星星（桌面）/ 15颗（移动端）
  - 🌌 深蓝色半透明夜晚遮罩（opacity: 0.4）
- **动画**：
  - 月亮呼吸般的发光效果（3秒循环）
  - 星星随机闪烁（1.5-3秒）
  - 营造宁静的夜晚氛围

---

## 🔧 技术实现

### 后端

#### 1. **StarCity实体** (`StarCity.java`)
```java
private String weather;  // 天气状态: sunny, rainy, snowy, cloudy, night
```

#### 2. **数据库迁移** (`PrizeInitService.java`)
- 自动添加 `weather` 列到 `star_city` 表
- 默认值：`sunny`
- 启动时自动检查并迁移

#### 3. **定时任务** (`WeatherRefreshTask.java`)
```java
@Scheduled(cron = "0 0 0/6 * * ?")  // 每6小时执行
public void refreshWeather()
```
- **执行时间**：每天 0:00、6:00、12:00、18:00
- **逻辑**：随机选择一种新天气（确保与当前天气不同）
- **日志**：详细记录天气变化

#### 4. **Mapper更新** (`StarCityMapper.xml`)
- 所有SQL查询都包含 `weather` 字段
- SELECT、INSERT、UPDATE 都已更新

### 前端

#### 1. **条件渲染** (`LuckyWheel.jsx`)
```javascript
{starCityData?.weather === 'rainy' && (
    // 雨天特效
)}
{starCityData?.weather === 'snowy' && (
    // 雪天特效
)}
// ... 其他天气
```

#### 2. **性能优化**
- ✅ GPU加速：`transform: translateZ(0)`
- ✅ 性能提示：`willChange: 'transform'`
- ✅ 移动端优化：减少粒子数量
- ✅ 不影响交互：`pointerEvents: 'none'`

#### 3. **CSS动画** (`LuckyWheel.css`)
- `@keyframes rainDrop` - 雨滴下落
- `@keyframes rainSplash` - 水花扩散
- `@keyframes snowFall` - 雪花飘落
- `@keyframes cloudMove` - 云朵飘动
- `@keyframes moonGlow` - 月亮发光
- `@keyframes starTwinkle` - 星星闪烁
- `@keyframes sunRotate` - 太阳旋转

---

## 📊 天气切换逻辑

### 随机算法
```java
// 从5种天气中随机选择
String newWeather = WEATHER_TYPES[random.nextInt(5)];

// 确保新天气与旧天气不同
while (newWeather.equals(oldWeather)) {
    newWeather = WEATHER_TYPES[random.nextInt(5)];
}
```

### 切换时间表
| 时间 | 说明 |
|------|------|
| 00:00 | 自动切换天气 |
| 06:00 | 自动切换天气 |
| 12:00 | 自动切换天气 |
| 18:00 | 自动切换天气 |

---

## 🎨 视觉效果对比

| 天气 | 粒子数（桌面/移动） | 主色调 | 氛围 |
|------|-------------------|--------|------|
| ☀️ 晴天 | 1个太阳 + 12条光线 | 金黄色 | 明亮、温暖 |
| 🌧️ 雨天 | 80/50 + 20/12 | 淡蓝色 | 清凉、湿润 |
| ❄️ 雪天 | 60/40 | 白色 | 寒冷、纯净 |
| ☁️ 多云 | 6/4 | 灰白色 | 阴沉、平静 |
| 🌙 夜晚 | 1个月亮 + 25/15 | 深蓝色 | 宁静、神秘 |

---

## 🚀 使用方法

### 查看当前天气
1. 进入星星城
2. 自动显示当前天气特效
3. 前端从 `starCityData.weather` 获取天气状态

### 手动触发天气切换（仅供测试）
可以通过直接修改数据库来测试不同天气：
```sql
UPDATE star_city SET weather = 'snowy' WHERE id = 1;
```

### 调整刷新频率
修改 `WeatherRefreshTask.java` 中的cron表达式：
```java
@Scheduled(cron = "0 0 0/6 * * ?")  // 每6小时
// 改为
@Scheduled(cron = "0 0 0/3 * * ?")  // 每3小时
```

---

## 📁 文件清单

### 后端文件
```
eden-server/
├── src/main/java/com/eden/lottery/
│   ├── entity/StarCity.java              ✅ 添加weather字段
│   ├── task/WeatherRefreshTask.java      ✅ 新建定时任务
│   └── service/PrizeInitService.java     ✅ 添加数据库迁移
└── src/main/resources/
    └── mapper/StarCityMapper.xml         ✅ 更新SQL语句
```

### 前端文件
```
eden-web/src/components/
├── LuckyWheel.jsx    ✅ 添加5种天气特效组件
└── LuckyWheel.css    ✅ 添加6个天气动画
```

---

## 🎯 特色功能

### 1. **智能切换**
- 新天气永远不会与旧天气相同
- 最多尝试10次确保切换成功

### 2. **响应式设计**
- 移动端自动减少粒子数量
- 桌面端完整特效体验

### 3. **性能优化**
- GPU加速所有动画
- 使用CSS transform而非position
- 合理的粒子数量控制

### 4. **日志记录**
```
2025-10-08 12:00:00 - 开始刷新星星城天气...
2025-10-08 12:00:01 - 星星城天气已刷新: sunny -> rainy
```

---

## 🔍 调试技巧

### 查看当前天气
```javascript
console.log('当前天气:', starCityData?.weather);
```

### 查看天气特效是否渲染
打开浏览器开发工具，查看DOM结构：
- 雨天：寻找 `key="rain-*"` 的元素
- 雪天：寻找 `key="snow-*"` 的元素
- 等等...

### 后端日志
启动后端时关注控制台输出：
```
2025-10-08 12:00:00 - 开始刷新星星城天气...
2025-10-08 12:00:01 - 星星城天气已刷新: sunny -> rainy
```

---

## 🎉 完成状态

✅ **后端**：
- [x] StarCity实体添加weather字段
- [x] 数据库自动迁移
- [x] 定时任务每6小时刷新
- [x] Mapper更新所有SQL

✅ **前端**：
- [x] 雨天特效（改为条件渲染）
- [x] 雪天特效
- [x] 多云特效
- [x] 夜晚特效
- [x] 晴天特效
- [x] 所有CSS动画

✅ **性能**：
- [x] GPU加速
- [x] 移动端优化
- [x] 不影响交互

---

**实现时间**：2025-10-08  
**系统版本**：v2.0  
**特效总数**：5种天气 × 独特视觉效果  
**动画总数**：6个CSS关键帧动画  

