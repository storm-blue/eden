# 用户漫游系统说明

## 系统概述

用户漫游系统是一个自动化功能，每隔2小时会检查所有用户的当前居住地，并根据自定义逻辑决定是否将用户移动到新的居所。

## 主要功能

### 1. 自动定时执行
- **执行频率**: 每2小时的整点执行（如：00:00, 02:00, 04:00...）
- **执行内容**: 遍历所有用户，调用自定义漫游逻辑决定是否移动

### 2. 自定义漫游逻辑
漫游逻辑在 `UserRoamingLogicService.java` 的 `determineNewResidence` 方法中实现：

```java
public String determineNewResidence(String username, String currentResidence) {
    // 在这里实现你的漫游逻辑
    // 
    // 参数说明：
    // - username: 用户名
    // - currentResidence: 当前居所 (castle/park/city_hall/white_dove_house/palace)
    //
    // 返回值：
    // - 返回新居所名称表示需要移动
    // - 返回null或currentResidence表示不移动
    
    return null; // 当前默认不移动任何用户
}
```

### 3. 可用居所列表
- `castle` - 城堡🏰
- `park` - 公园🌳  
- `city_hall` - 市政厅🏛️
- `white_dove_house` - 小白鸽家🕊️
- `palace` - 行宫🏯

## API接口

### 1. 手动触发漫游
```
POST /api/user-roaming/trigger
```
用于测试和手动执行漫游系统。

### 2. 获取漫游状态
```
GET /api/user-roaming/status
```
获取漫游系统的运行状态和统计信息。

## 前端功能

### 1. 手动触发按钮
在星星城页面左上角有一个🚶按钮，点击可以手动触发漫游系统（用于测试）。

### 2. 漫游通知
当漫游系统执行后，会在页面右上角显示通知提示。

## 实现自定义漫游逻辑示例

### 示例1：随机漫游
```java
public String determineNewResidence(String username, String currentResidence) {
    // 30%概率移动用户
    if (Math.random() < 0.3) {
        String[] buildings = {"castle", "park", "city_hall", "white_dove_house", "palace"};
        String newBuilding;
        do {
            newBuilding = buildings[(int)(Math.random() * buildings.length)];
        } while (newBuilding.equals(currentResidence)); // 确保不是当前居所
        
        return newBuilding;
    }
    return null; // 不移动
}
```

### 示例2：基于用户名的规则
```java
public String determineNewResidence(String username, String currentResidence) {
    // 特定用户有特定的移动规则
    if (username.contains("admin")) {
        return "castle"; // 管理员总是住城堡
    }
    
    if (username.startsWith("test")) {
        return "park"; // 测试用户总是住公园
    }
    
    // 其他用户随机移动
    if (Math.random() < 0.2) {
        String[] buildings = {"city_hall", "white_dove_house", "palace"};
        return buildings[(int)(Math.random() * buildings.length)];
    }
    
    return null; // 不移动
}
```

### 示例3：基于当前居所的流动
```java
public String determineNewResidence(String username, String currentResidence) {
    // 基于当前居所决定下一个可能的居所
    Map<String, String[]> moveOptions = Map.of(
        "castle", new String[]{"palace", "city_hall"},
        "palace", new String[]{"castle", "white_dove_house"},
        "city_hall", new String[]{"park", "castle"},
        "park", new String[]{"white_dove_house", "city_hall"},
        "white_dove_house", new String[]{"park", "palace"}
    );
    
    if (Math.random() < 0.25) { // 25%概率移动
        String[] options = moveOptions.get(currentResidence);
        if (options != null && options.length > 0) {
            return options[(int)(Math.random() * options.length)];
        }
    }
    
    return null; // 不移动
}
```

## 注意事项

1. **数据库事务**: 用户移动操作会自动包装在数据库事务中，确保数据一致性。

2. **错误处理**: 系统会自动捕获和记录错误，单个用户的移动失败不会影响其他用户。

3. **日志记录**: 所有漫游活动都会记录在日志中，便于调试和监控。

4. **性能考虑**: 系统会批量处理所有用户，但建议漫游逻辑保持简单高效。

5. **居所事件**: 用户移动后，相关居所的事件会自动刷新。

## 启用系统

系统默认已启用，定时任务会自动运行。如需修改执行频率，可以修改 `UserRoamingService.java` 中的 `@Scheduled` 注解：

```java
@Scheduled(cron = "0 0 0/2 * * ?") // 每2小时执行
```

cron表达式格式：`秒 分 时 日 月 周`
