# 精力系统剩余前端工作

## ✅ 后端已完成（100%）
所有后端工作已完成！✨

## 📋 前端剩余工作

### 在 LuckyWheel.jsx 中需要添加：

#### 1. 添加精力状态
```javascript
// 在组件开头添加
const [userEnergy, setUserEnergy] = useState(null) // 用户精力信息
```

#### 2. 添加获取精力的函数
```javascript
// 获取用户精力信息
const fetchUserEnergy = async () => {
    if (!userName) return
    
    try {
        const response = await fetch(`/api/user-info/${userName}/energy`)
        const result = await response.json()
        
        if (result.success) {
            console.log('获取用户精力成功:', result.data)
            setUserEnergy(result.data)
        } else {
            console.error('获取用户精力失败:', result.message)
        }
    } catch (error) {
        console.error('获取用户精力失败:', error)
    }
}
```

#### 3. 在打开魔法弹窗时获取精力
```javascript
// 修改 fetchMagics 函数
const fetchMagics = async () => {
    if (userName !== '秦小淮') return

    setLoadingMagics(true)
    try {
        const response = await fetch(`/api/magic/list?userId=${userName}`)
        const data = await response.json()
        if (data.success) {
            setMagics(data.data || [])
            // 同时获取精力信息
            fetchUserEnergy()
        } else {
            console.error('获取魔法列表失败:', data.message)
        }
    } catch (error) {
        console.error('获取魔法列表失败:', error)
    } finally {
        setLoadingMagics(false)
    }
}
```

#### 4. 修改施展魔法函数，添加精力检查
```javascript
// 修改 castMagic 函数
const castMagic = async (code) => {
    if (userName !== '秦小淮') {
        alert('只有秦小淮可以施展魔法')
        return
    }
    
    // 检查精力是否足够
    const magic = magics.find(m => m.code === code)
    if (magic && magic.energyCost && userEnergy) {
        if (userEnergy.energy < magic.energyCost) {
            alert(`精力不足！需要 ${magic.energyCost} 点精力，当前只有 ${userEnergy.energy} 点`)
            return
        }
    }
    
    setCastingMagic(code)
    try {
        const response = await fetch('/api/magic/cast', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: userName,
                code: code
            })
        })
        
        const result = await response.json()
        
        if (result.success) {
            // 刷新魔法列表和精力信息
            await fetchMagics()
            await fetchUserEnergy()
            
            // 执行特效
            if (code === 'FOOD_RAIN') {
                triggerFoodRainEffect()
            } else if (code === 'CHANGE_WEATHER') {
                triggerChangeWeatherEffect()
            } else if (code === 'BANISH_GIANT') {
                triggerBanishGiantEffect()
            }
            
            // 自动关闭魔法弹窗
            setShowMagicModal(false)
        } else {
            alert(result.message || '魔法施展失败')
        }
    } catch (error) {
        console.error('施展魔法失败:', error)
        alert('魔法施展失败: ' + error.message)
    } finally {
        setCastingMagic(null)
    }
}
```

#### 5. 传递精力信息到 MagicModal
```javascript
// 找到 MagicModal 组件的使用，添加 userEnergy 属性
<MagicModal
    show={showMagicModal}
    onClose={() => setShowMagicModal(false)}
    magics={magics}
    loading={loadingMagics}
    castingCode={castingMagic}
    onCast={castMagic}
    userEnergy={userEnergy}  // 新增
/>
```

## 🎯 实现步骤

1. 在 `/Users/g01d-01-0924/eden/eden-web/src/components/LuckyWheel.jsx` 中搜索以下内容：
   - `const [magics, setMagics]` - 在附近添加 `userEnergy` 状态
   - `const fetchMagics` - 修改以包含精力获取
   - `const castMagic` - 修改以包含精力检查
   - `<MagicModal` - 添加 `userEnergy` 属性

2. 测试精力系统：
   - 打开魔法弹窗，应该看到精力显示
   - 施展魔法，应该消耗精力
   - 精力不足时，应该无法施展魔法

## ✨ 完成后的效果

### 魔法弹窗
```
┌─────────────────────────────────────┐
│        ✨ 魔法管理                  │
│                                     │
│  ┌───────────────────────────────┐ │
│  │    ⚡ 当前精力                 │ │
│  │        15 / 15                │ │
│  │  每天凌晨12点恢复到满值         │ │
│  └───────────────────────────────┘ │
│                                     │
│  ▼ 天降食物 (3/3)   [施展魔法]      │
│    施展魔法后，将会有10000份食物... │
│    每日次数: 3                      │
│    剩余次数: 3                      │
│    ⚡ 精力消耗: 5                  │
│                                     │
│  ▼ 改变天气 (3/3)   [施展魔法]      │
│  ▼ 驱逐巨人 (1/1)   [施展魔法]      │
│                                     │
│           [关闭]                    │
└─────────────────────────────────────┘
```

### 精力不足提示
```
精力不足！需要 5 点精力，当前只有 3 点
```

### 施展魔法后
```
⚡ 当前精力: 10 / 15  (消耗了5点)
```

## 📚 API 端点

- **获取精力信息**: `GET /api/user-info/{userId}/energy`
  - Response: 
    ```json
    {
      "success": true,
      "data": {
        "energy": 15,
        "maxEnergy": 15,
        "energyRefreshTime": "2024-10-19T12:00:00"
      }
    }
    ```

- **施展魔法**: `POST /api/magic/cast`
  - Body: `{"userId": "秦小淮", "code": "FOOD_RAIN"}`
  - Response (成功):
    ```json
    {
      "success": true,
      "message": "魔法施展成功"
    }
    ```
  - Response (精力不足):
    ```json
    {
      "success": false,
      "message": "精力不足！需要 5 点精力，当前只有 3 点"
    }
    ```

## 🚀 准备好测试了吗？

所有后端代码已完成！只需要按照上面的步骤修改 LuckyWheel.jsx，就可以测试精力系统了！🎉

