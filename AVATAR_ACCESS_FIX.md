# Eden抽奖系统 - 头像访问配置解决方案

## 问题分析

头像文件无法访问的原因：
1. 后端Java服务在5000端口提供头像文件
2. 前端通过Nginx（80/443端口）访问
3. 头像URL包含`http://localhost:5000`，在服务器环境下无法访问

## 解决方案

### 方案1：修改后端配置文件

修改 `eden-server/src/main/resources/application.yml`：

```yaml
# 头像上传配置
avatar:
  upload:
    path: ./uploads/avatars/
  url:
    prefix: /uploads/avatars/
  server:
    # 改为使用Nginx的地址，而不是直接的5000端口
    base-url: http://your-domain.com  # 或者 https://your-domain.com
    # 如果是IP访问：base-url: http://your-server-ip
```

### 方案2：配置Nginx代理转发

在Nginx配置文件中添加头像文件的代理规则：

```nginx
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名或IP

    # 前端静态文件
    location / {
        root /path/to/eden-web/dist;  # 前端构建后的目录
        try_files $uri $uri/ /index.html;
    }

    # API接口代理到后端
    location /api/ {
        proxy_pass http://localhost:5000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 头像文件代理到后端（重要！）
    location /uploads/avatars/ {
        proxy_pass http://localhost:5000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 设置缓存头
        expires 7d;
        add_header Cache-Control "public, immutable";
    }

    # 管理后台
    location /admin.html {
        proxy_pass http://localhost:5000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 方案3：完整的生产环境配置

#### 1. 修改后端配置

```yaml
# application.yml
avatar:
  upload:
    path: ./uploads/avatars/
  url:
    prefix: /uploads/avatars/
  server:
    base-url: ${AVATAR_BASE_URL:http://localhost}  # 使用环境变量
```

#### 2. 启动后端时设置环境变量

```bash
# 设置环境变量
export AVATAR_BASE_URL=http://your-domain.com
# 或者
export AVATAR_BASE_URL=http://your-server-ip

# 启动后端服务
java -jar eden-lottery-1.0-SNAPSHOT.jar
```

#### 3. 或者在启动命令中直接指定

```bash
java -jar eden-lottery-1.0-SNAPSHOT.jar --avatar.server.base-url=http://your-domain.com
```

## 推荐的部署步骤

### 1. 修改后端配置
```bash
cd eden-server/src/main/resources
# 编辑 application.yml
vim application.yml
```

将 `avatar.server.base-url` 改为：
```yaml
avatar:
  server:
    base-url: http://your-domain.com  # 替换为实际域名或IP
```

### 2. 重新构建后端
```bash
cd eden-server
mvn clean package -DskipTests
```

### 3. 更新Nginx配置
```bash
# 编辑Nginx配置
sudo vim /etc/nginx/sites-available/eden-lottery

# 添加头像文件代理规则
location /uploads/avatars/ {
    proxy_pass http://localhost:5000;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    expires 7d;
    add_header Cache-Control "public, immutable";
}

# 重新加载Nginx配置
sudo nginx -t
sudo systemctl reload nginx
```

### 4. 重启后端服务
```bash
# 停止旧服务
sudo systemctl stop eden-lottery

# 启动新服务
sudo systemctl start eden-lottery

# 检查状态
sudo systemctl status eden-lottery
```

## 验证方法

1. **检查头像API**：
   ```bash
   curl http://your-domain.com/api/avatar/test-user
   ```

2. **检查头像文件访问**：
   ```bash
   curl -I http://your-domain.com/uploads/avatars/avatar_xxx.jpg
   ```

3. **在浏览器中测试**：
   - 上传头像
   - 检查头像是否正常显示
   - 查看浏览器网络面板，确认头像请求成功

## 常见问题

### Q: 头像上传成功但显示不出来
A: 检查Nginx是否正确代理了 `/uploads/avatars/` 路径

### Q: 头像URL包含localhost:5000
A: 修改后端配置中的 `avatar.server.base-url`

### Q: 403 Forbidden错误
A: 检查文件权限和Nginx用户权限

### Q: 文件不存在错误
A: 确认头像文件确实保存在服务器的 `./uploads/avatars/` 目录中
