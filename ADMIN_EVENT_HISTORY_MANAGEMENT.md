# 管理后台居所事件历史管理功能

## 概述

为管理后台新增了清除居所事件历史记录的功能，包括后端API接口和前端管理界面，允许管理员清除指定居所的所有事件历史记录，并提供事件历史统计概览。

## 功能特性

### 🔧 后端API接口

#### 1. 清除指定居所事件历史

**接口地址：** `DELETE /api/admin/residence-event-history/{residence}`

**权限要求：** 管理员登录（需要Bearer Token）

**路径参数：**
- `residence`: 居所类型（castle, park, city_hall, white_dove_house, palace）

**响应示例：**
```json
{
  "success": true,
  "message": "清除成功",
  "data": {
    "residence": "castle",
    "residenceName": "城堡🏰",
    "message": "居所事件历史清除成功"
  }
}
```

#### 2. 获取所有居所事件历史概览

**接口地址：** `GET /api/admin/residence-event-history/overview`

**权限要求：** 管理员登录（需要Bearer Token）

**响应示例：**
```json
{
  "success": true,
  "message": "获取成功",
  "data": {
    "overview": {
      "castle": {
        "totalCount": 15,
        "residenceName": "城堡🏰"
      },
      "park": {
        "totalCount": 8,
        "residenceName": "公园🌳"
      },
      "city_hall": {
        "totalCount": 12,
        "residenceName": "市政厅🏛️"
      },
      "white_dove_house": {
        "totalCount": 5,
        "residenceName": "小白鸽家🕊️"
      },
      "palace": {
        "totalCount": 7,
        "residenceName": "行宫🏯"
      }
    },
    "message": "获取居所事件历史概览成功"
  }
}
```

### 🖥️ 前端管理界面

#### 界面功能

1. **居所事件历史概览**
   - 卡片式布局显示所有居所的事件历史统计
   - 响应式设计，适配不同屏幕尺寸
   - 实时数据刷新功能
   - 清晰的数据可视化

2. **清除居所事件历史**
   - 下拉选择要清除的居所
   - 双重确认机制防止误操作
   - 操作成功后自动刷新概览数据
   - 完整的用户反馈和错误提示

#### 界面访问

1. **打开管理界面**
   ```bash
   # 通过后端服务访问
   http://localhost:5000/admin.html
   ```

2. **管理员登录**
   - 用户名：`admin`
   - 密码：`admin2008`

3. **访问居所事件管理**
   - 登录成功后点击"居所事件"标签页
   - 使用"🔄 刷新概览"按钮加载数据
   - 使用"🗑️ 清除历史"功能管理事件记录

## 实现细节

### 权限验证
- 所有接口都需要管理员登录认证
- 使用Bearer Token进行身份验证
- 无效Token或未登录用户将收到"未授权访问"错误

### 参数验证
- 验证居所参数是否为空
- 验证居所类型是否为有效值
- 无效参数将返回相应错误信息

### 错误处理
- 完善的异常捕获和错误日志记录
- 用户友好的错误信息返回
- 操作失败时的详细错误描述

### 日志记录
- 记录管理员清除操作的详细信息
- 包含居所名称、管理员操作时间等
- 便于审计和问题排查

## 测试验证

提供了完整的测试脚本 `test-admin-clear-event-history.sh`，包括：

1. **管理员登录认证测试**
2. **获取居所事件历史概览测试**
3. **清除指定居所事件历史测试**
4. **验证清除结果测试**
5. **无效参数处理测试**
6. **权限验证测试**
7. **管理员登出测试**

## 使用说明

1. **启动后端服务**
   ```bash
   cd eden-server
   npm start
   ```

2. **运行测试脚本**
   ```bash
   ./test-admin-clear-event-history.sh
   ```

3. **管理员登录**
   - 用户名：`admin`
   - 密码：`admin2008`

4. **清除居所事件历史**
   ```bash
   curl -X DELETE "http://localhost:5000/api/admin/residence-event-history/castle" \
     -H "Authorization: Bearer YOUR_TOKEN"
   ```

## 安全注意事项

- 清除操作不可逆，请谨慎使用
- 建议在清除前先备份重要数据
- 仅管理员可执行清除操作
- 所有操作都有详细的日志记录

## 技术实现

- **控制器层：** `AdminController.java`
- **服务层：** `ResidenceEventService.java`
- **数据层：** `ResidenceEventHistoryMapper.java`
- **权限验证：** `AdminService.java`
- **测试脚本：** `test-admin-clear-event-history.sh`
