# Eden转盘抽奖系统 Linux 部署指南

## 📋 系统要求

- **操作系统**: Ubuntu 20.04+ / CentOS 7+ / RHEL 8+
- **内存**: 至少 2GB RAM
- **存储**: 至少 5GB 可用空间
- **网络**: 开放 80 端口（HTTP）和 443 端口（HTTPS，可选）

## 🚀 快速部署

### 1. 环境准备

```bash
# 下载并运行环境准备脚本
sudo bash deploy.sh
```

### 2. 上传项目文件

```bash
# 将项目文件上传到服务器
scp -r eden/ user@your-server:/opt/

# 或使用git克隆
cd /opt
sudo git clone https://github.com/your-repo/eden.git
sudo chown -R $USER:$USER eden/
```

### 3. 构建和部署

```bash
# 运行构建部署脚本
sudo bash /opt/eden/build-and-deploy.sh
```

### 4. 配置Nginx

```bash
# 复制Nginx配置
sudo cp /opt/eden/nginx.conf /etc/nginx/sites-available/eden
sudo ln -s /etc/nginx/sites-available/eden /etc/nginx/sites-enabled/

# 修改域名配置
sudo nano /etc/nginx/sites-available/eden
# 将 your-domain.com 替换为你的域名或服务器IP

# 测试配置并重启Nginx
sudo nginx -t
sudo systemctl restart nginx
```

### 5. 配置系统服务

```bash
# 创建eden用户
sudo useradd -r -s /bin/false eden
sudo chown -R eden:eden /opt/eden

# 创建数据目录
sudo mkdir -p /opt/eden/data
sudo chown -R eden:eden /opt/eden/data

# 安装系统服务
sudo cp /opt/eden/eden-backend.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable eden-backend
sudo systemctl start eden-backend
```

### 6. 验证部署

```bash
# 检查后端服务状态
sudo systemctl status eden-backend

# 检查日志
sudo journalctl -u eden-backend -f

# 检查端口监听
sudo netstat -tlnp | grep :5000

# 访问应用
curl http://localhost/
curl http://localhost/api/user/test
```

## 🔧 手动部署步骤

### 前端部署

```bash
cd /opt/eden/eden-web

# 安装依赖
npm install

# 构建生产版本
npm run build

# 部署到Nginx
sudo mkdir -p /var/www/eden
sudo cp -r dist/* /var/www/eden/
sudo chown -R www-data:www-data /var/www/eden
```

### 后端部署

```bash
cd /opt/eden/eden-server

# 复制生产配置
cp /opt/eden/production-config.yml src/main/resources/application-prod.yml

# 构建JAR包
mvn clean package -DskipTests

# 或使用Maven Wrapper
./mvnw clean package -DskipTests

# 启动服务
java -jar target/eden-lottery-server-1.0.0.jar --spring.profiles.active=prod
```

## 🛠️ 管理命令

### 服务管理

```bash
# 启动服务
sudo systemctl start eden-backend

# 停止服务
sudo systemctl stop eden-backend

# 重启服务
sudo systemctl restart eden-backend

# 查看状态
sudo systemctl status eden-backend

# 查看日志
sudo journalctl -u eden-backend -f
```

### 手动管理

```bash
# 启动后端
/opt/eden/start-backend.sh

# 停止后端
/opt/eden/stop-backend.sh

# 重启Nginx
sudo systemctl restart nginx
```

## 📊 监控和维护

### 日志位置

- **后端日志**: `/var/log/eden/backend.log`
- **Nginx访问日志**: `/var/log/nginx/eden_access.log`
- **Nginx错误日志**: `/var/log/nginx/eden_error.log`
- **系统日志**: `sudo journalctl -u eden-backend`

### 数据库备份

```bash
# 备份SQLite数据库
sudo cp /opt/eden/data/eden_lottery.db /opt/eden/backup/eden_lottery_$(date +%Y%m%d_%H%M%S).db

# 创建定时备份
sudo crontab -e
# 添加: 0 2 * * * cp /opt/eden/data/eden_lottery.db /opt/eden/backup/eden_lottery_$(date +\%Y\%m\%d_\%H\%M\%S).db
```

### 性能监控

```bash
# 查看系统资源使用
htop

# 查看Java进程
jps -l

# 查看端口使用
sudo netstat -tlnp | grep :5000
sudo netstat -tlnp | grep :80
```

## 🔒 安全配置

### 防火墙设置

```bash
# Ubuntu/Debian
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable

# CentOS/RHEL
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --reload
```

### SSL证书配置（可选）

```bash
# 使用Let's Encrypt
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 🐛 故障排除

### 常见问题

1. **后端无法启动**
   ```bash
   # 检查Java版本
   java -version
   
   # 检查端口占用
   sudo lsof -i :5000
   
   # 查看详细错误
   sudo journalctl -u eden-backend -n 50
   ```

2. **前端无法访问**
   ```bash
   # 检查Nginx状态
   sudo systemctl status nginx
   
   # 检查配置语法
   sudo nginx -t
   
   # 查看错误日志
   sudo tail -f /var/log/nginx/eden_error.log
   ```

3. **数据库问题**
   ```bash
   # 检查数据库文件权限
   ls -la /opt/eden/data/
   
   # 检查SQLite版本
   sqlite3 --version
   ```

### 重新部署

```bash
# 停止服务
sudo systemctl stop eden-backend

# 重新构建
cd /opt/eden
sudo bash build-and-deploy.sh

# 重启服务
sudo systemctl start eden-backend
sudo systemctl restart nginx
```

## 📞 技术支持

如果遇到部署问题，请检查：

1. 系统日志：`sudo journalctl -u eden-backend`
2. 应用日志：`/var/log/eden/backend.log`
3. Nginx日志：`/var/log/nginx/eden_error.log`
4. 网络连接：`curl http://localhost:5000/api/user/test`

---

🎉 **部署完成后，访问 `http://your-server-ip` 即可使用Eden转盘抽奖系统！**
