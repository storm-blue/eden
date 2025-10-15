# 🌈 彩虹命令功能说明

## 功能概述

"创造彩虹"是一个新的命令，由秦小淮颁布后，会在星星城上空显示一道横跨整个城市的美丽彩虹。

## 技术实现

### 后端

1. **DecreeService.java**
   - 添加了 `DECREE_CREATE_RAINBOW` 常量
   - 实现了 `executeCreateRainbowDecree()` 方法（纯前端效果，后端只记录日志）

2. **PrizeInitService.java**
   - 在数据库初始化时自动创建 "CREATE_RAINBOW" 命令记录
   - 命令名称：创造彩虹
   - 命令描述：让星星城上空出现美丽的彩虹！彩虹将横跨整个星星城，为所有居民带来美好的祝福。

### 前端

1. **状态管理 (LuckyWheel.jsx)**
   - 添加了 `isRainbowActive` 状态，检测彩虹命令是否激活
   - 在星星城页面加载时调用 `fetchAllDecrees()` 获取所有命令状态
   - 所有用户都可以看到彩虹效果，不限于秦小淮

2. **彩虹视觉效果**
   - 使用7个彩色圆环叠加形成彩虹弧形（红橙黄绿蓝靛紫）
   - 添加光晕效果增强视觉美感
   - 使用 `clipPath` 只显示上半部分，形成经典的彩虹弧
   - 移动端和桌面端适配不同的边框宽度

3. **动画效果 (LuckyWheel.css)**
   - `rainbowAppear` 动画：彩虹从小到大、从透明到显现的渐入效果
   - 持续时间 2 秒，ease-out 缓动

## 使用方式

### 通过前端界面

1. 以"秦小淮"身份登录
2. 进入星星城
3. 点击左上角"📜 颁布命令"按钮
4. 在命令列表中找到"创造彩虹"
5. 点击"颁布命令"按钮

### 通过测试脚本

```bash
# 运行测试脚本
./test-rainbow-decree.sh
```

测试脚本会：
1. 获取命令列表
2. 颁布彩虹命令
3. 验证命令已激活
4. 等待用户查看效果
5. 取消彩虹命令
6. 验证命令已取消

### 通过API

```bash
# 颁布彩虹命令
curl -X POST "http://localhost:8080/api/decree/issue" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=CREATE_RAINBOW&userId=秦小淮"

# 取消彩虹命令
curl -X POST "http://localhost:8080/api/decree/cancel" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=CREATE_RAINBOW&userId=秦小淮"
```

## 效果展示

### 彩虹特性

- **位置**：星星城上空中央偏上（top: 15%）
- **大小**：横跨整个城市，宽度为屏幕宽度的 180%
- **颜色**：经典七色（红、橙、黄、绿、蓝、靛、紫），由外到内
- **透明度**：60% 透明度，确保不会遮挡建筑和其他元素
- **层级**：zIndex 20，显示在天气特效之上，但不遮挡UI元素
- **出现方式**：2秒渐入动画，伴随缩放和向上移动效果

### 与其他效果的协调

- 彩虹会显示在所有天气特效（雨、雪、云、晴天）之上
- 不影响其他交互功能（建筑点击、按钮操作等）
- 可与其他命令（如"不得靠近城堡"）同时生效

## 命令管理

- 命令代码：`CREATE_RAINBOW`
- 权限要求：仅秦小淮可以颁布和取消
- 效果可见性：所有进入星星城的用户都能看到
- 持续时间：直到秦小淮手动取消

## 扩展性

如需添加更多彩虹效果变化（如闪烁、移动等），可以在 `LuckyWheel.css` 中修改 `rainbowAppear` 动画，或添加新的动画效果。

彩虹的颜色、大小、位置等参数都在 `LuckyWheel.jsx` 的彩虹渲染代码中定义，可以根据需要调整。

