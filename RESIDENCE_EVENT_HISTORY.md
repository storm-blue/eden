# 居所事件历史功能

## 功能概述

Eden项目新增了完整的居所事件历史功能，能够记录和展示每个居所的历史事件，为用户提供丰富的历史回顾体验。

## 主要特性

### 🏛️ **自动历史记录**
- 每次生成新的居所事件时，系统会自动记录到历史表
- 记录包含事件数据、居住人员信息、特效设置和时间戳
- 自动清理机制，每个居所保留最新的20条历史记录

### 📊 **数据存储结构**
- **数据库表**: `residence_event_history`
- **字段**:
  - `id`: 主键
  - `residence`: 居所类型 (castle, park, city_hall, white_dove_house, palace)
  - `event_data`: 事件数据 (JSON格式)
  - `residents_info`: 居住人员信息 (JSON格式)
  - `show_heart_effect`: 是否显示爱心特效
  - `special_text`: 特殊文字
  - `show_special_effect`: 是否显示特殊特效
  - `created_at`: 创建时间

### 🎮 **前端界面**
- 在每个居所弹框中新增 **"📜 历史"** 按钮
- 点击按钮打开专用的历史查看弹窗
- 历史记录按时间倒序显示，最新的在上面
- 每条历史记录显示:
  - ⏰ 发生时间
  - 👥 当时的居住人员
  - 📋 具体的事件内容（支持特殊事件的粉色发光效果）

### 🔌 **API接口**

#### 1. 获取居所事件历史
```http
GET /api/residence-event-history/{residence}
```
- 返回指定居所的最近20条历史记录
- 包含历史数据和统计信息

#### 2. 获取指定数量的历史记录
```http
GET /api/residence-event-history/{residence}/{limit}
```
- 获取指定数量的历史记录（最多50条）

#### 3. 获取历史统计信息
```http
GET /api/residence-event-history/{residence}/stats
```
- 返回指定居所的历史记录统计

#### 4. 获取所有居所历史概览
```http
GET /api/residence-event-history/overview
```
- 返回所有居所的历史记录概览

#### 5. 清理历史记录
```http
DELETE /api/residence-event-history/{residence}
```
- 清理指定居所的所有历史记录

## 技术实现

### 🏗️ **后端架构**

#### 实体类
- `ResidenceEventHistory`: 历史记录实体
- 支持完整的CRUD操作

#### Mapper层
- `ResidenceEventHistoryMapper`: MyBatis映射接口
- 提供丰富的查询和管理方法

#### 服务层
- `ResidenceEventService`: 扩展了历史记录功能
- 在每次事件更新时自动记录历史
- 提供历史查询和统计方法

#### 控制器层
- `ResidenceEventHistoryController`: 专用的历史API控制器
- 提供完整的RESTful接口

### 🎨 **前端实现**

#### 状态管理
```javascript
const [showEventHistory, setShowEventHistory] = useState(false)
const [eventHistory, setEventHistory] = useState([])
const [loadingEventHistory, setLoadingEventHistory] = useState(false)
```

#### 核心功能
- `fetchEventHistory()`: 获取历史数据
- `showResidenceEventHistory()`: 显示历史弹窗
- `formatHistoryTime()`: 格式化时间显示
- `parseEventData()`: 解析事件数据

#### UI设计
- 使用与居所弹框相同的设计风格
- 支持移动端横屏显示
- 滚动列表展示历史记录
- 特殊事件保持粉色发光效果

## 使用方法

### 👤 **用户操作**
1. 进入星星城界面
2. 点击任意建筑（城堡🏰、公园🌳、市政厅🏛️、小白鸽家🕊️、行宫🏯）
3. 在弹出的居所信息窗口中，点击 **"📜 历史"** 按钮
4. 查看该居所的历史事件记录

### 🔧 **开发者测试**
```bash
# 运行测试脚本
./test-event-history.sh
```

测试脚本会：
- 触发各居所的事件生成
- 检查历史记录的创建
- 验证API接口的正确性
- 显示历史数据的详细信息

## 数据流程

```
用户操作 → 事件生成 → 自动记录历史 → 用户查看历史
    ↓           ↓            ↓            ↓
星星城交互 → ResidenceEventService → 数据库存储 → 历史弹窗展示
```

## 特色功能

### 🎭 **智能事件分类**
- 普通事件：黑色字体，标准样式
- 特殊事件：粉色发光字体，梦幻紫色渐变背景
- 历史记录完美保留原有的视觉效果

### 📱 **移动端优化**
- 支持强制横屏显示
- 响应式布局适配
- 触摸友好的操作界面

### 🔄 **自动维护**
- 自动清理超过20条的旧记录
- 防止数据库无限增长
- 保持系统性能稳定

## 未来扩展

### 可能的增强功能
- 🔍 历史记录搜索和筛选
- 📈 更详细的统计图表
- 📤 历史记录导出功能
- 🏷️ 自定义标签和分类
- 📅 按日期范围查询

## 注意事项

1. **性能考虑**: 历史记录会随时间增长，已实现自动清理机制
2. **数据一致性**: 历史记录与当前事件状态可能不同步，这是正常现象
3. **移动端**: 建议在横屏模式下使用以获得最佳体验
4. **兼容性**: 与现有的居所事件系统完全兼容，不影响原有功能

---

*该功能已完整实现并可投入使用。如需修改或扩展，请参考相关的源码文件。*
