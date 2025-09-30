# 🎪 Eden 欢乐抽奖系统

一个可爱的移动端抽奖应用，包含前端展示页面和后端API服务。

## 📱 特色功能

- 🎨 **可爱设计**: 专为移动端优化的可爱界面
- 🎯 **转盘抽奖**: 炫酷的转盘动画效果
- 🎁 **丰富奖品**: 7种不同级别的奖品
- 📊 **概率控制**: 后端可配置的奖品概率
- 💫 **动画效果**: 流畅的CSS动画和转场效果
- 📱 **响应式设计**: 完美适配各种手机屏幕尺寸

## 🚀 快速开始

### 前端启动

```bash
# 进入前端目录
cd eden-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

### 后端启动

```bash
# 进入后端目录
cd eden-server

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

后端API服务将在 `http://localhost:5000` 启动

## 🎮 使用方法

1. 打开浏览器访问 `http://localhost:3000`
2. 点击"🎲 开始抽奖"按钮
3. 观看转盘旋转动画
4. 查看抽奖结果弹窗
5. 可以重置转盘继续游戏

## 📚 API 接口

### 获取奖品列表
```
GET /api/prizes
```

### 执行抽奖
```
POST /api/lottery
Body: { "userId": "optional_user_id" }
```

### 获取抽奖记录
```
GET /api/records/:userId?
```

### 获取统计信息
```
GET /api/stats
```

### 健康检查
```
GET /api/health
```

## 🎁 奖品配置

系统包含7种奖品：

| 奖品 | 级别 | 概率 |
|------|------|------|
| 🎁 大奖 | Epic | 5% |
| 🎮 游戏币 | Rare | 15% |
| 🎪 再来一次 | Common | 20% |
| 🎈 小奖品 | Common | 25% |
| 🍭 糖果 | Common | 15% |
| 🎨 贴纸 | Uncommon | 10% |
| 🌟 积分 | Uncommon | 10% |

## 🛠 技术栈

### 前端
- **React 18** - 现代化React框架
- **Vite** - 快速构建工具
- **CSS3** - 原生CSS动画和响应式设计

### 后端
- **Node.js** - 服务器运行环境
- **Express.js** - Web应用框架
- **CORS** - 跨域资源共享

## 📱 移动端优化

- 触摸友好的按钮设计
- 响应式布局适配各种屏幕尺寸
- 优化的动画性能
- 防止页面缩放和误触
- 适配iPhone、Android等主流设备

## 🎨 设计特色

- 渐变色彩搭配
- 可爱的emoji图标
- 流畅的转场动画
- 弹性交互效果
- 浮动装饰元素

## 📄 许可证

MIT License

---

<div align="center">
🎪 享受抽奖的快乐时光！🎪
</div>