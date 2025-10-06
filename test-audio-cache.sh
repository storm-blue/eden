#!/bin/bash

echo "🎵 音频缓存系统测试脚本"
echo "================================"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. 检查后端音频文件是否存在...${NC}"
if [ -f "eden-server/src/main/resources/static/audio/star-city-bg.mp3" ]; then
    echo -e "${GREEN}✅ star-city-bg.mp3 文件存在${NC}"
    ls -lh eden-server/src/main/resources/static/audio/star-city-bg.mp3
else
    echo -e "${RED}❌ star-city-bg.mp3 文件不存在${NC}"
fi

if [ -f "eden-server/src/main/resources/static/audio/star-city-bg2.mp3" ]; then
    echo -e "${GREEN}✅ star-city-bg2.mp3 文件存在${NC}"
    ls -lh eden-server/src/main/resources/static/audio/star-city-bg2.mp3
else
    echo -e "${RED}❌ star-city-bg2.mp3 文件不存在${NC}"
fi

echo ""
echo -e "${YELLOW}2. 检查后端WebConfig缓存配置...${NC}"
if grep -q "CacheControl.maxAge" eden-server/src/main/java/com/eden/lottery/config/WebConfig.java; then
    echo -e "${GREEN}✅ HTTP缓存配置已添加${NC}"
    grep -A 3 "CacheControl.maxAge" eden-server/src/main/java/com/eden/lottery/config/WebConfig.java
else
    echo -e "${RED}❌ HTTP缓存配置未找到${NC}"
fi

echo ""
echo -e "${YELLOW}3. 检查前端代理配置...${NC}"
if grep -q "/audio" eden-web/vite.config.js; then
    echo -e "${GREEN}✅ 前端音频代理配置已添加${NC}"
    grep -A 5 "/audio" eden-web/vite.config.js
else
    echo -e "${RED}❌ 前端音频代理配置未找到${NC}"
fi

echo ""
echo -e "${YELLOW}4. 检查前端音频路径配置...${NC}"
if grep -q "starCityMusicList" eden-web/src/components/LuckyWheel.jsx; then
    echo -e "${GREEN}✅ 前端音频路径配置已找到${NC}"
    grep -A 3 "starCityMusicList" eden-web/src/components/LuckyWheel.jsx
else
    echo -e "${RED}❌ 前端音频路径配置未找到${NC}"
fi

echo ""
echo -e "${YELLOW}5. 检查nginx音频转发配置...${NC}"
if [ -f "nginx.conf" ]; then
    if grep -q "location /audio/" nginx.conf; then
        echo -e "${GREEN}✅ nginx音频转发配置已添加${NC}"
        grep -A 5 "location /audio/" nginx.conf | head -6
    else
        echo -e "${RED}❌ nginx音频转发配置未找到${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  nginx.conf文件不存在，如果使用nginx请手动配置${NC}"
fi

echo ""
echo -e "${YELLOW}📋 测试步骤说明：${NC}"
echo "1. 启动后端服务：cd eden-server && mvn spring-boot:run"
echo "2. 启动前端服务：cd eden-web && npm run dev"
echo "3. 如果使用nginx：./deploy-nginx-audio.sh 部署配置"
echo "4. 访问 http://localhost:3000 (开发环境) 或 http://your-domain.com (生产环境)"
echo "5. 点击进入星星城，观察音乐是否正常播放"
echo "6. 刷新页面，再次进入星星城，音乐应该瞬间播放（使用缓存）"

echo ""
echo -e "${YELLOW}🔍 缓存验证方法：${NC}"
echo "1. 打开浏览器开发者工具 (F12)"
echo "2. 切换到 Network 标签"
echo "3. 首次进入星星城时，应该看到音频文件下载"
echo "4. 刷新页面后再次进入，音频文件应显示 '(from disk cache)'"

echo ""
echo -e "${GREEN}🎯 预期效果：${NC}"
echo "• 首次访问：正常下载音频文件（~2MB，2-3秒）"
echo "• 再次访问：瞬间播放，无网络请求（0秒）"
echo "• 缓存有效期：30天"
echo "• 支持随机播放：2首音乐随机切换"
