# Eden 命令系统

## 概述

命令系统是星星城的特权功能，允许秦小淮颁布和取消命令，对其他用户的行为产生即时影响。

## 功能特性

### 1. 权限控制
- **颁布权限**：仅秦小淮可以颁布和取消命令
- **可见性**：命令按钮仅在星星城页面对秦小淮可见（左上角）

### 2. 当前支持的命令

#### NO_CASTLE_ACCESS - 不得靠近城堡

**命令效果**：
1. **立即驱逐**：颁布命令时立即将城堡中除了李星斗之外的所有人驱逐到指定居所
   - 存子 → 行宫
   - 小白鸽 → 小白鸽家
   - 白婆婆 → 小白鸽家
   - 大祭司 → 行宫
   - 严伯升 → 市政厅

2. **持续限制**：命令生效期间
   - 所有用户的漫游目标都不能包含城堡
   - 李星斗可以继续居住在城堡（不受影响）

3. **自动刷新**：驱逐完成后自动刷新相关居所的事件

## 技术实现

### 后端架构

#### 1. 数据层
- **实体**：`Decree.java` - 命令实体
- **Mapper**：`DecreeMapper.java` - MyBatis接口
- **数据库表**：`decree`
  ```sql
  CREATE TABLE decree (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      code VARCHAR(50) NOT NULL UNIQUE,
      name VARCHAR(100) NOT NULL,
      description TEXT,
      active INTEGER NOT NULL DEFAULT 0,
      issued_at TIMESTAMP,
      cancelled_at TIMESTAMP,
      issued_by VARCHAR(50)
  )
  ```

#### 2. 服务层
- **DecreeService**：命令业务逻辑
  - `getAllDecrees()`: 获取所有命令
  - `issueDecree(code, userId)`: 颁布命令
  - `cancelDecree(code, userId)`: 取消命令
  - `isDecreeActive(code)`: 检查命令是否激活
  - `executeDecreeEffect(code)`: 执行命令效果

#### 3. 控制层
- **DecreeController**：命令API接口
  - `GET /api/decree/list`: 获取命令列表
  - `POST /api/decree/issue`: 颁布命令
  - `POST /api/decree/cancel`: 取消命令

#### 4. 集成统一移动方法
- **DecreeService**：使用`StarCityService.moveUserToBuilding()`
  - 自动刷新相关居所事件
  - 统一的错误处理和日志
  - 支持移动原因标记（"decree"）
  - 与其他移动场景（roaming、manual）保持一致

#### 5. 漫游逻辑集成
- **UserRoamingLogicService**：修改了`performSimpleRandomMove`方法
  - 检查`NO_CASTLE_ACCESS`命令是否激活
  - 如果激活，从可选居所中移除城堡

### 前端实现

#### 1. 组件
- **DecreeModal.jsx**：命令管理弹窗组件
- **DecreeModal.css**：弹窗样式

#### 2. 功能集成
- **LuckyWheel.jsx**：
  - 状态管理：`showDecreeModal`, `decrees`, `loadingDecrees`, `operatingDecree`
  - API函数：`fetchDecrees()`, `issueDecree()`, `cancelDecree()`
  - 星星城左上角添加"📜 颁布命令"按钮（仅秦小淮可见）

## 使用指南

### 1. 颁布命令
1. 以秦小淮身份登录
2. 进入星星城
3. 点击左上角"📜 颁布命令"按钮
4. 在弹窗中选择命令
5. 点击"颁布命令"按钮
6. 系统会立即执行命令效果

### 2. 查看命令状态
- 已激活的命令会显示特殊样式（金色边框和发光效果）
- 显示颁布时间

### 3. 取消命令
1. 在命令管理弹窗中
2. 找到已激活的命令
3. 点击"取消命令"按钮
4. 命令效果立即失效

## API文档

### 获取命令列表
```
GET /api/decree/list?userId={userId}
```

**响应示例**：
```json
{
  "success": true,
  "decrees": [
    {
      "id": 1,
      "code": "NO_CASTLE_ACCESS",
      "name": "不得靠近城堡",
      "description": "城堡禁止入内！...",
      "active": true,
      "issuedAt": "2025-01-15T10:30:00",
      "issuedBy": "秦小淮"
    }
  ]
}
```

### 颁布命令
```
POST /api/decree/issue
Content-Type: application/x-www-form-urlencoded

code=NO_CASTLE_ACCESS&userId=秦小淮
```

**响应示例**：
```json
{
  "success": true,
  "message": "命令已颁布"
}
```

### 取消命令
```
POST /api/decree/cancel
Content-Type: application/x-www-form-urlencoded

code=NO_CASTLE_ACCESS&userId=秦小淮
```

**响应示例**：
```json
{
  "success": true,
  "message": "命令已取消"
}
```

## 测试

运行测试脚本：
```bash
./test-decree.sh
```

测试脚本会依次执行：
1. 获取命令列表
2. 颁布"不得靠近城堡"命令
3. 验证命令状态
4. 检查城堡居民（应只剩李星斗）
5. 取消命令
6. 再次验证状态

## 扩展指南

### 添加新命令

#### 1. 数据库初始化
在 `PrizeInitService.createDecreeTable()` 中添加新命令：
```java
String insertDecree = """
        INSERT INTO decree (code, name, description, active)
        VALUES ('NEW_DECREE_CODE', '新命令名称', '命令描述', 0)
        """;
```

#### 2. 定义命令常量
在 `DecreeService` 中添加常量：
```java
public static final String DECREE_NEW_CODE = "NEW_DECREE_CODE";
```

#### 3. 实现命令效果
在 `DecreeService.executeDecreeEffect()` 中添加分支：
```java
if (DECREE_NEW_CODE.equals(code)) {
    executeNewDecreeEffect();
}
```

#### 4. 实现效果方法
```java
private void executeNewDecreeEffect() {
    // 实现具体的命令效果逻辑
    logger.info("执行新命令效果");
}
```

#### 5. 集成到其他系统
- 如需影响漫游，修改 `UserRoamingLogicService`
- 如需影响居住选择，修改 `ResidenceController`
- 如需影响事件生成，修改 `ResidenceEventService`

## 注意事项

1. **权限验证**：所有命令操作都会验证用户是否为秦小淮
2. **事件刷新**：驱逐用户后会自动刷新相关居所的事件
3. **日志记录**：所有命令操作都有详细的日志记录
4. **幂等性**：重复颁布/取消同一命令不会产生错误
5. **持久化**：命令状态存储在数据库中，重启后保持

## 未来扩展

可以添加的新命令类型：
- **宵禁令**：特定时间段限制用户移动
- **集合令**：召集所有用户到指定居所
- **禁言令**：禁止特定用户的某些操作
- **赦免令**：取消对某用户的所有限制
- **排他令**：指定某居所只允许特定用户进入

## 文件清单

### 后端文件
- `src/main/java/com/eden/lottery/entity/Decree.java`
- `src/main/java/com/eden/lottery/mapper/DecreeMapper.java`
- `src/main/java/com/eden/lottery/service/DecreeService.java`
- `src/main/java/com/eden/lottery/controller/DecreeController.java`
- `src/main/java/com/eden/lottery/service/PrizeInitService.java` (添加了数据库迁移)
- `src/main/java/com/eden/lottery/service/UserRoamingLogicService.java` (修改了漫游逻辑)

### 前端文件
- `src/components/DecreeModal.jsx`
- `src/components/DecreeModal.css`
- `src/components/LuckyWheel.jsx` (集成了命令功能)

### 测试文件
- `test-decree.sh`

### 文档
- `DECREE_SYSTEM.md` (本文件)

