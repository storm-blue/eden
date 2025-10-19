# 👤 星星城用户头像和下拉菜单功能

## 📋 功能概述

星星城左上角现在显示用户头像和精力值，点击头像会出现下拉菜单，包含颁布命令和施展魔法功能。

## 🎯 新增功能

### 1. 用户头像显示
- ✅ **位置**：星星城左上角
- ✅ **样式**：圆形头像，50x50px
- ✅ **内容**：用户上传的头像图片，或用户名首字母
- ✅ **交互**：点击头像显示/隐藏下拉菜单

### 2. 精力值显示
- ✅ **位置**：头像右侧
- ✅ **样式**：半透明背景，圆角设计
- ✅ **内容**：当前精力/最大精力（如：15/15）
- ✅ **图标**：⚡ 闪电图标

### 3. 用户下拉菜单
- ✅ **触发**：点击用户头像
- ✅ **内容**：
  - 用户信息区域（用户名 + 精力值）
  - 秦小淮专属功能（颁布命令、施展魔法）
- ✅ **交互**：点击外部区域自动关闭

## 🖥️ 界面设计

### 头像区域布局
```
┌─────────────────────────────────────────┐
│ 星星城左上角                            │
│                                         │
│ ┌─────┐ ┌─────────────────────────────┐ │
│ │ 👤  │ │ ⚡ 15/15                    │ │
│ │头像 │ │ 精力值                      │ │
│ └─────┘ └─────────────────────────────┘ │
│                                         │
│ 点击头像 ↓                              │
│                                         │
│ ┌─────────────────────────────────────┐ │
│ │ 秦小淮                              │ │
│ │ ⚡ 精力: 15/15                      │ │
│ │ ─────────────────────────────────── │ │
│ │ 📜 颁布命令                         │ │
│ │ ✨ 施展魔法                         │ │
│ └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
```

### 样式特点
- **头像**：渐变背景 + 阴影效果 + 悬停缩放
- **精力值**：毛玻璃效果 + 圆角设计
- **下拉菜单**：半透明背景 + 毛玻璃模糊 + 阴影
- **按钮**：渐变背景 + 悬停效果

## 🔧 技术实现

### 前端状态管理
```javascript
// 用户头像和精力状态
const [userAvatar, setUserAvatar] = useState(null) // 用户头像路径
const [userEnergy, setUserEnergy] = useState(null) // 用户精力信息
const [showUserDropdown, setShowUserDropdown] = useState(false) // 显示用户下拉菜单
```

### API 调用函数
```javascript
// 获取用户头像
const fetchUserAvatar = async () => {
    const response = await fetch(`/api/avatar/${userName}`)
    const data = await response.json()
    if (data.success) {
        setUserAvatar(data.data.avatarPath)
    }
}

// 获取用户精力信息
const fetchUserEnergy = async () => {
    const response = await fetch(`/api/user-info/${userName}/energy`)
    const data = await response.json()
    if (data.success) {
        setUserEnergy(data.data)
    }
}
```

### 数据获取时机
```javascript
useEffect(() => {
    if (showStarCity) {
        fetchStarCityData()
        fetchSpecialCombos()
        loadAllBuildingResidents()
        loadAllResidenceEvents()
        fetchAllDecrees()
        fetchGiantAttackStatus()
        fetchUserAvatar() // 新增：获取用户头像
        fetchUserEnergy() // 新增：获取用户精力信息
    }
}, [showStarCity])
```

### 点击外部关闭菜单
```javascript
useEffect(() => {
    const handleClickOutside = (event) => {
        if (showUserDropdown && !event.target.closest('[data-user-dropdown]')) {
            setShowUserDropdown(false)
        }
    }

    if (showUserDropdown) {
        document.addEventListener('mousedown', handleClickOutside)
    }

    return () => {
        document.removeEventListener('mousedown', handleClickOutside)
    }
}, [showUserDropdown])
```

## 📊 功能对比

### 之前的设计
```
┌─────────────────────────────────────────┐
│ 星星城左上角                            │
│                                         │
│ ┌─────────────┐ ┌─────────────┐         │
│ │📜 颁布命令  │ │✨ 施展魔法  │         │
│ └─────────────┘ └─────────────┘         │
│                                         │
│ 问题：                                  │
│ • 按钮占用空间大                        │
│ • 没有用户身份信息                      │
│ • 没有精力值显示                        │
│ • 界面不够简洁                          │
└─────────────────────────────────────────┘
```

