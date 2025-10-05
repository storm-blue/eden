#!/bin/bash

# Eden抽奖系统 - 修复头像上传413错误
# 使用方法: ./fix-upload-413.sh

set -e

echo "🔧 修复头像上传413错误 (Request Entity Too Large)"

# 1. 备份当前Nginx配置
if [ -f "/etc/nginx/sites-available/eden-lottery" ]; then
    echo "📦 备份当前Nginx配置..."
    sudo cp /etc/nginx/sites-available/eden-lottery /etc/nginx/sites-available/eden-lottery.backup.$(date +%Y%m%d_%H%M%S)
    echo "✅ 备份完成"
fi

# 2. 检查并修复Nginx配置
echo "🔍 检查Nginx配置中的client_max_body_size设置..."

NGINX_CONFIG="/etc/nginx/sites-available/eden-lottery"

if ! sudo grep -q "client_max_body_size" "$NGINX_CONFIG"; then
    echo "⚠️  发现问题：缺少client_max_body_size配置"
    echo "🔧 正在修复..."
    
    # 在server块开始后添加client_max_body_size
    sudo sed -i '/server {/a\    # 设置客户端请求体大小限制（重要！）\n    client_max_body_size 10M;' "$NGINX_CONFIG"
    
    echo "✅ 已添加client_max_body_size 10M配置"
else
    echo "✅ client_max_body_size配置已存在"
fi

# 3. 检查并优化API代理配置
echo "🔍 检查API代理缓冲配置..."

if ! sudo grep -q "proxy_request_buffering off" "$NGINX_CONFIG"; then
    echo "🔧 添加文件上传优化配置..."
    
    # 在/api/的location块中添加缓冲优化
    sudo sed -i '/location \/api\// {
        :a
        n
        /proxy_read_timeout/a\        \n        # 针对文件上传API，设置更大的缓冲区\n        proxy_request_buffering off;\n        proxy_buffering off;
        ba
    }' "$NGINX_CONFIG"
    
    echo "✅ 已添加文件上传优化配置"
fi

# 4. 测试Nginx配置
echo "🧪 测试Nginx配置..."
if sudo nginx -t; then
    echo "✅ Nginx配置测试通过"
else
    echo "❌ Nginx配置测试失败，请检查配置文件"
    exit 1
fi

# 5. 重新加载Nginx
echo "🔄 重新加载Nginx..."
sudo systemctl reload nginx
echo "✅ Nginx重新加载完成"

# 6. 检查后端配置
echo "🔍 检查后端文件上传配置..."
BACKEND_CONFIG="eden-server/src/main/resources/application.yml"

if [ -f "$BACKEND_CONFIG" ]; then
    if grep -q "max-file-size: 5MB" "$BACKEND_CONFIG"; then
        echo "✅ 后端文件大小限制: 5MB"
    else
        echo "⚠️  后端配置可能需要检查"
    fi
else
    echo "⚠️  未找到后端配置文件"
fi

# 7. 显示当前配置状态
echo ""
echo "📋 当前配置状态:"
echo "   🌐 Nginx文件大小限制: 10MB"
echo "   ☕ 后端文件大小限制: 5MB"
echo "   🔧 代理缓冲: 已优化"
echo ""

# 8. 测试建议
echo "🧪 测试建议:"
echo "   1. 尝试上传小于5MB的图片文件"
echo "   2. 如果仍然出现413错误，检查以下内容:"
echo "      - 确认图片文件确实小于5MB"
echo "      - 查看Nginx错误日志: sudo tail -f /var/log/nginx/error.log"
echo "      - 查看后端日志: sudo journalctl -u eden-lottery -f"
echo ""

# 9. 常见问题排查
echo "🔧 如果问题仍然存在:"
echo "   1. 检查主Nginx配置是否有全局限制:"
echo "      sudo grep -r client_max_body_size /etc/nginx/"
echo ""
echo "   2. 检查系统级别限制:"
echo "      ulimit -a"
echo ""
echo "   3. 重启服务 (如果需要):"
echo "      sudo systemctl restart nginx"
echo "      sudo systemctl restart eden-lottery"
echo ""

echo "🎉 修复脚本执行完成！"
echo "💡 现在可以尝试重新上传头像了"
