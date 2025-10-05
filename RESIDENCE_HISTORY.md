# 居住历史记录功能

## 功能概述

居住历史记录功能可以跟踪和管理星星城中用户的居住变更历史，包括：

- 自动记录用户每次更换居住地点的历史
- 管理后台查看和管理所有居住历史记录
- 提供详细的居住统计信息
- 支持按用户和地点筛选历史记录

## 数据库表结构

### residence_history 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | INTEGER | 主键，自增 |
| user_id | VARCHAR(50) | 用户ID |
| residence | VARCHAR(20) | 新居住地点 |
| previous_residence | VARCHAR(20) | 之前的居住地点 |
| change_time | TIMESTAMP | 搬迁时间 |
| ip_address | VARCHAR(45) | 用户IP地址 |
| user_agent | VARCHAR(500) | 用户代理信息 |

## API 接口

### 用户端接口

- `GET /api/residence/history/{userId}` - 获取用户居住历史
- `GET /api/residence/history/location/{residence}` - 获取地点居住历史
- `GET /api/residence/statistics` - 获取居住统计信息

### 管理端接口

- `GET /api/admin/residence-history` - 获取所有居住历史（分页）
- `GET /api/admin/residence-history/user/{userId}` - 获取指定用户居住历史
- `GET /api/admin/residence-history/location/{residence}` - 获取指定地点居住历史
- `GET /api/admin/residence-history/stats` - 获取居住历史统计
- `DELETE /api/admin/residence-history/{historyId}` - 删除居住历史记录

## 管理后台功能

### 居住历史标签页

1. **统计信息显示**
   - 总搬迁次数
   - 系统活跃状态

2. **历史记录列表**
   - 显示所有用户的居住变更记录
   - 支持分页浏览
   - 显示搬迁前后地点、时间、IP地址等信息

3. **筛选功能**
   - 按用户ID筛选
   - 按居住地点筛选

4. **管理功能**
   - 删除历史记录
   - 刷新数据和统计信息

## 自动记录机制

当用户通过 `/api/residence/set` 接口更换居住地点时，系统会自动：

1. 检查新地点与当前地点是否相同
2. 如果不同，则记录一条居住历史
3. 保存用户IP地址和浏览器信息
4. 更新用户的当前居住地点

## 居住地点代码对照

| 代码 | 名称 | 图标 |
|------|------|------|
| castle | 城堡 | 🏰 |
| city_hall | 市政厅 | 🏛️ |
| palace | 行宫 | 🏯 |
| dove_house | 小白鸽家 | 🕊️ |
| park | 公园 | 🌳 |

## 测试方法

运行测试脚本：
```bash
./test-residence-history.sh
```

或者手动测试：
1. 启动后端服务
2. 创建测试用户
3. 多次更换用户居住地点
4. 访问管理后台查看居住历史记录

## 注意事项

1. 居住历史记录会自动创建表结构，无需手动建表
2. 如果用户设置相同的居住地点，不会产生历史记录
3. 删除历史记录不会影响用户当前的居住状态
4. 管理员可以删除历史记录，但建议谨慎操作
