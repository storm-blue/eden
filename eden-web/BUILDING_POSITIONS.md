# 星星城建筑位置配置说明

## 功能说明

星星城中的5个建筑物（城堡、市政厅、行宫、小白鸽家、公园）的位置会根据星星城的等级自动调整。这样可以让不同等级的星星城背景图片有不同的建筑布局。

## 配置位置

建筑位置配置在 `LuckyWheel.jsx` 的 `getBuildingPositions()` 函数中：

```javascript
// 根据星星城等级获取建筑位置配置
const getBuildingPositions = (level) => {
    // 默认位置（LV1-LV2）
    const defaultPositions = {
        castle: {top: '23%', left: '48%'},
        city_hall: {top: '12%', left: '72%'},
        palace: {top: '8%', left: '23%'},
        white_dove_house: {top: '31%', left: '61%'},
        park: {top: '50%', left: '40%'}
    }

    // LV3-LV4 位置
    const lv3Positions = {
        castle: {top: '25%', left: '50%'},
        city_hall: {top: '15%', left: '70%'},
        palace: {top: '10%', left: '25%'},
        white_dove_house: {top: '35%', left: '63%'},
        park: {top: '52%', left: '38%'}
    }

    // LV5+ 位置
    const lv5Positions = {
        castle: {top: '28%', left: '52%'},
        city_hall: {top: '18%', left: '68%'},
        palace: {top: '12%', left: '28%'},
        white_dove_house: {top: '38%', left: '65%'},
        park: {top: '55%', left: '35%'}
    }

    // 根据等级返回对应位置
    if (level >= 5) {
        return lv5Positions
    } else if (level >= 3) {
        return lv3Positions
    } else {
        return defaultPositions
    }
}
```

## 建筑类型

| 建筑代码 | 建筑名称 | 图标 |
|---------|---------|------|
| castle | 城堡 | 🏰 |
| city_hall | 市政厅 | 🏛️ |
| palace | 行宫 | 🏯 |
| white_dove_house | 小白鸽家 | 🕊️ |
| park | 公园 | 🌳 |

## 等级分段

- **LV1-LV2**: 使用 `defaultPositions`
- **LV3-LV4**: 使用 `lv3Positions`
- **LV5+**: 使用 `lv5Positions`

## 如何调整建筑位置

### 1. 查看当前背景图

不同等级的背景图文件位置：
- `eden-web/public/picture/lv1.jpg` - LV1
- `eden-web/public/picture/lv2.jpg` - LV2
- `eden-web/public/picture/lv3.jpg` - LV3
- 等等...

### 2. 确定建筑在图片中的位置

- **top**: 建筑从上到下的位置百分比（0%-100%）
- **left**: 建筑从左到右的位置百分比（0%-100%）

### 3. 修改配置

在 `LuckyWheel.jsx` 中找到对应等级的位置配置，修改百分比值。

例如，如果LV5的城堡在图片中心偏上，可以这样设置：
```javascript
const lv5Positions = {
    castle: {top: '30%', left: '50%'},  // 30%表示距离顶部30%，50%表示水平居中
    // ... 其他建筑
}
```

### 4. 添加新等级段

如果需要为更高等级添加不同的位置配置，可以在 `getBuildingPositions()` 函数中添加新的条件：

```javascript
// LV10+ 位置
const lv10Positions = {
    castle: {top: '35%', left: '55%'},
    // ... 其他建筑
}

// 在返回逻辑中添加
if (level >= 10) {
    return lv10Positions
} else if (level >= 5) {
    return lv5Positions
} else if (level >= 3) {
    return lv3Positions
} else {
    return defaultPositions
}
```

## 注意事项

1. **百分比单位**: 位置使用百分比（如 '50%'），这样在不同屏幕尺寸下都能保持相对位置
2. **中心点对齐**: 建筑白圈使用 `transform: translate(-50%, -50%)`，所以 top/left 指向的是建筑的中心点
3. **居民头像**: 居民头像会自动显示在建筑白圈下方18px处，无需单独配置
4. **useMemo 优化**: 位置配置使用了 `useMemo`，只在等级变化时重新计算，性能优化

## 测试

修改配置后：
1. 重启前端服务（如果使用热重载，保存后会自动更新）
2. 在星星城页面查看建筑位置是否正确
3. 可以通过管理后台修改星星城等级来测试不同等级的位置配置

## 相关代码位置

- **位置配置函数**: `LuckyWheel.jsx` 第251-293行
- **建筑白圈渲染**: `LuckyWheel.jsx` 第2230-2490行左右
- **居民头像渲染**: `LuckyWheel.jsx` 第1344-1400行左右

