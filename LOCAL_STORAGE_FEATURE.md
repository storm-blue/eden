# 用户信息本地存储功能

## 功能概述

为Eden抽奖系统添加了用户信息本地存储功能，用户登录一次后，下次访问时会自动恢复登录状态，无需重复输入用户名。

## 实现的功能

### 1. 自动保存用户信息
- 用户输入用户名并点击"开始游戏"后，用户名会自动保存到浏览器的localStorage
- 使用键名：`eden_userName`
- 保存时机：用户确认姓名时（`handleNameConfirm`函数）

### 2. 自动恢复登录状态
- 页面加载时自动检查localStorage中是否有保存的用户名
- 如果有保存的用户名，自动：
  - 设置用户名状态
  - 隐藏用户名输入框
  - 获取用户信息（剩余抽奖次数等）
  - 获取用户头像
- 实现方式：使用`useEffect`钩子监听页面加载

### 3. 退出登录功能
- 在用户信息显示区域添加了"🚪 退出"按钮
- 点击后会弹出确认对话框
- 确认后会：
  - 清除localStorage中的用户名
  - 重置所有相关状态
  - 显示用户名输入框
  - 清除用户信息和头像

## 技术实现

### 状态初始化
```javascript
const [userName, setUserName] = useState(() => {
    // 从localStorage读取保存的用户名
    return localStorage.getItem('eden_userName') || ''
})

const [showNameInput, setShowNameInput] = useState(() => {
    // 如果localStorage中有用户名，则不显示输入框
    return !localStorage.getItem('eden_userName')
})
```

### 自动恢复用户信息
```javascript
useEffect(() => {
    const savedUserName = localStorage.getItem('eden_userName')
    if (savedUserName && savedUserName.trim()) {
        console.log('自动加载保存的用户:', savedUserName)
        fetchUserInfo(savedUserName)
        fetchUserAvatar(savedUserName)
    }
}, [])
```

### 保存用户信息
```javascript
const handleNameConfirm = async () => {
    // ... 验证逻辑 ...
    
    // 保存用户名到localStorage
    localStorage.setItem('eden_userName', newUserName)
    
    // ... 其他逻辑 ...
}
```

### 退出登录
```javascript
const handleLogout = () => {
    if (confirm('确定要退出登录吗？')) {
        // 清除localStorage
        localStorage.removeItem('eden_userName')
        // 重置所有相关状态
        setUserName('')
        setUserInfo(null)
        setUserAvatar(null)
        setShowNameInput(true)
        setTempName('')
        setShowWelcomeEffect(false)
        setWelcomeEffectFinished(true)
    }
}
```

## 用户体验

### 首次访问
1. 用户看到用户名输入框
2. 输入用户名并点击"开始游戏"
3. 用户名被保存到本地存储
4. 正常进入游戏界面

### 再次访问
1. 页面自动加载保存的用户名
2. 直接显示游戏界面，无需重新输入
3. 用户信息（剩余抽奖次数、头像等）自动加载

### 退出登录
1. 点击用户名旁边的"🚪 退出"按钮
2. 确认退出后清除所有本地存储
3. 回到用户名输入界面

## 安全考虑

1. **数据存储**: 仅存储用户名，不存储敏感信息
2. **数据验证**: 每次自动登录时都会重新验证用户信息
3. **用户控制**: 用户可以随时退出登录清除本地数据
4. **浏览器兼容**: 使用标准的localStorage API，兼容所有现代浏览器

## 浏览器支持

- Chrome: ✅ 完全支持
- Firefox: ✅ 完全支持  
- Safari: ✅ 完全支持
- Edge: ✅ 完全支持
- 移动端浏览器: ✅ 完全支持

## 注意事项

1. **隐私模式**: 在浏览器隐私/无痕模式下，localStorage可能无法持久保存
2. **清除缓存**: 用户清除浏览器数据时会同时清除保存的用户名
3. **跨设备**: 本地存储仅限当前设备和浏览器，不会在不同设备间同步
4. **存储限制**: localStorage通常有5-10MB的存储限制，但用户名占用空间极小

## 测试建议

1. 测试首次登录和保存功能
2. 测试页面刷新后的自动恢复
3. 测试退出登录功能
4. 测试在隐私模式下的行为
5. 测试清除浏览器数据后的表现
