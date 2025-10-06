# 星星城背景音乐

## 音频文件

### 背景音乐
- **star-city-bg.mp3** - 星星城主背景音乐
  - 时长：约3分钟
  - 格式：MP3, 64kbps
  - 循环播放

## 播放机制

### 简化播放系统
- **单音频文件**：只使用一个背景音乐文件
- **循环播放**：设置 `loop=true` 实现无缝循环
- **自动播放**：进入星星城后延迟播放（移动端2秒，桌面端1秒）
- **预加载优化**：进入页面时预加载音频文件

### HTTP缓存优化
- **缓存时长**：30天
- **缓存策略**：公共缓存，必须重新验证
- **开发环境**：通过Vite代理转发到后端
- **生产环境**：通过Nginx代理，启用30天缓存

## 技术实现

### 前端实现
```javascript
// 简化的音乐播放
const playStarCityMusic = () => {
    starCityAudioRef.current.src = starCityMusicUrl
    starCityAudioRef.current.loop = true
    starCityAudioRef.current.play()
}
```

### 后端配置
- **Spring Boot**: `/audio/**` 路径映射，30天缓存
- **Nginx**: 代理转发，启用缓存头
- **文件位置**: `eden-server/src/main/resources/static/audio/`

## 性能优化

### CPU优化收益
- **移除随机选择**：不再使用Math.random()选择音乐
- **移除状态管理**：删除musicCacheStatus和currentMusicIndex
- **简化事件处理**：onEnded基本不会触发（因为loop=true）
- **减少内存占用**：只需要加载一个音频文件

### 网络优化
- **文件大小**：约751KB（压缩后）
- **预加载**：进入星星城时预加载
- **缓存策略**：30天强缓存，减少重复下载

## 维护说明

### 文件管理
- 只需要维护一个音频文件
- 如需更换音乐，直接替换 `star-city-bg.mp3`
- 建议音频格式：MP3, 64kbps, 循环友好

### 调试方法
```bash
# 检查音频文件
curl -I http://localhost:5000/audio/star-city-bg.mp3

# 查看缓存状态
# 浏览器开发者工具 → Network → 查看Cache-Control头
```
