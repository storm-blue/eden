# 🔧 命令初始化逻辑修复

## 问题描述

在添加"创造彩虹"命令后，发现新命令没有被初始化到数据库中。

### 原因分析

之前的初始化逻辑：
```java
if (!tables.next()) {
    // 表不存在，创建表并初始化所有命令
    createDecreeTable(connection);
} else {
    // 表已存在，跳过初始化
    logger.info("decree表已存在");
}
```

**问题**：
- 当 `decree` 表已经存在时（包含"不得靠近城堡"命令）
- 系统直接跳过初始化，不检查是否有新命令需要添加
- 导致新增的"创造彩虹"命令无法被自动添加到数据库

## 解决方案

### 修改后的逻辑

```java
if (!tables.next()) {
    // 表不存在，创建表并初始化所有命令
    createDecreeTable(connection);
} else {
    // 表已存在，检查并添加缺失的命令
    logger.info("decree表已存在，检查并添加缺失的命令...");
    ensureDecreeRecords(connection);
}
```

### 新增方法：ensureDecreeRecords

```java
/**
 * 确保所有必需的命令记录都存在
 */
private void ensureDecreeRecords(Connection connection) throws Exception {
    try (Statement statement = connection.createStatement()) {
        // 检查每个命令是否存在，不存在则添加
        
        // 1. 检查"不得靠近城堡"命令
        String checkDecree1 = "SELECT COUNT(*) as cnt FROM decree WHERE code = 'NO_CASTLE_ACCESS'";
        ResultSet rs1 = statement.executeQuery(checkDecree1);
        if (rs1.next() && rs1.getInt("cnt") == 0) {
            // 不存在，添加命令
            statement.execute(insertDecree1Sql);
            logger.info("添加命令: NO_CASTLE_ACCESS");
        }
        rs1.close();
        
        // 2. 检查"创造彩虹"命令
        String checkDecree2 = "SELECT COUNT(*) as cnt FROM decree WHERE code = 'CREATE_RAINBOW'";
        ResultSet rs2 = statement.executeQuery(checkDecree2);
        if (rs2.next() && rs2.getInt("cnt") == 0) {
            // 不存在，添加命令
            statement.execute(insertDecree2Sql);
            logger.info("添加命令: CREATE_RAINBOW");
        }
        rs2.close();
    }
}
```

## 优势

### 1. 增量更新
- ✅ 只添加缺失的命令，不影响已存在的命令
- ✅ 支持后续新增更多命令，无需手动SQL

### 2. 幂等性
- ✅ 多次启动应用不会重复添加
- ✅ 避免主键冲突或重复数据

### 3. 自动化
- ✅ 无需手动执行SQL脚本
- ✅ 应用启动时自动检查和修复

### 4. 向后兼容
- ✅ 对已有部署环境友好
- ✅ 不需要删除或修改现有数据

## 使用方法

### 自动修复（推荐）

只需重启后端服务：

```bash
cd eden-server
./start.sh  # 或 start.bat (Windows)
```

启动日志会显示：
```
decree表已存在，检查并添加缺失的命令...
添加命令: CREATE_RAINBOW
```

### 手动添加（备选）

如果不想重启服务，可以手动执行SQL：

```bash
sqlite3 eden-server/lottery.db << EOF
INSERT INTO decree (code, name, description, active)
VALUES ('CREATE_RAINBOW', '创造彩虹', 
        '让星星城上空出现美丽的彩虹！彩虹将横跨整个星星城，为所有居民带来美好的祝福。', 
        0);
EOF
```

## 验证

### 1. 检查数据库

```bash
sqlite3 eden-server/lottery.db "SELECT code, name, active FROM decree;"
```

预期输出：
```
NO_CASTLE_ACCESS|不得靠近城堡|0
CREATE_RAINBOW|创造彩虹|0
```

### 2. 检查API

```bash
curl -s "http://localhost:8080/api/decree/list?userId=秦小淮" | jq '.decrees'
```

应该能看到两个命令。

### 3. 前端测试

1. 登录为"秦小淮"
2. 进入星星城
3. 点击"📜 颁布命令"
4. 应该能看到"创造彩虹"命令

## 后续新增命令

以后如果要添加新命令，按以下步骤：

### 1. 在 DecreeService 中添加常量

```java
public static final String DECREE_NEW_COMMAND = "NEW_COMMAND";
```

### 2. 在 ensureDecreeRecords 中添加检查和插入逻辑

```java
// 检查并添加"新命令"
String checkDecree3 = "SELECT COUNT(*) as cnt FROM decree WHERE code = 'NEW_COMMAND'";
ResultSet rs3 = statement.executeQuery(checkDecree3);
if (rs3.next() && rs3.getInt("cnt") == 0) {
    String insertDecree3 = """
            INSERT INTO decree (code, name, description, active)
            VALUES ('NEW_COMMAND', '新命令名称', '新命令描述', 0)
            """;
    statement.execute(insertDecree3);
    logger.info("添加命令: NEW_COMMAND");
}
rs3.close();
```

### 3. 重启应用

命令会自动添加到数据库中。

## 最佳实践

### 命令代码规范
- 使用大写字母和下划线：`CREATE_RAINBOW`
- 语义清晰，见名知意
- 避免使用特殊字符

### 命令名称规范
- 简洁明了，4-6个汉字
- 描述命令的主要作用
- 例如："创造彩虹"、"不得靠近城堡"

### 命令描述规范
- 详细说明命令的效果
- 说明影响范围和持续时间
- 提示用户可能的后果

## 总结

通过添加 `ensureDecreeRecords` 方法，我们实现了：

1. ✅ **自动检测** - 启动时自动检查缺失的命令
2. ✅ **自动添加** - 缺失的命令自动插入数据库
3. ✅ **幂等操作** - 多次执行不会出错
4. ✅ **易于扩展** - 新增命令只需添加检查逻辑
5. ✅ **向后兼容** - 不影响现有部署环境

这种设计模式可以应用到其他需要数据初始化的场景，确保数据的完整性和一致性。

