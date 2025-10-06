#!/bin/bash

echo "🖼️ 星星城背景图片缓存优化建议"
echo "================================"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}📊 当前背景图片状态：${NC}"
echo "• 文件位置：eden-web/public/picture/"
echo "• 文件数量：5个（lv1.jpg - lv5.jpg）"
echo "• 文件大小：469KB - 554KB 每个"
echo "• 总大小：约2.6MB"
echo "• 访问路径：/picture/lv{level}.jpg"

echo ""
echo -e "${GREEN}✅ 已有缓存：${NC}"
echo "• 生产环境：Nginx静态文件缓存（1年）"
echo "• 缓存规则：~* \\.(js|css|png|jpg|jpeg|gif|ico|svg)$"
echo "• 缓存策略：public, immutable"

echo ""
echo -e "${RED}❌ 缺失缓存：${NC}"
echo "• 开发环境：无缓存配置"
echo "• 后端配置：无专门的/picture/路径配置"
echo "• 预加载：前端未预加载背景图片"

echo ""
echo -e "${YELLOW}🚀 优化建议：${NC}"

echo ""
echo -e "${BLUE}1. 后端添加图片缓存配置：${NC}"
echo "在 WebConfig.java 中添加："
echo "registry.addResourceHandler(\"/picture/**\")"
echo "        .addResourceLocations(\"classpath:/static/picture/\")"
echo "        .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))"

echo ""
echo -e "${BLUE}2. 开发环境代理配置：${NC}"
echo "在 vite.config.js 中添加："
echo "'/picture': {"
echo "  target: 'http://localhost:5000',"
echo "  changeOrigin: true"
echo "}"

echo ""
echo -e "${BLUE}3. 前端预加载优化：${NC}"
echo "在进入星星城时预加载背景图片："
echo "const preloadBackgroundImage = (level) => {"
echo "  const img = new Image()"
echo "  img.src = \`/picture/lv\${level}.jpg\`"
echo "}"

echo ""
echo -e "${BLUE}4. 图片格式优化：${NC}"
echo "• 考虑转换为WebP格式（减少50%文件大小）"
echo "• 创建移动端专用的压缩版本"
echo "• 使用响应式图片（<picture>标签）"

echo ""
echo -e "${YELLOW}📈 预期优化效果：${NC}"
echo "┌─────────────────────┬─────────────┬─────────────┐"
echo "│       场景          │   优化前    │   优化后    │"
echo "├─────────────────────┼─────────────┼─────────────┤"
echo "│ 生产环境缓存        │     1年     │     1年     │"
echo "│ 开发环境缓存        │     无      │   后端7天   │"
echo "│ 首次加载时间        │   较慢      │   预加载    │"
echo "│ 重复访问            │   瞬间      │   瞬间      │"
echo "│ 移动端性能          │   一般      │   优化版    │"
echo "└─────────────────────┴─────────────┴─────────────┘"

echo ""
echo -e "${GREEN}✨ 总结：${NC}"
echo "• 生产环境已有良好的1年缓存"
echo "• 开发环境需要添加后端代理"
echo "• 可考虑添加预加载和图片格式优化"
echo "• 移动端可使用压缩版本提升性能"
