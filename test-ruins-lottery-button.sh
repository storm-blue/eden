#!/bin/bash

# 测试废墟状态下抽奖按钮的状态
echo "🎲 测试废墟状态下抽奖按钮的状态"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（抽奖按钮应该变为不可点击的灰色）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（抽奖按钮应该恢复正常）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🎲 废墟状态下抽奖按钮状态测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开抽奖页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，抽奖按钮应该显示为'🏚️ 废墟状态'并变为灰色不可点击"
echo "4. 点击抽奖按钮，应该弹出提示'废墟状态下无法进行抽奖！'"
echo "5. 设置废墟状态为false"
echo "6. 刷新页面，抽奖按钮应该恢复正常状态"
echo ""
echo "预期结果："
echo "- 废墟状态下：抽奖按钮显示'🏚️ 废墟状态'，样式为disabled灰色"
echo "- 正常状态下：抽奖按钮显示'🎲 转动命运'，样式为正常可点击"
echo ""
echo "功能说明："
echo "- 按钮状态：废墟状态下disabled=true，正常状态下disabled=false"
echo "- 按钮文本：废墟状态下显示'🏚️ 废墟状态'，正常状态下显示'🎲 转动命运'"
echo "- 点击行为：废墟状态下点击会弹出提示，正常状态下可以正常抽奖"
echo "- CSS样式：废墟状态下按钮会应用disabled样式，呈现灰色不可点击状态"
