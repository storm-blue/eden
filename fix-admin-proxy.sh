#!/bin/bash

# 修复admin.html代理配置
set -e

echo "🔧 修复admin.html代理配置..."

# 检查后端服务是否运行
if ! curl -s http://localhost:5000/admin.html > /dev/null; then
    echo "⚠️ 警告: 后端服务可能未运行或admin.html不可访问"
    echo "请确保后端服务已启动: sudo systemctl status eden-backend"
fi

# 更新Nginx配置
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
    echo "✅ 已备份现有配置"
fi

# 复制新配置
sudo cp /opt/eden/nginx.conf "$NGINX_CONFIG"

# 如果是Ubuntu/Debian，确保启用站点
if [ -d "/etc/nginx/sites-available" ]; then
    sudo ln -sf "$NGINX_CONFIG" "$NGINX_ENABLED"
fi

# 测试配置
echo "🧪 测试Nginx配置..."
if sudo nginx -t; then
    echo "✅ Nginx配置语法正确"
    
    # 重启Nginx
    echo "🔄 重启Nginx..."
    sudo systemctl restart nginx
    
    # 等待服务启动
    sleep 2
    
    # 测试访问
    echo "🧪 测试admin.html访问..."
    if curl -s -o /dev/null -w "%{http_code}" http://localhost/admin.html | grep -q "200\|302"; then
        echo "✅ admin.html访问正常"
    else
        echo "❌ admin.html访问失败，检查后端服务状态"
        echo "后端服务状态:"
        sudo systemctl status eden-backend --no-pager -l
    fi
    
else
    echo "❌ Nginx配置语法错误，请检查配置文件"
    exit 1
fi

echo "🎉 admin.html代理配置完成！"
echo "🌐 访问地址: http://your-server-ip/admin.html"
echo "🔑 登录信息: admin / admin2008"

# 显示服务状态
echo ""
echo "📊 服务状态检查:"
echo "Nginx状态:"
sudo systemctl status nginx --no-pager -l | head -5

echo ""
echo "后端服务状态:"
sudo systemctl status eden-backend --no-pager -l | head -5
