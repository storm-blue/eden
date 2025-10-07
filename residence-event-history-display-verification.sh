#!/bin/bash

echo "🔍 居所事件历史显示方式修改验证"
echo "================================="

echo "1. 检查后端排序方式..."
# 检查getRecentEventHistory的排序
recent_asc=$(grep -c "ORDER BY id ASC" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)
recent_desc=$(grep -c "ORDER BY id DESC" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)

echo "   - ASC排序数量: $recent_asc"
echo "   - DESC排序数量: $recent_desc"

if [ $recent_asc -eq 2 ] && [ $recent_desc -eq 2 ]; then
    echo "   ✅ 后端排序正确：查询使用正序(ASC)，清理使用倒序(DESC)"
else
    echo "   ❌ 后端排序配置不正确"
fi

echo ""
echo "2. 检查前端自动滚动功能..."
scroll_code=$(grep -c "scrollContainer.scrollTop = scrollContainer.scrollHeight" eden-web/src/components/LuckyWheel.jsx)
echo "   - 自动滚动代码数量: $scroll_code"

if [ $scroll_code -eq 1 ]; then
    echo "   ✅ 前端自动滚动功能已添加"
else
    echo "   ❌ 前端自动滚动功能未正确添加"
fi

echo ""
echo "3. 显示具体的SQL语句..."
echo "   getRecentEventHistory SQL:"
grep -A 1 "getRecentEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "SELECT" | sed 's/^[[:space:]]*/     /'

echo "   getAllEventHistory SQL:"
grep -A 1 "getAllEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "SELECT" | sed 's/^[[:space:]]*/     /'

echo ""
echo "4. 检查前端滚动逻辑..."
echo "   滚动代码片段:"
grep -A 5 "scrollContainer.scrollTop" eden-web/src/components/LuckyWheel.jsx | sed 's/^[[:space:]]*/     /'

echo ""
echo "🎉 显示方式修改验证完成！"
echo "================================="
echo ""
echo "📋 修改总结："
echo "   • 后端：返回正序排列的历史记录（最旧的在前，最新的在后）"
echo "   • 前端：自动滚动到底部显示最新记录"
echo "   • 用户体验：最新记录在底部，向上滚动查看历史（类似聊天记录）"
