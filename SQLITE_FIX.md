# SQLite兼容性问题修复

## 问题描述

在生产环境中出现了SQLite JDBC驱动兼容性错误：

```
java.sql.SQLFeatureNotSupportedException: not implemented by SQLite JDBC driver
at org.sqlite.jdbc3.JDBC3Statement.getGeneratedKeys(JDBC3Statement.java:361)
```

## 问题原因

1. **MyBatis @Options注解问题**：
   - 使用了 `@Options(useGeneratedKeys = true, keyProperty = "id")`
   - SQLite JDBC驱动不支持 `getGeneratedKeys()` 方法

2. **复杂子查询问题**：
   - SQLite在DELETE语句中不支持复杂的嵌套子查询
   - 原有的清理历史记录SQL不兼容

## 修复方案

### 1. 移除自动生成键的依赖

**修改前**：
```java
@Insert("INSERT INTO residence_event_history (...) VALUES (...)")
@Options(useGeneratedKeys = true, keyProperty = "id")
int insertEventHistory(ResidenceEventHistory eventHistory);
```

**修改后**：
```java
@Insert("INSERT INTO residence_event_history (...) VALUES (...)")
int insertEventHistory(ResidenceEventHistory eventHistory);
```

### 2. 简化SQL查询语句

**修改前**：
```sql
DELETE FROM residence_event_history WHERE residence = ? AND id NOT IN 
(SELECT id FROM (SELECT id FROM residence_event_history WHERE residence = ? 
ORDER BY created_at DESC LIMIT ?) AS recent_records)
```

**修改后**：
```sql
DELETE FROM residence_event_history WHERE residence = ? AND ROWID NOT IN 
(SELECT ROWID FROM residence_event_history WHERE residence = ? 
ORDER BY created_at DESC LIMIT ?)
```

### 3. 调整日志记录

**修改前**：
```java
logger.debug("记录事件历史成功，居所: {}, 历史ID: {}", residence, history.getId());
```

**修改后**：
```java
int result = residenceEventHistoryMapper.insertEventHistory(history);
logger.debug("记录事件历史成功，居所: {}, 插入结果: {}", residence, result > 0 ? "成功" : "失败");
```

## 影响评估

### ✅ 正面影响
- 解决了SQLite兼容性问题
- 服务可以正常启动和运行
- 事件历史功能可以正常使用

### ⚠️ 需要注意
- 无法获取自动生成的ID（但实际业务中不需要）
- 日志记录方式略有变化
- 依赖SQLite的ROWID特性进行清理

## 验证方法

1. **重启服务**：
   ```bash
   sudo systemctl restart eden-backend
   ```

2. **检查服务状态**：
   ```bash
   sudo systemctl status eden-backend
   ```

3. **查看日志**：
   ```bash
   sudo journalctl -u eden-backend -f
   ```

4. **测试事件历史功能**：
   - 进入星星城界面
   - 点击任意建筑的历史按钮
   - 确认历史记录正常显示

## 部署说明

修复后需要：
1. 重新编译后端代码
2. 重启eden-backend服务
3. 验证功能正常

## SQLite最佳实践

为避免类似问题，在使用SQLite时应该：

1. **避免使用 `useGeneratedKeys`**
2. **简化复杂查询**，特别是DELETE语句中的子查询
3. **使用ROWID**进行行级操作
4. **测试SQL语句**在SQLite中的兼容性

## 相关文件

- `ResidenceEventHistoryMapper.java` - 修复了注解和SQL语句
- `ResidenceEventService.java` - 调整了日志记录方式
