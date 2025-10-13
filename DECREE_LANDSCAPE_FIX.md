# 命令弹窗强制横屏修复

## 问题

命令管理弹窗在星星城（强制横屏环境）中显示方向不正确。

## 解决方案

为命令弹窗添加强制横屏样式，与星星城的其他弹窗保持一致。

## 实现细节

### 1. 添加CSS类

**DecreeModal.jsx**:
```jsx
// 之前
<div className="decree-modal-overlay" onClick={onClose}>

// 现在
<div className="decree-modal-overlay force-landscape" onClick={onClose}>
```

### 2. 添加强制横屏CSS

**DecreeModal.css**:
```css
/* 移动端强制横屏样式 */
@media screen and (max-width: 768px) {
    .decree-modal-overlay.force-landscape {
        position: fixed !important;
        top: 50% !important;
        left: 50% !important;
        transform: translate(-50%, -50%) rotate(90deg) !important;
        transform-origin: center center !important;
        width: 100vh !important;
        height: 100vw !important;
        max-width: 100vh !important;
        max-height: 100vw !important;
        z-index: 100001 !important;
    }

    /* 重置弹窗内容，防止继承父级transform */
    .decree-modal-overlay.force-landscape .decree-modal-content {
        position: relative !important;
        max-width: 650px !important;
        width: 95% !important;
        max-height: 80vh !important;
        padding: 20px !important;
        margin: 0 auto !important;
        transform: none !important;
    }

    /* 调整各元素的字体大小和间距 */
    .decree-modal-overlay.force-landscape .decree-modal-title {
        font-size: 22px !important;
        margin-bottom: 18px !important;
    }

    .decree-modal-overlay.force-landscape .decree-item {
        padding: 15px !important;
    }

    .decree-modal-overlay.force-landscape .decree-name {
        font-size: 18px !important;
    }

    .decree-modal-overlay.force-landscape .decree-description {
        font-size: 13px !important;
    }

    /* 确保所有元素不会被额外旋转 */
    .decree-modal-overlay.force-landscape * {
        max-width: none !important;
        max-height: none !important;
    }
}
```

## 工作原理

### 横屏转换机制

1. **旋转容器**：
   - `transform: rotate(90deg)` - 将整个弹窗容器旋转90度
   - `width: 100vh` / `height: 100vw` - 交换宽高，适应横屏

2. **重置内容**：
   - `transform: none` - 确保内容本身不被旋转
   - 调整字体大小和间距以适应横屏布局

3. **z-index层级**：
   - `z-index: 100001` - 确保在星星城容器之上显示

## 效果

- ✅ 移动端竖屏访问星星城时，命令弹窗自动横屏显示
- ✅ 内容保持正确方向，不会被旋转
- ✅ 布局紧凑，适合横屏显示
- ✅ 与其他弹窗（居住选择、捐献等）显示效果一致

## 参考

该实现参考了项目中其他弹窗的强制横屏样式：
- `.donation-modal-overlay.force-landscape`
- `.residence-modal-overlay.force-landscape`

---

✅ 命令弹窗现在完美支持星星城的强制横屏模式！

