# 用户状态自动刷新 - 功能总结

## 🎯 功能概述

**✅ 已实现完成！**

系统每30分钟自动为所有用户从其专属状态列表中随机选择一个新状态。

## 👥 已配置的用户

| 用户 | 状态数量 | 状态风格 |
|-----|---------|---------|
| 👑 **李星斗** | 8个 | 领导者风格（处理政务、批阅文件、视察城市...） |
| 💕 **秦小淮** | 8个 | 文艺风格（画画、阅读、书法、品茶...） |
| 🛡️ **存子** | 8个 | 守护者风格（守护、巡逻、警戒、训练...） |
| 🕊️ **小白鸽** | 8个 | 自由风格（翱翔、送信、觅食、飞行...） |
| 📖 **邓炎博升** | 8个 | 学者风格（研究、思考、写作、交流...） |
| 🌟 **其他用户** | 8个 | 默认通用状态（忙碌、休息、工作...） |

## ⏰ 运行机制

### 自动执行
- **时间**：每30分钟（每小时的0分和30分）
- **范围**：所有用户
- **方式**：从各自的状态列表中随机选择

### 状态示例

**李星斗可能的状态变化**：
```
12:00 → 处理政务👑
12:30 → 享用下午茶☕
13:00 → 批阅文件📜
13:30 → 与秦小淮谈心💕
```

**秦小淮可能的状态变化**：
```
12:00 → 在公园画画🎨
12:30 → 阅读书籍📚
13:00 → 陪伴李星斗💕
13:30 → 品茶中🍵
```

## 🧪 测试方法

### 方法1：快速测试（推荐）

```bash
cd /Users/g01d-01-0924/eden
./trigger-status-refresh.sh
```

这会：
1. ✅ 显示当前所有用户的状态
2. 🔄 手动触发一次刷新
3. ✅ 显示刷新后的状态（可以看到变化）

### 方法2：等待自动执行

下次自动执行时间：
- 如果现在是 12:15，下次执行是 12:30
- 如果现在是 12:45，下次执行是 13:00

查看日志：
```bash
tail -f eden-server/logs/*.log | grep "用户状态刷新"
```

### 方法3：使用API测试

```bash
# 1. 登录
TOKEN=$(curl -s -X POST "http://localhost:8080/api/admin/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}' | jq -r '.data.token')

# 2. 触发刷新
curl -X POST "http://localhost:8080/api/admin/trigger-status-refresh" \
    -H "Authorization: Bearer $TOKEN" | jq .

# 3. 查看结果
curl -s "http://localhost:8080/api/admin/users" | \
    jq -r '.data[] | "\(.userId): \(.status)"'
```

## ➕ 添加新用户状态

### 步骤

1. 打开文件：`eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`

2. 在 `static {}` 块中添加：

```java
// 新用户的状态列表
USER_STATUS_MAP.put("新用户名", new String[]{
    "状态1🌟",
    "状态2💫",
    "状态3✨",
    "状态4🎯",
    "状态5💡",
    "状态6🎨",
    "状态7📚",
    "状态8🎵"
});
```

3. 重启服务：
```bash
cd eden-server
./start.sh  # 或 start.bat
```

## 📊 当前状态列表详情

### 李星斗的8个状态
```
1. 处理政务👑
2. 批阅文件📜
3. 视察星星城🏛️
4. 思考城市发展💭
5. 享用下午茶☕
6. 在城堡休息💤
7. 与秦小淮谈心💕
8. 规划未来✨
```

### 秦小淮的8个状态
```
1. 陪伴李星斗💕
2. 在公园画画🎨
3. 阅读书籍📚
4. 欣赏风景👀
5. 练习书法✍️
6. 听音乐中🎵
7. 品茶中🍵
8. 散步中🚶
```

### 存子的8个状态
```
1. 忠诚守护中🛡️
2. 巡逻星星城🚶
3. 保护李星斗👮
4. 警戒中👁️
5. 值勤中💼
6. 训练中🏃
7. 休息片刻😌
8. 观察四周🔍
```

### 小白鸽的8个状态
```
1. 在天空翱翔🕊️
2. 送信中✉️
3. 休息中💤
4. 觅食中🌾
5. 梳理羽毛✨
6. 欣赏风景🌅
7. 与朋友玩耍🎈
8. 飞行训练🪽
```

### 邓炎博升的8个状态
```
1. 研究学问📖
2. 思考问题🤔
3. 写作中✍️
4. 喝咖啡☕
5. 散步思考🚶
6. 与人交流💬
7. 整理笔记📝
8. 休息放松😌
```

## 📚 相关文档

| 文档 | 说明 |
|-----|------|
| **USER_STATUS_LIST.md** | 详细的状态列表和配置指南 |
| **USER_STATUS_REFRESH_QUICK_START.md** | 快速开始指南 |
| **USER_STATUS_REFRESH.md** | 完整的功能文档和示例 |

## 🔧 核心代码文件

| 文件 | 说明 |
|-----|------|
| `service/UserStatusService.java` | **状态列表配置** |
| `task/UserStatusRefreshTask.java` | 定时任务 |
| `controller/AdminController.java` | 手动触发接口 |
| `trigger-status-refresh.sh` | 测试脚本 |

## 🎨 自定义建议

### 1. 调整状态文本

可以修改现有状态文本，让它们更符合角色性格：

```java
USER_STATUS_MAP.put("李星斗", new String[]{
    "处理紧急政务👑",  // 改为更具体的描述
    "召开重要会议📋",  // 添加更多场景
    // ...
});
```

### 2. 调整刷新频率

修改 `UserStatusRefreshTask.java`：

```java
// 当前：每30分钟
@Scheduled(cron = "0 0,30 * * * ?")

// 改为每15分钟
@Scheduled(cron = "0 */15 * * * ?")

// 改为每小时
@Scheduled(cron = "0 0 * * * ?")
```

### 3. 添加时间相关逻辑

可以在 `determineUserStatus()` 方法中添加时间判断：

```java
public String determineUserStatus(String userId, String residence) {
    LocalDateTime now = LocalDateTime.now();
    int hour = now.getHour();
    
    // 夜间特殊状态
    if (hour >= 22 || hour < 6) {
        return "睡觉中💤";
    }
    
    // 否则从列表中随机选择
    String[] statusList = USER_STATUS_MAP.getOrDefault(userId, USER_STATUS_MAP.get("default"));
    return statusList[random.nextInt(statusList.length)];
}
```

## ✅ 验证清单

- [x] 创建 `UserStatusService.java` 服务类
- [x] 创建 `UserStatusRefreshTask.java` 定时任务
- [x] 为5个用户配置专属状态列表
- [x] 添加默认状态列表
- [x] 在 `AdminController` 添加手动触发接口
- [x] 创建测试脚本 `trigger-status-refresh.sh`
- [x] 编写完整文档

## 🚀 立即开始

1. **重启后端服务**（如果还没重启）
2. **运行测试脚本**：`./trigger-status-refresh.sh`
3. **观察效果**：每30分钟用户状态会自动变化
4. **自定义**：修改 `UserStatusService.java` 添加更多用户或状态

---

**状态自动刷新系统已完全就绪！🎉**

