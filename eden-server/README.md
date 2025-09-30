# 🎪 Eden抽奖系统 - Java后端

基于Spring Boot + SQLite的轻量级抽奖系统后端服务。

## 🚀 快速启动

### 环境要求
- Java 17 或更高版本
- Maven 3.6 或更高版本

### 启动命令

```bash
# 进入Java后端目录
cd eden-server-java

# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```

服务将在 `http://localhost:5000` 启动

### 打包运行

```bash
# 打包成jar
mvn clean package -DskipTests

# 运行jar包
java -jar target/eden-lottery-server-1.0.0.jar
```

## 📚 API接口

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

## 🗄️ 数据库

使用SQLite轻量级数据库，自动创建 `eden_lottery.db` 文件。

### 数据表结构

**prizes 奖品表**
- id: 主键
- name: 奖品名称
- probability: 中奖概率
- level: 奖品级别
- created_at: 创建时间
- updated_at: 更新时间

**lottery_records 抽奖记录表**
- id: 主键
- user_id: 用户ID
- prize_id: 奖品ID（外键）
- ip_address: 客户端IP
- user_agent: 客户端信息
- created_at: 抽奖时间

## 🎁 奖品配置

默认奖品及概率：

| 奖品 | 概率 | 级别 |
|------|------|------|
| 🍰 吃的～ | 15% | common |
| 🥤 喝的～ | 20% | common |
| ❤️ 爱 | 1% | epic |
| 💸 空空如也 | 25% | common |
| 🧧 红包 | 10% | uncommon |
| 🔄 再转一次 | 25% | special |
| 🎁 随机礼物 | 4% | rare |

## 🛠 技术栈

- **Spring Boot 3.2** - 主框架
- **Spring Data JPA** - 数据访问层
- **SQLite** - 轻量级数据库
- **Maven** - 依赖管理
- **Logback** - 日志框架

## 📁 项目结构

```
src/main/java/com/eden/lottery/
├── EdenLotteryApplication.java     # 启动类
├── controller/                     # 控制器层
│   └── LotteryController.java
├── service/                        # 服务层
│   ├── LotteryService.java
│   └── PrizeInitService.java
├── repository/                     # 数据访问层
│   ├── PrizeRepository.java
│   └── LotteryRecordRepository.java
├── entity/                         # 实体类
│   ├── Prize.java
│   └── LotteryRecord.java
└── dto/                           # 数据传输对象
    ├── ApiResponse.java
    ├── LotteryRequest.java
    └── LotteryResult.java
```
