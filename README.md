# 🎪 Eden 欢乐抽奖系统

一个可爱的移动端抽奖应用，前端采用React，后端采用Java Spring Boot + SQLite轻量级数据库。

## 📱 特色功能

- 🎨 **可爱设计**: 专为移动端优化的可爱界面
- 🎯 **转盘抽奖**: 炫酷的转盘动画效果
- 🎁 **丰富奖品**: 7种不同级别的奖品
- 📊 **概率控制**: 后端控制的奖品概率配置
- 💾 **数据持久化**: SQLite轻量级数据库存储
- 💫 **动画效果**: 流畅的CSS动画和转场效果
- 📱 **响应式设计**: 完美适配各种手机屏幕尺寸
- 👥 **用户系统**: 每用户独立的抽奖次数管理
- ⏰ **定时刷新**: 每日自动恢复用户抽奖机会
- 🔄 **再转一次**: 特殊奖品可获得额外抽奖机会
- 🛠 **管理后台**: 完整的Web管理界面

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

#### 方式1：使用启动脚本（推荐）
```bash
# 进入后端目录
cd eden-server

# Linux/Mac
./start.sh

# Windows
start.bat
```

#### 方式2：使用Maven命令
```bash
# 进入后端目录
cd eden-server

# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```

后端API服务将在 `http://localhost:5000` 启动

### 环境要求

**前端**
- Node.js 16+ 
- npm 或 yarn

**后端**
- Java 17 或更高版本
- Maven 3.6 或更高版本

## 🎮 使用方法

### 普通用户
1. 先启动Java后端服务（`http://localhost:5000`）
2. 再启动前端服务（`http://localhost:3000`）
3. 打开浏览器访问前端地址
4. 输入您的姓名开始游戏
5. 点击"🎲 转动命运"按钮开始抽奖
6. 观看转盘旋转动画
7. 查看抽奖结果弹窗
8. 转盘中心显示剩余抽奖次数

### 管理员
1. 启动后端服务
2. 访问 `http://localhost:5000/admin.html`
3. 使用 `admin/admin2008` 登录
4. 管理用户、查看抽奖历史、调整抽奖次数

## 📚 API 接口

### 健康检查
```
GET /api/health
```

### 获取奖品列表
```
GET /api/prizes
```

### 执行抽奖
```
POST /api/lottery
Content-Type: application/json

{
  "userId": "optional_user_id"
}
```

### 获取抽奖记录
```
GET /api/records/{userId}
```

### 获取统计信息
```
GET /api/stats
```

## 🛠 管理后台

系统提供了完整的管理后台功能，管理员可以通过Web界面管理用户和查看系统数据。

### 访问管理后台

启动后端服务后，访问：`http://localhost:5000/admin.html`

### 管理员登录

- **用户名**: `admin`
- **密码**: `admin2008`

### 管理功能

1. **📊 系统统计**
   - 总用户数
   - 活跃用户数
   - 总抽奖次数

2. **👥 用户管理**
   - 查看所有用户信息
   - 实时显示用户剩余抽奖次数
   - 查看用户创建时间和最后刷新时间

3. **📋 抽奖历史**
   - 查看所有用户的抽奖记录
   - 显示奖品信息和IP地址
   - 按时间排序显示

4. **⚡ 用户操作**
   - **添加用户**: 创建新用户并设置每日抽奖次数
   - **增加次数**: 给指定用户增加当前剩余抽奖次数
   - **设置每日次数**: 修改用户的每日刷新抽奖次数

### 管理后台API

```bash
# 管理员登录
POST /api/admin/login
{
  "username": "admin",
  "password": "admin2008"
}

# 获取用户列表
GET /api/admin/users
Header: Authorization: Bearer {token}

# 获取抽奖历史
GET /api/admin/lottery-history
Header: Authorization: Bearer {token}

# 添加用户
POST /api/admin/users/add
Header: Authorization: Bearer {token}
{
  "userId": "用户名",
  "dailyDraws": 3
}

# 增加用户抽奖次数
POST /api/admin/users/add-draws
Header: Authorization: Bearer {token}
{
  "userId": "用户名",
  "remainingDraws": 5
}

# 设置用户每日抽奖次数
POST /api/admin/users/set-daily-draws
Header: Authorization: Bearer {token}
{
  "userId": "用户名",
  "dailyDraws": 5
}
```

## 🎁 奖品配置

系统包含7种奖品（概率在后端配置）：

| 奖品 | 级别 | 概率 |
|------|------|------|
| 🍰 吃的～ | Common | 15% |
| 🥤 喝的～ | Common | 20% |
| ❤️ 爱 | Epic | 1% |
| 💸 空空如也 | Common | 25% |
| 🧧 红包 | Uncommon | 10% |
| 🔄 再转一次 | Special | 25% |
| 🎁 随机礼物 | Rare | 4% |

## 🛠 技术栈

### 前端
- **React 18** - 现代化React框架
- **Vite** - 快速构建工具
- **Lucky Canvas** - 转盘组件库
- **CSS3** - 原生CSS动画和响应式设计

### 后端
- **Spring Boot 3.2** - Java Web框架
- **MyBatis** - 数据访问层
- **SQLite** - 轻量级数据库
- **Maven** - 依赖管理工具
- **Spring Scheduling** - 定时任务

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

## 🗄️ 数据存储

使用SQLite轻量级数据库，包含以下数据表：

- **prizes**: 奖品信息表
- **lottery_records**: 抽奖记录表

数据库文件：`eden_lottery.db`（自动创建）

## 📁 项目结构

```
eden/
├── eden-web/                    # React前端
│   ├── src/
│   │   ├── components/
│   │   │   ├── LuckyWheel.jsx   # 转盘组件
│   │   │   └── LuckyWheel.css   # 转盘样式
│   │   ├── App.jsx              # 主应用
│   │   └── main.jsx             # 入口文件
│   ├── package.json
│   └── vite.config.js
├── eden-server/                 # Java后端
│   ├── src/main/java/com/eden/lottery/
│   │   ├── controller/          # 控制器层
│   │   ├── service/             # 服务层
│   │   ├── repository/          # 数据访问层
│   │   ├── entity/              # 实体类
│   │   └── dto/                 # 数据传输对象
│   ├── pom.xml                  # Maven配置
│   ├── start.sh                 # Linux/Mac启动脚本
│   └── start.bat                # Windows启动脚本
└── README.md                    # 项目说明
```

## 🔧 开发说明

### 修改奖品概率

编辑 `eden-server/src/main/resources/application.yml` 文件中的概率配置：

```yaml
app:
  lottery:
    prizes:
      - name: "🍰 吃的～"
        probability: 0.15  # 修改这里的概率值
        level: "common"
```

### 自定义奖品

1. 修改后端配置文件中的奖品列表
2. 更新前端 `LuckyWheel.jsx` 中的转盘奖品显示
3. 确保前后端奖品名称一致

## 📄 许可证

MIT License

---

<div align="center">
🎪 享受抽奖的快乐时光！🎪
</div>