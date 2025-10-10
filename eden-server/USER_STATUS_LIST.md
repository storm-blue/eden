# 用户状态列表配置

## 📋 当前配置的用户

### 👑 李星斗
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

### 💕 秦小淮
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

### 🛡️ 存子
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

### 🕊️ 小白鸽
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

### 📖 邓炎博升
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

### 🌟 其他用户（默认列表）
```
1. 忙碌中💫
2. 休息中😌
3. 工作中💼
4. 思考中💭
5. 散步中🚶
6. 阅读中📚
7. 放松中🎵
8. 聊天中💬
```

## ➕ 添加新用户状态列表

### 步骤 1：打开配置文件

编辑文件：`eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`

### 步骤 2：在 static 块中添加

找到 `static {}` 块，在里面添加新用户的配置：

```java
static {
    // ... 现有配置
    
    // 新用户的状态列表
    USER_STATUS_MAP.put("新用户名", new String[]{
        "状态1",
        "状态2",
        "状态3",
        "状态4",
        "状态5",
        "状态6",
        "状态7",
        "状态8"
    });
}
```

### 步骤 3：重启服务

```bash
cd eden-server
./start.sh  # 或 start.bat (Windows)
```

### 步骤 4：测试

```bash
./trigger-status-refresh.sh
```

## ✏️ 修改现有用户的状态列表

### 方法 1：直接修改代码

1. 打开 `UserStatusService.java`
2. 找到对应用户的 `USER_STATUS_MAP.put()` 语句
3. 修改数组中的状态文本
4. 重启服务

### 方法 2：动态添加（运行时）

可以通过调用 `addUserStatusList()` 方法动态修改：

```java
// 在其他服务或控制器中注入 UserStatusService
@Autowired
private UserStatusService userStatusService;

// 动态修改用户状态列表
userStatusService.addUserStatusList("李星斗", new String[]{
    "新状态1",
    "新状态2",
    // ...
});
```

**注意**：方法2修改的状态在服务重启后会丢失，需要重新执行。

## 💡 状态设计建议

### 1. 状态数量
- 建议每个用户 **6-10** 个状态
- 太少会显得单调
- 太多会分散，每个状态出现频率过低

### 2. 状态风格
- 使用表情符号 emoji 增加视觉效果
- 保持角色性格一致性
- 状态应该是动态的，描述当前在做什么

### 3. 状态示例

**积极向上型**：
```
工作中💼、学习中📚、运动中🏃、思考中💭
```

**生活化型**：
```
喝咖啡☕、散步中🚶、听音乐🎵、看书📖
```

**角色特定型**：
```
处理政务👑、守护中🛡️、送信中✉️、画画中🎨
```

**时间相关型**：
```
享用早餐🥐、午休中💤、下午茶☕、晚餐时间🍷
```

## 🔄 状态更新机制

### 自动更新
- **频率**：每30分钟（每小时的0分和30分）
- **方式**：从用户的状态列表中随机选择
- **范围**：所有用户

### 手动触发
```bash
# 使用测试脚本
./trigger-status-refresh.sh

# 或使用API
curl -X POST "http://localhost:8080/api/admin/trigger-status-refresh" \
    -H "Authorization: Bearer YOUR_TOKEN"
```

## 📊 状态统计

可以通过日志查看状态更新情况：

```bash
# 查看刷新日志
tail -f eden-server/logs/*.log | grep "用户状态刷新"

# 查看具体用户状态变化（需要DEBUG级别）
tail -f eden-server/logs/*.log | grep "为用户.*选择状态"
```

启用DEBUG日志：

```yaml
# application.yml
logging:
  level:
    com.eden.lottery.service.UserStatusService: DEBUG
```

## 🎯 完整示例

为新用户"测试用户"添加个性化状态：

```java
static {
    // ... 其他用户配置
    
    // 测试用户 - 科技爱好者
    USER_STATUS_MAP.put("测试用户", new String[]{
        "编写代码💻",
        "调试程序🐛",
        "阅读技术文档📄",
        "参加会议👥",
        "喝咖啡提神☕",
        "思考架构设计🏗️",
        "代码审查👀",
        "休息放松🎮"
    });
}
```

重启服务后，"测试用户"的状态将每30分钟从上述8个状态中随机选择一个。

## 📚 相关文件

- **状态配置**：`eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java`
- **定时任务**：`eden-server/src/main/java/com/eden/lottery/task/UserStatusRefreshTask.java`
- **测试脚本**：`trigger-status-refresh.sh`
- **快速开始**：`USER_STATUS_REFRESH_QUICK_START.md`
- **完整文档**：`USER_STATUS_REFRESH.md`

