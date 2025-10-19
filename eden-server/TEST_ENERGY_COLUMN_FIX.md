# ✅ Energy Refresh Time 列添加修复

## 🐛 问题描述

启动服务器时出现以下错误：

```
Caused by: org.sqlite.SQLiteException: [SQLITE_ERROR] SQL error or missing database 
(Cannot add a column with non-constant default)
```

**错误原因：** SQLite 不支持在 `ALTER TABLE ADD COLUMN` 时使用非常量的默认值（如函数调用）

## ❌ 错误的代码

```java
private void addEnergyRefreshTimeColumn(Connection connection) throws Exception {
    // ❌ 错误：使用了函数作为默认值
    String sql = "ALTER TABLE users ADD COLUMN energy_refresh_time DATETIME DEFAULT (datetime('now', 'localtime'))";
    
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(sql);
        // ...
    }
}
```

**SQLite 限制：**
- ✅ 允许：常量默认值（如 `DEFAULT 0`, `DEFAULT 'value'`, `DEFAULT NULL`）
- ❌ 不允许：函数默认值（如 `DEFAULT datetime('now')`, `DEFAULT CURRENT_TIMESTAMP`）

## ✅ 修复后的代码

```java
private void addEnergyRefreshTimeColumn(Connection connection) throws Exception {
    // ✅ 正确：先添加列（不设置默认值），然后用UPDATE设置值
    String sql = "ALTER TABLE users ADD COLUMN energy_refresh_time DATETIME";
    
    try (Statement stmt = connection.createStatement()) {
        // 步骤1：添加列（允许NULL）
        stmt.execute(sql);
        logger.info("energy_refresh_time列添加成功");
        
        // 步骤2：为所有现有用户设置默认值
        String updateSql = "UPDATE users SET energy_refresh_time = datetime('now', 'localtime') WHERE energy_refresh_time IS NULL";
        stmt.execute(updateSql);
        logger.info("为现有用户设置默认精力刷新时间");
    }
}
```

## 🔍 修复原理

### 两步法迁移策略

1. **添加列（无默认值）**
   ```sql
   ALTER TABLE users ADD COLUMN energy_refresh_time DATETIME;
   ```
   - 新列允许 `NULL`
   - 所有现有记录的该列值为 `NULL`

2. **填充默认值**
   ```sql
   UPDATE users 
   SET energy_refresh_time = datetime('now', 'localtime') 
   WHERE energy_refresh_time IS NULL;
   ```
   - 为所有 `NULL` 值的记录设置当前时间
   - 可以使用函数，因为这是在 `UPDATE` 中，不是在 `ALTER TABLE` 中

## 📋 验证步骤

### 方式1：启动服务器
```bash
cd /Users/g01d-01-0924/eden/eden-server
mvn spring-boot:run
```

**预期输出：**
```
✅ energy_refresh_time列添加成功
✅ 为现有用户设置默认精力刷新时间
✅ 数据库迁移完成
✅ Started EdenLotteryApplication in X.XXX seconds
```

### 方式2：直接检查数据库
```bash
cd /Users/g01d-01-0924/eden/eden-server
sqlite3 eden_lottery.db

-- 检查列是否存在
.schema users

-- 检查数据
SELECT user_id, energy, max_energy, energy_refresh_time 
FROM users 
LIMIT 5;
```

**预期结果：**
```
user_id     energy  max_energy  energy_refresh_time
----------  ------  ----------  -------------------
秦小淮      15      15          2024-10-19 16:45:23
李星斗      15      15          2024-10-19 16:45:23
...
```

## 🔄 回滚方案（如果需要）

如果需要回滚这个列：

```sql
-- SQLite 不直接支持 DROP COLUMN（需要旧版本）
-- 需要重建表的方式：

-- 1. 创建备份表
CREATE TABLE users_backup AS SELECT * FROM users;

-- 2. 删除原表
DROP TABLE users;

-- 3. 重新创建表（不包含 energy_refresh_time）
CREATE TABLE users (...);  -- 使用原来的schema

-- 4. 从备份恢复数据
INSERT INTO users (...) SELECT ... FROM users_backup;

-- 5. 删除备份
DROP TABLE users_backup;
```

## 📚 相关文件

### 修改的文件
- ✅ `/Users/g01d-01-0924/eden/eden-server/src/main/java/com/eden/lottery/service/PrizeInitService.java`
  - 修改了 `addEnergyRefreshTimeColumn()` 方法

### 相关的其他方法
`PrizeInitService.java` 中的其他列添加方法都使用了常量默认值，不需要修改：

- ✅ `addEnergyColumn()` - 使用 `DEFAULT 15`（常量）
- ✅ `addMaxEnergyColumn()` - 使用 `DEFAULT 15`（常量）
- ✅ `addMagicEnergyCostColumn()` - 使用 `DEFAULT 5`（常量）

## 🎓 SQLite ALTER TABLE 限制总结

### 支持的操作
1. ✅ `ADD COLUMN` - 添加列
2. ✅ `RENAME COLUMN` - 重命名列（SQLite 3.25.0+）
3. ✅ `RENAME TO` - 重命名表

### 限制
1. ❌ 不能使用函数作为 `DEFAULT` 值
2. ❌ 不能直接 `DROP COLUMN`（SQLite 3.35.0之前）
3. ❌ 不能修改列类型（需要重建表）
4. ❌ 不能添加 `NOT NULL` 约束（除非有默认值）

### 解决方案
- **函数默认值**：先 `ADD COLUMN`，再 `UPDATE` 设置值
- **删除列**：重建表
- **修改列类型**：重建表
- **添加约束**：重建表

## 🏁 测试结果

### 预期行为
1. ✅ 服务器启动成功
2. ✅ `energy_refresh_time` 列成功添加
3. ✅ 所有现有用户的 `energy_refresh_time` 都被设置为当前时间
4. ✅ 新用户创建时会自动设置 `energy_refresh_time`（在 `User` 构造函数中）

### 新用户创建逻辑
```java
public User(String userId, Integer dailyDraws) {
    // ...
    this.energy = 15;
    this.maxEnergy = 15;
    this.energyRefreshTime = LocalDateTime.now();  // ✅ 新用户自动设置
}
```

---

**修复时间：** 2024-10-19  
**问题状态：** ✅ 已修复  
**可以启动服务器：** 是

