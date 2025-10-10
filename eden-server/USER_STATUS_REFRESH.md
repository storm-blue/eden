# 用户状态自动刷新功能

## 📝 功能说明

系统会每30分钟自动刷新所有用户的状态。状态的决定逻辑由 `UserStatusService.determineUserStatus()` 方法提供，可以根据用户名和当前居所来灵活定制。

## 🔧 核心组件

### 1. UserStatusRefreshTask（定时任务）

**位置**: `eden-server/src/main/java/com/eden/lottery/task/UserStatusRefreshTask.java`

**执行时间**: 每30分钟（每小时的0分和30分）

**执行流程**:
1. 获取所有用户列表
2. 遍历每个用户
3. 调用 `UserStatusService.determineUserStatus(userId, residence)` 获取新状态
4. 如果返回的状态不为 `null` 且与当前状态不同，则更新数据库
5. 记录更新统计信息

### 2. UserStatusService（状态决定服务）

**位置**: `eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`

**核心方法**: `determineUserStatus(String userId, String residence)`

**参数**:
- `userId`: 用户名（例如：李星斗、秦小淮、存子等）
- `residence`: 当前居所代码（例如：castle、park、city_hall等）

**返回值**:
- 返回新的用户状态字符串：系统会将其更新到数据库
- 返回 `null`：表示不修改当前状态

## 💡 实现示例

### 示例 1：根据居所设置状态

```java
public String determineUserStatus(String userId, String residence) {
    if (residence == null) {
        return "流浪中";
    }
    
    switch (residence) {
        case "castle":
            return "在城堡里享受下午茶☕";
        case "park":
            return "在公园散步🌳";
        case "city_hall":
            return "在市政厅办公📋";
        case "palace":
            return "在行宫休息💤";
        case "white_dove_house":
            return "在小白鸽家做客🕊️";
        default:
            return "神秘行踪✨";
    }
}
```

### 示例 2：根据用户名和居所设置个性化状态

```java
public String determineUserStatus(String userId, String residence) {
    // 特殊用户的特殊状态
    if ("李星斗".equals(userId)) {
        if ("castle".equals(residence)) {
            return "在城堡处理政务👑";
        } else if ("city_hall".equals(residence)) {
            return "视察市政厅🏛️";
        }
    }
    
    if ("秦小淮".equals(userId)) {
        if ("castle".equals(residence)) {
            return "陪伴李星斗💕";
        } else if ("park".equals(residence)) {
            return "在公园画画🎨";
        }
    }
    
    if ("存子".equals(userId)) {
        return "忠诚守护中🛡️";
    }
    
    // 默认状态
    return null; // 不修改状态
}
```

### 示例 3：根据时间段设置状态

```java
public String determineUserStatus(String userId, String residence) {
    LocalDateTime now = LocalDateTime.now();
    int hour = now.getHour();
    
    // 夜间（22:00 - 6:00）
    if (hour >= 22 || hour < 6) {
        return "睡觉中💤";
    }
    
    // 早晨（6:00 - 9:00）
    if (hour >= 6 && hour < 9) {
        return "享用早餐🥐";
    }
    
    // 中午（11:00 - 13:00）
    if (hour >= 11 && hour < 13) {
        return "午餐时间🍽️";
    }
    
    // 下午茶时间（15:00 - 17:00）
    if (hour >= 15 && hour < 17) {
        return "下午茶时间☕";
    }
    
    // 晚餐时间（18:00 - 20:00）
    if (hour >= 18 && hour < 20) {
        return "晚餐时间🍷";
    }
    
    // 其他时间保持原状态
    return null;
}
```

### 示例 4：随机生成状态

```java
private final Random random = new Random();

private final String[] ACTIVITIES = {
    "阅读中📚", "听音乐中🎵", "喝咖啡☕", "散步中🚶",
    "思考中💭", "工作中💼", "放松中😌", "聊天中💬"
};

public String determineUserStatus(String userId, String residence) {
    // 20% 的概率更新状态
    if (random.nextDouble() < 0.2) {
        return ACTIVITIES[random.nextInt(ACTIVITIES.length)];
    }
    
    // 80% 的概率保持原状态
    return null;
}
```

### 示例 5：综合逻辑

