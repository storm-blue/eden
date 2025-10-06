# 管理界面架构说明

## 📋 **项目结构澄清**

### ✅ **实际使用的管理界面**

**文件位置**：`eden-server/src/main/resources/static/admin.html`

**访问方式**：`http://localhost:5000/admin.html`

**说明**：
- 由 Spring Boot 后端作为静态资源提供服务
- 包含完整的管理功能和居所事件管理
- 这是生产环境和开发环境实际使用的管理界面

### ✅ **项目架构**

```
eden/
├── eden-server/                 # Spring Boot 后端
│   └── src/main/resources/static/
│       └── admin.html          # ✅ 实际使用的管理界面
└── eden-web/                   # React/Vite 前端项目
    ├── src/                    # React 组件和源码
    ├── public/                 # 静态资源
    └── index.html              # 主应用入口
```

### 🗑️ **已清理的文件**

- ~~`eden-web/admin.html`~~ (已删除，避免混淆)

### 🌐 **访问说明**

1. **主应用**：React/Vite 前端应用
   - 开发环境：`http://localhost:3000` (Vite dev server)
   - 生产环境：通过 Nginx 代理访问

2. **管理界面**：Spring Boot 静态资源
   - 访问地址：`http://localhost:5000/admin.html`
   - 登录信息：admin / admin2008

### 🔧 **部署配置**

**Nginx 配置**：
```nginx
# 主应用 - 前端静态文件
location / {
    root /var/www/eden;
    try_files $uri $uri/ /index.html;
}

# 管理后台 - 转发到后端
location /admin.html {
    proxy_pass http://localhost:5000/admin.html;
}

# API接口 - 转发到后端
location /api/ {
    proxy_pass http://localhost:5000/api/;
}
```

## 🎯 **总结**

- **主应用**：React 前端 + Spring Boot API
- **管理界面**：Spring Boot 静态资源
- **架构清晰**：前后端分离，管理界面独立
- **访问简单**：直接通过后端端口访问管理界面

这样的架构避免了管理界面的复杂构建过程，同时保持了功能的完整性。
