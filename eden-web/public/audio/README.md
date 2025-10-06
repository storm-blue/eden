# 星星城背景音乐

## 音频文件

### 背景音乐
- **star-city-bg.mp3** - 星星城主背景音乐
  - 时长：约3分钟
  - 格式：MP3, 64kbps
  - 大小：约751KB
  - 循环播放

## 播放机制

### 简化播放系统
- **单音频文件**：只使用一个背景音乐文件
- **循环播放**：设置 `loop=true` 实现无缝循环
- **自动播放**：进入星星城后延迟播放（移动端2秒，桌面端1秒）
- **前端静态资源**：直接从前端public目录提供

### 缓存优化
- **开发环境**：Vite开发服务器直接提供
- **生产环境**：Nginx静态文件缓存（1年）
- **浏览器缓存**：`Cache-Control: public, immutable`

## 技术实现

### 前端实现
```javascript
// 简化的音乐播放
const playStarCityMusic = () => {
    if (!starCityAudioRef.current.src) {
        starCityAudioRef.current.src = starCityMusicUrl
    }
    starCityAudioRef.current.loop = true
    starCityAudioRef.current.play()
}
```

### 静态资源配置
- **文件位置**: `eden-web/public/audio/star-city-bg.mp3`
- **访问路径**: `/audio/star-city-bg.mp3`
- **Nginx缓存**: 静态文件规则 `~* \.(mp3|...)$` - 1年缓存

## 性能优化

### 简化收益
- **移除代理转发**：减少一层网络请求
- **直接访问**：前端静态资源，访问更快
- **简化架构**：不再需要后端音频配置
- **CDN友好**：易于CDN分发

### 缓存策略
- **文件大小**：约751KB（压缩后）
- **预加载**：按需加载（preload="none"）
- **缓存时长**：生产环境1年，开发环境由Vite管理

## 维护说明

### 文件管理
- 只需要维护前端的音频文件
- 如需更换音乐，直接替换 `public/audio/star-city-bg.mp3`
- 建议音频格式：MP3, 64kbps, 循环友好
- 重新构建前端即可生效

### 部署注意事项
- 前端构建时会自动包含音频文件
- 生产环境由Nginx直接提供静态文件
- 享受1年强缓存，性能优异

### 调试方法
```bash
# 开发环境
curl -I http://localhost:3000/audio/star-city-bg.mp3

# 生产环境  
curl -I http://your-domain.com/audio/star-city-bg.mp3
# 应该看到 Cache-Control: public, immutable
```