```java
private final Random random = new Random();

public String determineUserStatus(String userId, String residence) {
    LocalDateTime now = LocalDateTime.now();
    int hour = now.getHour();
    
    // 1. 特殊用户在特定居所的固定状态
    if ("李星斗".equals(userId) && "castle".equals(residence)) {
        return hour < 12 ? "处理政务👑" : "批阅文件📜";
    }
    
    // 2. 夜间统一睡觉
    if (hour >= 23 || hour < 6) {
        return "睡觉中💤";
    }
    
    // 3. 根据居所和时间段
    if ("park".equals(residence)) {
        if (hour >= 6 && hour < 9) {
            return "晨练中🏃";
        } else if (hour >= 15 && hour < 18) {
            return "在公园散步🌳";
        }
    }
    
    // 4. 30% 概率随机更新状态
    if (random.nextDouble() < 0.3) {
        String[] statusList = getStatusByResidence(residence);
        return statusList[random.nextInt(statusList.length)];
    }
    
    // 5. 其他情况保持原状态
    return null;
}

private String[] getStatusByResidence(String residence) {
    switch (residence) {
        case "castle":
            return new String[]{"在城堡休息💤", "享用下午茶☕", "欣赏风景👀"};
        case "park":
            return new String[]{"在公园闲逛🌳", "喂鸟🦜", "野餐🧺"};
        case "city_hall":
            return new String[]{"处理公务📋", "开会中💼", "审阅文件📄"};
        default:
            return new String[]{"忙碌中💫", "休息中😌", "思考中💭"};
    }
}
```

## 🔍 日志说明

### 正常日志

```
2025-10-10 12:00:00 - 开始执行用户状态刷新任务...
2025-10-10 12:00:01 - 用户状态刷新任务执行完成 - 共检查 5 个用户，更新 3 个用户状态
```

### 调试日志（DEBUG级别）

```
2025-10-10 12:00:00 - 决定用户状态 - 用户: 李星斗, 居所: castle
2025-10-10 12:00:00 - 用户 李星斗 的状态已更新为: 在城堡处理政务👑
```

### 错误日志

```
2025-10-10 12:00:00 - 更新用户 李星斗 的状态失败
java.lang.Exception: ...
```

## ⚙️ 配置说明

### 修改执行时间

在 `UserStatusRefreshTask.java` 中修改 `@Scheduled` 注解的 cron 表达式：

```java
// 当前：每30分钟（每小时的0分和30分）
@Scheduled(cron = "0 0,30 * * * ?")

// 改为每15分钟（每小时的0、15、30、45分）
@Scheduled(cron = "0 0,15,30,45 * * * ?")

// 改为每小时（每小时的0分）
@Scheduled(cron = "0 0 * * * ?")

// 改为每10分钟
@Scheduled(cron = "0 */10 * * * ?")
```

### Cron 表达式格式

```
秒 分 时 日 月 星期
0  0  *  *  *   ?     → 每小时
0  30 *  *  *   ?     → 每小时的30分
0  0,30 * * * ?       → 每小时的0分和30分
0  */15 * * * ?       → 每15分钟
```

## 🧪 测试建议

### 1. 手动触发测试

可以创建一个测试接口来手动触发状态刷新：

```java
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private UserStatusRefreshTask userStatusRefreshTask;
    
    @PostMapping("/refresh-status")
    public String testRefreshStatus() {
        userStatusRefreshTask.refreshUserStatus();
        return "状态刷新任务已手动触发";
    }
}
```

### 2. 观察日志

启用 DEBUG 级别日志来查看详细的状态更新过程：

```yaml
# application.yml
logging:
  level:
    com.eden.lottery.task.UserStatusRefreshTask: DEBUG
    com.eden.lottery.service.UserStatusService: DEBUG
```

### 3. 数据库验证

查询用户表验证状态是否正确更新：

```sql
SELECT user_id, status, residence, update_time 
FROM users 
ORDER BY update_time DESC;
```

## 📌 注意事项

1. **返回 null 的含义**: 如果 `determineUserStatus()` 返回 `null`，系统不会修改该用户的状态
2. **性能考虑**: 如果用户数量很大，建议在状态决定逻辑中避免复杂的计算或外部API调用
3. **事务处理**: 任务已添加 `@Transactional` 注解，单个用户更新失败不会影响其他用户
4. **状态长度**: 建议状态文本不要超过数据库字段限制（通常为255字符）
5. **并发问题**: 定时任务不会与其他用户状态更新操作冲突，数据库层面有锁保护

## 🚀 快速开始

1. 打开 `UserStatusService.java`
2. 找到 `determineUserStatus()` 方法
3. 删除或修改 `// TODO` 注释
4. 实现你的状态决定逻辑
5. 重启后端服务
6. 等待下一个30分钟时间点（或手动触发）观察效果

## 📦 相关文件

- 定时任务：`eden-server/src/main/java/com/eden/lottery/task/UserStatusRefreshTask.java`
- 服务类：`eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`
- 数据访问：`eden-server/src/main/java/com/eden/lottery/mapper/UserMapper.java`
- 用户实体：`eden-server/src/main/java/com/eden/lottery/entity/User.java`

