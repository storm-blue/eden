#!/bin/bash

echo "🎵 星星城背景音乐双重下载问题分析"
echo "================================"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${RED}🔍 问题确认：音乐文件下载了两次${NC}"
echo ""

echo -e "${YELLOW}📋 双重下载的原因：${NC}"
echo ""

echo -e "${BLUE}1. HTML Audio元素预加载：${NC}"
echo "<audio ref={starCityAudioRef} preload=\"auto\">"
echo "    <source src=\"/audio/star-city-bg.mp3\" />"
echo "</audio>"
echo "• preload=\"auto\" 会立即开始下载"
echo "• 这是第一次下载"

echo ""
echo -e "${BLUE}2. JavaScript预加载函数：${NC}"
echo "const preloadMusic = async () => {"
echo "    const tempAudio = new Audio()"
echo "    tempAudio.src = starCityMusicUrl  // 第二次下载！"
echo "}"
echo "• 创建新的Audio对象并设置src"
echo "• 这是第二次下载"

echo ""
echo -e "${BLUE}3. 播放函数中的src设置：${NC}"
echo "const playStarCityMusic = () => {"
echo "    starCityAudioRef.current.src = starCityMusicUrl  // 可能第三次！"
echo "}"
echo "• 如果src已经相同，通常不会重新下载"
echo "• 但仍然是多余的操作"

echo ""
echo -e "${RED}❌ 当前下载流程：${NC}"
echo "1. 页面加载 → HTML audio preload=\"auto\" → 下载开始"
echo "2. 进入星星城 → preloadMusic() → 创建新Audio → 再次下载"
echo "3. 延迟播放 → playStarCityMusic() → 设置src → 可能再次请求"

echo ""
echo -e "${GREEN}✅ 解决方案：${NC}"
echo ""

echo -e "${YELLOW}方案1：移除HTML预加载（推荐）${NC}"
echo "• 将 preload=\"auto\" 改为 preload=\"none\""
echo "• 只依赖JavaScript预加载"
echo "• 减少一次下载"

echo ""
echo -e "${YELLOW}方案2：移除JavaScript预加载${NC}"
echo "• 删除preloadMusic()函数"
echo "• 只依赖HTML预加载"
echo "• 在播放时直接使用已预加载的音频"

echo ""
echo -e "${YELLOW}方案3：统一预加载机制（最优）${NC}"
echo "• HTML设置preload=\"none\""
echo "• 删除独立的preloadMusic()函数"
echo "• 在playStarCityMusic()中检查是否需要加载"

echo ""
echo -e "${BLUE}🛠️ 具体修复代码：${NC}"
echo ""
echo "// 1. 修改HTML audio元素"
echo "<audio ref={starCityAudioRef} preload=\"none\">"
echo ""
echo "// 2. 简化播放函数"
echo "const playStarCityMusic = () => {"
echo "    if (starCityAudioRef.current && !isMusicPlaying) {"
echo "        if (!starCityAudioRef.current.src) {"
echo "            starCityAudioRef.current.src = starCityMusicUrl"
echo "        }"
echo "        starCityAudioRef.current.loop = true"
echo "        starCityAudioRef.current.play()"
echo "    }"
echo "}"
echo ""
echo "// 3. 移除preloadMusic()调用"

echo ""
echo -e "${GREEN}📊 优化效果：${NC}"
echo "┌─────────────────────┬─────────────┬─────────────┐"
echo "│       场景          │   修复前    │   修复后    │"
echo "├─────────────────────┼─────────────┼─────────────┤"
echo "│ 音频下载次数        │     2次     │     1次     │"
echo "│ 网络请求            │   重复请求   │   单次请求  │"
echo "│ 加载时间            │   较慢      │   更快      │"
echo "│ 带宽消耗            │   双倍      │   正常      │"
echo "└─────────────────────┴─────────────┴─────────────┘"

echo ""
echo -e "${YELLOW}🔧 立即修复建议：${NC}"
echo "1. 将HTML audio的preload改为\"none\""
echo "2. 删除preloadMusic()函数调用"
echo "3. 在playStarCityMusic()中添加src检查"
echo "4. 测试确认只下载一次"

echo ""
echo -e "${BLUE}💡 验证方法：${NC}"
echo "• 打开浏览器开发者工具"
echo "• 进入Network面板"
echo "• 清除缓存并刷新"
echo "• 进入星星城页面"
echo "• 查看star-city-bg.mp3的请求次数"
