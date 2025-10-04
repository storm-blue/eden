# 数据库迁移功能说明

## 概述

Eden抽奖系统具备自动数据库迁移功能，能够在应用启动时自动检查并更新数据库表结构，确保新功能的字段能够无缝添加到现有数据库中。

## 迁移机制

### 触发时机
- 每次后端服务启动时自动执行
- 通过 `PrizeInitService` 的 `ApplicationRunner` 接口实现
- 在奖品数据初始化之前执行（`@Order(1)`）

### 检查逻辑
1. **连接数据库**：获取数据源连接
2. **获取表结构**：使用 `DatabaseMetaData` 获取现有列信息
3. **逐列检查**：检查必需的列是否存在
4. **自动添加**：如果列不存在，则自动执行 `ALTER TABLE` 语句添加

## 当前支持的迁移

### users表迁移

#### 1. wish_count字段
- **类型**：`INTEGER NOT NULL DEFAULT 0`
- **用途**：存储用户可用的许愿次数
- **迁移SQL**：`ALTER TABLE users ADD COLUMN wish_count INTEGER NOT NULL DEFAULT 0`

#### 2. residence字段
- **类型**：`VARCHAR(20) DEFAULT NULL`
- **用途**：存储用户选择的居住地点
- **可选值**：`'castle'`, `'city_hall'`, `'palace'`, `'dove_house'`, `'park'`
- **迁移SQL**：`ALTER TABLE users ADD COLUMN residence VARCHAR(20) DEFAULT NULL`

## 日志输出

### 正常迁移日志
```
INFO  - 开始数据库迁移检查...
INFO  - users表缺少residence列，添加列...
INFO  - residence列添加成功
INFO  - users表结构检查完成
INFO  - 数据库迁移检查完成
```

### 无需迁移日志
```
INFO  - 开始数据库迁移检查...
INFO  - users表结构检查完成
INFO  - 数据库迁移检查完成
```

## 安全特性

1. **幂等性**：多次执行不会产生错误
2. **事务安全**：迁移失败会抛出异常，阻止服务启动
3. **数据保留**：只添加新列，不修改现有数据
4. **默认值**：新列都有合理的默认值

## 扩展方式

如需添加新的字段迁移，请在 `PrizeInitService.checkAndMigrateUsersTable()` 方法中添加：

```java
// 检查新字段是否存在
if (!columns.contains("new_field_name")) {
    logger.info("users表缺少new_field_name列，添加列...");
    addNewFieldColumn(connection);
}
```

并实现对应的 `addNewFieldColumn` 方法：

```java
private void addNewFieldColumn(Connection connection) throws Exception {
    String sql = "ALTER TABLE users ADD COLUMN new_field_name DATA_TYPE DEFAULT_VALUE";
    
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(sql);
        logger.info("new_field_name列添加成功");
    }
}
```

## 测试验证

使用提供的测试脚本验证迁移功能：

```bash
./test-migration.sh
```

该脚本会：
1. 检查数据库文件是否存在
2. 显示当前表结构
3. 检查各个字段是否存在
4. 提供测试建议和预期日志

## 注意事项

1. **备份重要**：生产环境部署前请备份数据库
2. **日志监控**：部署时请关注迁移相关的日志输出
3. **回滚计划**：如有问题，可以从备份恢复数据库
4. **测试验证**：部署后请测试新功能是否正常工作
