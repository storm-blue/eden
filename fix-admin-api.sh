#!/bin/bash

# 修复管理后台API访问问题
set -e

echo "🔧 修复管理后台API访问问题..."

# 1. 重新构建后端（包含修复后的admin.html）
echo "🏗️ 重新构建后端..."
cd /opt/eden/eden-server

# 检查Maven
if command -v mvn &> /dev/null; then
    mvn clean package -DskipTests
elif [ -f "mvnw" ]; then
    ./mvnw clean package -DskipTests
else
    echo "❌ 未找到Maven，请先安装Maven"
    exit 1
fi

echo "✅ 后端重新构建完成"

# 2. 更新Nginx配置
echo "📝 更新Nginx配置..."

# 检查Nginx配置目录
if [ -d "/etc/nginx/sites-available" ]; then
    # Ubuntu/Debian
    NGINX_CONFIG="/etc/nginx/sites-available/eden"
    NGINX_ENABLED="/etc/nginx/sites-enabled/eden"
else
    # CentOS/RHEL
    NGINX_CONFIG="/etc/nginx/conf.d/eden.conf"
fi

# 备份现有配置
if [ -f "$NGINX_CONFIG" ]; then
    sudo cp "$NGINX_CONFIG" "$NGINX_CONFIG.backup.$(date +%Y%m%d_%H%M%S)"
    echo "✅ 已备份现有Nginx配置"
fi

# 复制新配置
sudo cp /opt/eden/nginx.conf "$NGINX_CONFIG"

# 如果是Ubuntu/Debian，确保启用站点
if [ -d "/etc/nginx/sites-available" ]; then
    sudo ln -sf "$NGINX_CONFIG" "$NGINX_ENABLED"
fi

# 3. 测试Nginx配置
echo "🧪 测试Nginx配置..."
if sudo nginx -t; then
    echo "✅ Nginx配置语法正确"
else
    echo "❌ Nginx配置语法错误"
    exit 1
fi

# 4. 重启服务
echo "🔄 重启服务..."

# 重启后端服务
sudo systemctl restart eden-backend
echo "✅ 后端服务已重启"

# 重启Nginx
sudo systemctl restart nginx
echo "✅ Nginx已重启"

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 5

# 5. 验证服务状态
echo "📊 验证服务状态..."

# 检查后端服务
if sudo systemctl is-active --quiet eden-backend; then
    echo "✅ 后端服务运行正常"
else
    echo "❌ 后端服务启动失败"
    sudo systemctl status eden-backend --no-pager -l
    exit 1
fi

# 检查Nginx
if sudo systemctl is-active --quiet nginx; then
    echo "✅ Nginx运行正常"
else
    echo "❌ Nginx启动失败"
    sudo systemctl status nginx --no-pager -l
    exit 1
fi

# 6. 测试API访问
echo "🧪 测试API访问..."

# 测试主应用
if curl -s -o /dev/null -w "%{http_code}" http://localhost/ | grep -q "200"; then
    echo "✅ 主应用访问正常"
else
    echo "⚠️ 主应用访问可能有问题"
fi

# 测试admin.html
if curl -s -o /dev/null -w "%{http_code}" http://localhost/admin.html | grep -q "200"; then
    echo "✅ 管理后台页面访问正常"
else
    echo "❌ 管理后台页面访问失败"
fi

# 测试API代理
if curl -s -o /dev/null -w "%{http_code}" http://localhost/api/user/test | grep -q "200\|404"; then
    echo "✅ API代理工作正常"
else
    echo "❌ API代理可能有问题"
fi

echo ""
echo "🎉 修复完成！"
echo "🌐 现在可以通过以下地址访问："
echo "   主应用: http://your-server-ip/"
echo "   管理后台: http://your-server-ip/admin.html"
echo "   登录信息: admin / admin2008"
echo ""
echo "📋 API调用现在使用相对路径 '/api'，会自动通过Nginx代理到后端"

# 显示服务器IP（如果可以获取）
SERVER_IP=$(hostname -I | awk '{print $1}' 2>/dev/null || echo "your-server-ip")
if [ "$SERVER_IP" != "your-server-ip" ]; then
    echo "🔗 您的服务器IP: $SERVER_IP"
    echo "   访问地址: http://$SERVER_IP/admin.html"
fi
