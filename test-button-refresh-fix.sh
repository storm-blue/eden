#!/bin/bash

# 测试转盘页面刷新时按钮正确置灰的修复
echo "🔧 测试转盘页面刷新时按钮正确置灰的修复"
echo "========================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🔧 转盘页面刷新时按钮正确置灰的修复测试完成！"
echo ""
echo "前端测试说明："
echo "1. 设置废墟状态为true"
echo "2. 刷新转盘页面（F5或Ctrl+R）"
echo "3. 页面加载完成后，抽奖按钮应该："
echo "   - 立即显示为灰色不可点击状态"
echo   "   - 不需要等待星星城数据加载"
echo "4. 设置废墟状态为false"
echo "5. 再次刷新转盘页面"
echo "6. 页面加载完成后，抽奖按钮应该恢复正常状态"
echo ""
echo "修复说明："
echo "- 问题：页面刷新时starCityData还未加载，导致按钮没有正确置灰"
echo "- 解决方案：添加独立的isRuinsMode状态，页面加载时立即检查废墟状态"
echo "- 实现：checkRuinsStatus函数在页面初始化时立即调用"
echo ""
echo "技术细节："
echo "- 新增isRuinsMode状态：独立的废墟状态跟踪"
echo "- checkRuinsStatus函数：页面加载时立即检查废墟状态"
echo "- 状态同步：starCityData加载完成后同步isRuinsMode"
echo "- 按钮逻辑：使用isRuinsMode而不是starCityData?.isRuins"
