#!/bin/bash

echo "🔧 Nginx音频缓存配置部署脚本"
echo "================================"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. 检查nginx配置文件语法...${NC}"
if sudo nginx -t -c $(pwd)/nginx.conf; then
    echo -e "${GREEN}✅ nginx配置文件语法正确${NC}"
else
    echo -e "${RED}❌ nginx配置文件语法错误，请检查配置${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}2. 检查音频转发配置...${NC}"
if grep -q "location /audio/" nginx.conf; then
    echo -e "${GREEN}✅ 音频转发配置已添加${NC}"
    grep -A 10 "location /audio/" nginx.conf
else
    echo -e "${RED}❌ 音频转发配置未找到${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}3. 部署配置选项：${NC}"
echo "选择部署方式："
echo "1) 复制到 /etc/nginx/sites-available/eden"
echo "2) 复制到 /etc/nginx/conf.d/eden.conf"
echo "3) 仅显示配置内容（手动部署）"
echo "4) 跳过部署"

read -p "请选择 (1-4): " choice

case $choice in
    1)
        echo -e "${BLUE}复制配置到 /etc/nginx/sites-available/eden...${NC}"
        sudo cp nginx.conf /etc/nginx/sites-available/eden
        sudo ln -sf /etc/nginx/sites-available/eden /etc/nginx/sites-enabled/eden
        echo -e "${GREEN}✅ 配置已部署到 sites-available${NC}"
        ;;
    2)
        echo -e "${BLUE}复制配置到 /etc/nginx/conf.d/eden.conf...${NC}"
        sudo cp nginx.conf /etc/nginx/conf.d/eden.conf
        echo -e "${GREEN}✅ 配置已部署到 conf.d${NC}"
        ;;
    3)
        echo -e "${BLUE}配置文件内容：${NC}"
        echo "请手动将以下内容添加到您的nginx配置中："
        echo "----------------------------------------"
        cat nginx.conf
        echo "----------------------------------------"
        ;;
    4)
        echo -e "${YELLOW}跳过配置部署${NC}"
        ;;
    *)
        echo -e "${RED}无效选择，跳过部署${NC}"
        ;;
esac

echo ""
echo -e "${YELLOW}4. 重载nginx配置...${NC}"
if [ "$choice" = "1" ] || [ "$choice" = "2" ]; then
    if sudo nginx -s reload; then
        echo -e "${GREEN}✅ nginx配置重载成功${NC}"
    else
        echo -e "${RED}❌ nginx配置重载失败${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  请手动重载nginx配置: sudo nginx -s reload${NC}"
fi

echo ""
echo -e "${GREEN}🎯 音频缓存配置要点：${NC}"
echo "• 路径转发: /audio/ → http://localhost:5000/audio/"
echo "• 缓存时间: 30天 (proxy_cache_valid 200 30d)"
echo "• 缓存状态: X-Cache-Status 响应头显示缓存状态"
echo "• 音频优化: 启用proxy_buffering，64k缓冲区"
echo "• 超时设置: 连接30s，发送/读取60s"

echo ""
echo -e "${YELLOW}📋 测试步骤：${NC}"
echo "1. 确保后端服务运行: cd eden-server && mvn spring-boot:run"
echo "2. 访问音频文件测试: curl -I http://your-domain.com/audio/star-city-bg.mp3"
echo "3. 检查缓存头: 应包含 Cache-Control: max-age=2592000"
echo "4. 检查nginx缓存: 响应头应包含 X-Cache-Status"

echo ""
echo -e "${BLUE}🔍 调试命令：${NC}"
echo "• 检查nginx状态: sudo systemctl status nginx"
echo "• 查看nginx错误日志: sudo tail -f /var/log/nginx/eden_error.log"
echo "• 查看nginx访问日志: sudo tail -f /var/log/nginx/eden_access.log"
echo "• 测试配置: sudo nginx -t"
