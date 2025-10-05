# 本地开发环境代理配置

## 🔍 问题分析

### 错误现象
```
Request URL: http://127.0.0.1:3001/uploads/avatars/avatar_xxx.jpg
Status Code: 404 Not Found
```

### 问题原因
1. **前端开发服务器**: Vite运行在 `3001` 端口
2. **后端服务器**: Java服务运行在 `5000` 端口
3. **头像文件存储**: 文件保存在后端服务器的 `./uploads/avatars/` 目录
4. **代理缺失**: Vite只代理了 `/api` 路径，没有代理 `/uploads` 路径

## ✅ 解决方案

### 修改 `vite.config.js`
```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:5000',
        changeOrigin: true,
        secure: false
      },
      // 新增：代理上传文件路径
      '/uploads': {
        target: 'http://localhost:5000',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
```

## 🔄 数据流程

### 修改前（❌ 失败）
```
浏览器 → http://127.0.0.1:3001/uploads/avatars/xxx.jpg
       ↓
    Vite服务器 → 404 (没有这个文件)
```

### 修改后（✅ 成功）
```
浏览器 → http://127.0.0.1:3001/uploads/avatars/xxx.jpg
       ↓
    Vite代理 → http://localhost:5000/uploads/avatars/xxx.jpg
       ↓
    Java后端 → 返回实际文件
```

## 🧪 测试验证

### 1. 重启开发服务器
```bash
cd eden-web
npm run dev
```

### 2. 测试头像上传
1. 打开浏览器访问 `http://localhost:3001`
2. 输入用户名登录
3. 点击头像上传功能
4. 查看上传后的头像是否正常显示

### 3. 验证代理工作
```bash
# 直接访问后端
curl http://localhost:5000/uploads/avatars/avatar_xxx.jpg

# 通过Vite代理访问
curl http://localhost:3001/uploads/avatars/avatar_xxx.jpg
```

## 📊 环境对比

| 环境 | 前端地址 | 后端地址 | 代理配置 |
|------|----------|----------|----------|
| **开发环境** | localhost:3001 | localhost:5000 | Vite代理 |
| **生产环境** | your-domain.com | your-domain.com:5000 | Nginx代理 |

## 🔧 完整的开发环境配置

### vite.config.js（完整版）
```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    host: true,
    proxy: {
      // API接口代理
      '/api': {
        target: 'http://localhost:5000',
        changeOrigin: true,
        secure: false,
        configure: (proxy, options) => {
          proxy.on('error', (err, req, res) => {
            console.log('API代理错误:', err);
          });
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('API代理请求:', req.method, req.url);
          });
        }
      },
      // 上传文件代理
      '/uploads': {
        target: 'http://localhost:5000',
        changeOrigin: true,
        secure: false,
        configure: (proxy, options) => {
          proxy.on('error', (err, req, res) => {
            console.log('文件代理错误:', err);
          });
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('文件代理请求:', req.method, req.url);
          });
        }
      },
      // 管理后台代理
      '/admin.html': {
        target: 'http://localhost:5000',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: true
  }
})
```

## 🚨 常见问题排查

### 1. 代理不生效
```bash
# 确保重启了开发服务器
npm run dev

# 检查控制台是否有代理日志
```

### 2. 仍然404错误
```bash
# 检查后端服务是否运行
curl http://localhost:5000/api/user/test

# 检查文件是否确实存在
ls -la eden-server/uploads/avatars/
```

### 3. CORS错误
```bash
# 后端已配置CORS，通常不会有此问题
# 如果出现，检查后端的@CrossOrigin注解
```

## 📝 开发流程

### 启动顺序
1. **启动后端服务**
   ```bash
   cd eden-server
   java -jar target/eden-lottery-1.0-SNAPSHOT.jar
   ```

2. **启动前端开发服务器**
   ```bash
   cd eden-web
   npm run dev
   ```

3. **访问应用**
   ```
   http://localhost:3001  # 注意是3001端口，不是3000
   ```

### 调试技巧
1. **查看Network面板**: 确认请求是否正确代理
2. **查看Console**: 查看是否有代理日志
3. **直接访问后端**: 确认后端文件确实存在

## 🎯 总结

通过在 `vite.config.js` 中添加 `/uploads` 路径的代理配置，现在本地开发环境可以：

✅ **正确代理API请求** (`/api` → `localhost:5000`)  
✅ **正确代理文件请求** (`/uploads` → `localhost:5000`)  
✅ **保持开发体验一致** (与生产环境行为一致)  
✅ **支持热重载** (不影响Vite的开发特性)  

修改后需要重启开发服务器才能生效！🚀