### 现在的设计
```
┌─────────────────────────────────────────┐
│ 星星城左上角                            │
│                                         │
│ ┌─────┐ ┌─────────────┐                 │
│ │ 👤  │ │ ⚡ 15/15    │                 │
│ │头像 │ │ 精力值      │                 │
│ └─────┘ └─────────────┘                 │
│                                         │
│ 优势：                                  │
│ • 界面更简洁                            │
│ • 显示用户身份                          │
│ • 实时显示精力值                        │
│ • 功能整合到下拉菜单                    │
│ • 节省屏幕空间                          │
└─────────────────────────────────────────┘
```

## 🎮 用户体验

### 交互流程
1. **进入星星城** → 自动加载用户头像和精力信息
2. **查看状态** → 头像和精力值一目了然
3. **点击头像** → 下拉菜单展开
4. **选择功能** → 颁布命令或施展魔法
5. **点击外部** → 菜单自动关闭

### 视觉反馈
- **头像悬停**：缩放效果 + 阴影加深
- **按钮悬停**：背景渐变反转
- **菜单展开**：平滑动画效果
- **精力不足**：可扩展为红色警告

## 🔄 数据更新

### 实时更新
- **精力值**：施展魔法后自动刷新
- **头像**：用户上传新头像后刷新
- **菜单状态**：点击外部或选择功能后关闭

### 缓存策略
- **头像**：页面加载时获取一次
- **精力**：每次进入星星城时获取
- **菜单**：本地状态管理

## 📱 响应式设计

### 移动端适配
- **头像大小**：50px 适合触摸操作
- **菜单宽度**：180px 最小宽度
- **间距**：12px 适合手指点击
- **字体**：14px 保证可读性

### 桌面端优化
- **悬停效果**：鼠标悬停时的视觉反馈
- **键盘支持**：可扩展 ESC 键关闭菜单
- **精确点击**：鼠标点击的精确控制

## 🎨 样式细节

### 头像样式
```css
width: 50px;
height: 50px;
border-radius: 50%;
background: userAvatar ? `url(${userAvatar})` : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
background-size: cover;
background-position: center;
box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
border: 2px solid rgba(255, 255, 255, 0.3);
backdrop-filter: blur(10px);
```

### 精力值样式
```css
background: rgba(255, 255, 255, 0.9);
backdrop-filter: blur(10px);
border-radius: 20px;
padding: 8px 16px;
box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
border: 1px solid rgba(255, 255, 255, 0.3);
```

### 下拉菜单样式
```css
background: rgba(255, 255, 255, 0.95);
backdrop-filter: blur(20px);
border-radius: 12px;
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
border: 1px solid rgba(255, 255, 255, 0.2);
```

## 🔒 权限控制

### 功能可见性
- **所有用户**：头像 + 精力值显示
- **秦小淮**：额外显示颁布命令和施展魔法按钮
- **其他用户**：下拉菜单只显示用户信息

### 数据安全
- **头像获取**：需要有效的用户名
- **精力获取**：需要有效的用户名
- **功能调用**：后端验证用户权限

## 🐛 错误处理

### 网络错误
- **头像加载失败**：显示用户名首字母
- **精力获取失败**：显示 "--" 占位符
- **API 调用失败**：控制台记录错误

### 数据异常
- **头像路径为空**：使用默认渐变背景
- **精力数据为空**：不显示精力值
- **用户名为空**：不显示头像区域

## 📚 相关文档

- [ENERGY_SYSTEM.md](./ENERGY_SYSTEM.md) - 精力系统说明
- [MAGIC_SYSTEM.md](./MAGIC_SYSTEM.md) - 魔法系统说明
- [DECREE_SYSTEM.md](./DECREE_SYSTEM.md) - 命令系统说明

## 🚀 未来扩展

### 可能的功能
1. **头像上传**：点击头像直接上传新头像
2. **精力动画**：精力变化时的动画效果
3. **用户设置**：下拉菜单中添加设置选项
4. **通知中心**：下拉菜单中显示系统通知
5. **快捷操作**：常用功能的快捷入口

### 性能优化
1. **图片懒加载**：头像图片的懒加载
2. **缓存优化**：头像和精力数据的缓存
3. **动画优化**：使用 CSS transform 而非位置变化
4. **内存管理**：及时清理事件监听器

---

**更新时间**：2024-10-19  
**版本**：v1.0.0  
**状态**：✅ 已完成，可立即使用
