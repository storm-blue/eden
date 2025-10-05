#!/bin/bash

# Eden抽奖系统 - 头像访问问题修复部署脚本
# 使用方法: ./fix-avatar-access.sh your-domain.com

set -e

# 检查参数
if [ -z "$1" ]; then
    echo "使用方法: $0 <域名或IP地址>"
    echo "例如: $0 example.com"
    echo "例如: $0 192.168.1.100"
    exit 1
fi

DOMAIN_OR_IP=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "🚀 开始修复Eden抽奖系统头像访问问题..."
echo "📍 域名/IP: $DOMAIN_OR_IP"

# 1. 重新构建后端（如果需要）
echo "📦 重新构建后端服务..."
cd "$SCRIPT_DIR/eden-server"
if [ -f "pom.xml" ]; then
    mvn clean package -DskipTests
    echo "✅ 后端构建完成"
else
    echo "⚠️  未找到pom.xml，跳过后端构建"
fi

# 2. 更新Nginx配置
echo "🔧 配置Nginx..."

# 创建Nginx配置文件
NGINX_CONFIG="/etc/nginx/sites-available/eden-lottery"
sudo tee "$NGINX_CONFIG" > /dev/null << EOF
server {
    listen 80;
    server_name $DOMAIN_OR_IP;

    # 前端静态文件
    location / {
        root /var/www/eden-web;
        try_files \$uri \$uri/ /index.html;
        
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header X-XSS-Protection "1; mode=block" always;
    }

    # API接口代理到后端Java服务
    location /api/ {
        proxy_pass http://localhost:5000;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 头像文件代理到后端（修复头像访问问题的关键）
    location /uploads/avatars/ {
        proxy_pass http://localhost:5000;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        expires 7d;
        add_header Cache-Control "public, immutable";
        add_header Access-Control-Allow-Origin "*";
    }

    # 管理后台页面
    location /admin.html {
        proxy_pass http://localhost:5000;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    location ~* \.(jpg|jpeg|png|gif|ico|css|js)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    error_page 404 /index.html;
    access_log /var/log/nginx/eden-lottery.access.log;
    error_log /var/log/nginx/eden-lottery.error.log;
}
EOF

# 启用站点
sudo ln -sf "$NGINX_CONFIG" /etc/nginx/sites-enabled/
sudo rm -f /etc/nginx/sites-enabled/default

# 测试Nginx配置
echo "🔍 测试Nginx配置..."
sudo nginx -t

if [ $? -eq 0 ]; then
    echo "✅ Nginx配置测试通过"
    sudo systemctl reload nginx
    echo "✅ Nginx重新加载完成"
else
    echo "❌ Nginx配置测试失败，请检查配置文件"
    exit 1
fi

# 3. 构建前端（如果需要）
echo "🎨 构建前端..."
cd "$SCRIPT_DIR/eden-web"
if [ -f "package.json" ]; then
    npm run build
    
    # 部署前端文件
    sudo rm -rf /var/www/eden-web
    sudo mkdir -p /var/www/eden-web
    sudo cp -r dist/* /var/www/eden-web/
    sudo chown -R www-data:www-data /var/www/eden-web
    echo "✅ 前端部署完成"
else
    echo "⚠️  未找到package.json，跳过前端构建"
fi

# 4. 重启后端服务
echo "🔄 重启后端服务..."

# 设置环境变量并重启服务
if systemctl is-active --quiet eden-lottery; then
    sudo systemctl stop eden-lottery
fi

# 创建或更新systemd服务文件
sudo tee /etc/systemd/system/eden-lottery.service > /dev/null << EOF
[Unit]
Description=Eden Lottery Backend Service
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=$SCRIPT_DIR/eden-server
Environment=AVATAR_BASE_URL=http://$DOMAIN_OR_IP
ExecStart=/usr/bin/java -jar target/eden-lottery-1.0-SNAPSHOT.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable eden-lottery
sudo systemctl start eden-lottery

# 等待服务启动
echo "⏳ 等待后端服务启动..."
sleep 10

# 5. 验证部署
echo "🔍 验证部署结果..."

# 检查后端服务状态
if systemctl is-active --quiet eden-lottery; then
    echo "✅ 后端服务运行正常"
else
    echo "❌ 后端服务未正常启动"
    sudo systemctl status eden-lottery
    exit 1
fi

# 检查Nginx状态
if systemctl is-active --quiet nginx; then
    echo "✅ Nginx服务运行正常"
else
    echo "❌ Nginx服务未正常启动"
    sudo systemctl status nginx
    exit 1
fi

# 测试API访问
echo "🧪 测试API访问..."
if curl -f -s "http://localhost/api/user/test" > /dev/null; then
    echo "✅ API访问正常"
else
    echo "⚠️  API访问测试失败，请检查后端服务"
fi

echo ""
echo "🎉 头像访问问题修复完成！"
echo ""
echo "📋 部署信息:"
echo "   🌐 网站地址: http://$DOMAIN_OR_IP"
echo "   🔧 管理后台: http://$DOMAIN_OR_IP/admin.html"
echo "   📸 头像访问: http://$DOMAIN_OR_IP/uploads/avatars/"
echo ""
echo "📝 注意事项:"
echo "   1. 确保防火墙开放80端口"
echo "   2. 如果使用域名，确保DNS解析正确"
echo "   3. 头像文件现在通过Nginx代理访问，无需直接访问5000端口"
echo ""
echo "🔧 如需查看日志:"
echo "   后端日志: sudo journalctl -u eden-lottery -f"
echo "   Nginx日志: sudo tail -f /var/log/nginx/eden-lottery.error.log"
