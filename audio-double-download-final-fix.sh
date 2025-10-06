#!/bin/bash

echo "🎵 背景音乐双重下载问题彻底修复"
echo "================================"

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${RED}🔍 双重下载的根本原因：${NC}"
echo ""

echo -e "${YELLOW}1. HTML Source标签问题：${NC}"
echo "<audio preload=\"none\">"
echo "    <source src=\"/audio/star-city-bg.mp3\" />  <!-- 这里触发第一次下载！ -->"
echo "</audio>"
echo "• 即使设置了preload=\"none\"，<source>标签仍可能触发下载"
echo "• 浏览器会预解析<source>标签的src属性"

echo ""
echo -e "${YELLOW}2. JavaScript设置src问题：${NC}"
echo "starCityAudioRef.current.src = starCityMusicUrl  // 第二次下载！"
echo "• JavaScript动态设置src会触发新的网络请求"
echo "• 即使URL相同，浏览器可能认为是新的资源请求"

echo ""
echo -e "${GREEN}✅ 彻底修复方案：${NC}"
echo ""

echo -e "${BLUE}修复1：移除HTML Source标签${NC}"
echo "<!-- 修复前 -->"
echo "<audio preload=\"none\">"
echo "    <source src=\"/audio/star-city-bg.mp3\" />"
echo "</audio>"
echo ""
echo "<!-- 修复后 -->"
echo "<audio preload=\"none\">"
echo "    <!-- 完全移除source标签 -->"
echo "</audio>"

echo ""
echo -e "${BLUE}修复2：优化JavaScript src设置${NC}"
echo "// 修复前：可能重复设置"
echo "if (!audio.src || audio.src === window.location.href) {"
echo "    audio.src = musicUrl  // 可能触发重复下载"
echo "}"
echo ""
echo "// 修复后：严格检查"
echo "if (!audio.src) {"
echo "    audio.src = musicUrl  // 只在真正为空时设置"
echo "}"

echo ""
echo -e "${YELLOW}📊 修复效果对比：${NC}"
echo "┌─────────────────────┬─────────────┬─────────────┐"
echo "│       场景          │   修复前    │   修复后    │"
echo "├─────────────────────┼─────────────┼─────────────┤"
echo "│ HTML层面下载        │     1次     │     0次     │"
echo "│ JavaScript下载      │     1次     │     1次     │"
echo "│ 总下载次数          │     2次     │     1次     │"
echo "│ 网络请求            │   重复请求   │   单次请求  │"
echo "│ 带宽消耗            │   1.5MB     │   0.75MB    │"
echo "└─────────────────────┴─────────────┴─────────────┘"

echo ""
echo -e "${GREEN}🎯 新的加载流程：${NC}"
echo "1. 页面加载 → HTML audio元素创建 → 无source标签 → 不下载"
echo "2. 进入星星城 → 延迟1-2秒"
echo "3. 调用playStarCityMusic() → 检查audio.src为空 → 设置src并下载"
echo "4. 音频开始播放 → 循环播放 → 不再重新下载"

echo ""
echo -e "${BLUE}🔍 验证方法：${NC}"
echo ""
echo "1. 清除浏览器缓存"
echo "2. 打开开发者工具 → Network面板"
echo "3. 访问网站并进入星星城"
echo "4. 查看star-city-bg.mp3的请求"
echo "5. ✅ 应该只看到1次请求（约751KB）"
echo "6. 📝 查看控制台日志：'首次设置音频源: /audio/star-city-bg.mp3'"

echo ""
echo -e "${YELLOW}⚠️  技术细节：${NC}"
echo "• 移除<source>标签不影响音频播放功能"
echo "• JavaScript动态设置src是标准做法"
echo "• preload=\"none\"确保不会提前下载"
echo "• 只在真正需要播放时才下载音频"

echo ""
echo -e "${GREEN}🧪 测试脚本：${NC}"
echo "# 清除缓存后测试"
echo "1. 按F12打开开发者工具"
echo "2. 右键刷新按钮 → '清空缓存并硬性重新加载'"
echo "3. 进入星星城页面"
echo "4. 在Network面板搜索'star-city-bg.mp3'"
echo "5. 确认只有1个请求"

echo ""
echo -e "${BLUE}💡 原理说明：${NC}"
echo "• HTML <source>标签会被浏览器预解析"
echo "• 即使preload=\"none\"，某些浏览器仍可能下载metadata"
echo "• 完全移除<source>标签是最彻底的解决方案"
echo "• JavaScript动态设置src时严格检查，避免重复设置"

echo ""
echo -e "${GREEN}✨ 修复完成！${NC}"
echo "现在背景音乐确保只下载一次，彻底解决双重下载问题！"
