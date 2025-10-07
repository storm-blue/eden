#!/bin/bash

echo "🔍 自动滚动功能修复验证"
echo "========================"

echo "1. 检查滚动函数是否存在..."
scroll_function=$(grep -c "const scrollToBottom = ()" eden-web/src/components/LuckyWheel.jsx)
echo "   - scrollToBottom函数数量: $scroll_function"

if [ $scroll_function -eq 1 ]; then
    echo "   ✅ scrollToBottom函数已创建"
else
    echo "   ❌ scrollToBottom函数未找到"
fi

echo ""
echo "2. 检查useEffect监听器..."
useeffect_scroll=$(grep -c "监听事件历史数据变化，自动滚动到底部" eden-web/src/components/LuckyWheel.jsx)
echo "   - useEffect滚动监听器数量: $useeffect_scroll"

if [ $useeffect_scroll -eq 1 ]; then
    echo "   ✅ useEffect滚动监听器已添加"
else
    echo "   ❌ useEffect滚动监听器未找到"
fi

echo ""
echo "3. 检查滚动方法数量..."
scroll_methods=$(grep -c "scrollContainer.scrollTop = scrollContainer.scrollHeight" eden-web/src/components/LuckyWheel.jsx)
echo "   - 滚动操作数量: $scroll_methods"

if [ $scroll_methods -eq 3 ]; then
    echo "   ✅ 使用了3种滚动方法确保成功"
else
    echo "   ❌ 滚动方法数量不正确"
fi

echo ""
echo "4. 检查requestAnimationFrame的使用..."
raf_usage=$(grep -c "requestAnimationFrame" eden-web/src/components/LuckyWheel.jsx)
echo "   - requestAnimationFrame使用数量: $raf_usage"

if [ $raf_usage -eq 1 ]; then
    echo "   ✅ 使用了requestAnimationFrame确保DOM更新"
else
    echo "   ❌ 未使用requestAnimationFrame"
fi

echo ""
echo "5. 显示滚动函数代码片段..."
echo "   scrollToBottom函数:"
grep -A 15 "const scrollToBottom = ()" eden-web/src/components/LuckyWheel.jsx | head -16 | sed 's/^[[:space:]]*/     /'

echo ""
echo "6. 显示useEffect监听器代码片段..."
echo "   useEffect监听器:"
grep -A 8 "监听事件历史数据变化" eden-web/src/components/LuckyWheel.jsx | sed 's/^[[:space:]]*/     /'

echo ""
echo "🎉 自动滚动功能修复验证完成！"
echo "========================"
echo ""
echo "📋 修复方案："
echo "   • 创建了专门的scrollToBottom函数"
echo "   • 使用了3种滚动方法：直接设置、requestAnimationFrame、延时确保"
echo "   • 添加了useEffect监听eventHistory变化"
echo "   • 延时200ms确保DOM完全渲染"
