# 用户状态自动刷新 - 快速开始

## 🎯 功能概述

系统每30分钟（每小时的0分和30分）自动更新所有用户的状态。

## 📝 实现状态决定逻辑

### 编辑文件

打开文件：`eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`

### 修改方法

找到 `determineUserStatus` 方法：

```java
public String determineUserStatus(String userId, String residence) {
    // 在这里实现你的逻辑
    
    // 返回新状态 → 系统会更新到数据库
    // 返回 null → 不修改当前状态
    return null;
}
```

### 参数说明

- `userId`: 用户名（例如：李星斗、秦小淮、存子等）
- `residence`: 居所代码（castle、park、city_hall、palace、white_dove_house）

### 当前实现

**✅ 已实现为每个用户设置不同的状态列表，随机选择**

每个用户都有自己的专属状态列表（8个状态），系统会每30分钟从列表中随机选择一个：

| 用户 | 状态示例 |
|-----|---------|
| 李星斗 | 处理政务👑、批阅文件📜、视察星星城🏛️、思考城市发展💭... |
| 秦小淮 | 陪伴李星斗💕、在公园画画🎨、阅读书籍📚、欣赏风景👀... |
| 存子 | 忠诚守护中🛡️、巡逻星星城🚶、保护李星斗👮、警戒中👁️... |
| 小白鸽 | 在天空翱翔🕊️、送信中✉️、休息中💤、觅食中🌾... |
| 邓炎博升 | 研究学问📖、思考问题🤔、写作中✍️、喝咖啡☕... |
| 其他用户 | 使用默认状态列表（忙碌中💫、休息中😌、工作中💼...） |

### 添加新用户的状态列表

在 `UserStatusService.java` 的 `static {}` 块中添加：

```java
// 新用户的状态列表
USER_STATUS_MAP.put("用户名", new String[]{
    "状态1",
    "状态2",
    "状态3",
    // ... 更多状态
});
```

## 🧪 测试

### 方法1：运行测试脚本

```bash
# 手动触发并查看前后对比
./trigger-status-refresh.sh
```

### 方法2：使用API直接测试

```bash
# 1. 登录
TOKEN=$(curl -s -X POST "http://localhost:8080/api/admin/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}' | jq -r '.data.token')

# 2. 手动触发刷新
curl -X POST "http://localhost:8080/api/admin/trigger-status-refresh" \
    -H "Authorization: Bearer $TOKEN" | jq .

# 3. 查看结果
curl -s "http://localhost:8080/api/admin/users" | jq -r '.data[] | "\(.userId): \(.status)"'
```

### 方法3：等待自动执行

定时任务会在每小时的0分和30分自动执行。查看日志：

```bash
# 实时监控日志
tail -f eden-server/logs/*.log | grep "用户状态刷新"
```

## ⏰ 修改执行时间

编辑文件：`eden-server/src/main/java/com/eden/lottery/task/UserStatusRefreshTask.java`

修改 `@Scheduled` 注解：

```java
// 当前：每30分钟（每小时的0分和30分）
@Scheduled(cron = "0 0,30 * * * ?")

// 改为每15分钟
@Scheduled(cron = "0 */15 * * * ?")

// 改为每小时
@Scheduled(cron = "0 0 * * * ?")

// 改为每10分钟
@Scheduled(cron = "0 */10 * * * ?")
```

## 📚 完整文档

详细的实现示例和说明，请查看：
- `eden-server/USER_STATUS_REFRESH.md`

## 🔧 相关文件

| 文件 | 说明 |
|-----|------|
| `service/UserStatusService.java` | **状态决定逻辑**（需要你实现） |
| `task/UserStatusRefreshTask.java` | 定时任务配置 |
| `controller/AdminController.java` | 手动触发接口 |
| `trigger-status-refresh.sh` | 测试脚本 |

