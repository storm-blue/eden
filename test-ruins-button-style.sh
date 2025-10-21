#!/bin/bash

# 测试废墟状态下抽奖按钮的样式修改
echo "🎲 测试废墟状态下抽奖按钮的样式修改"
echo "=================================="

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（抽奖按钮应该变为灰色且不晃动）..."
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

echo -e "\n🎲 废墟状态下抽奖按钮样式修改测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开抽奖页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，抽奖按钮应该："
echo "   - 保持原有文本'🎲 转动命运'"
echo "   - 变为灰色背景 (#ccc)"
echo "   - 透明度降低 (opacity: 0.6)"
echo "   - 鼠标指针变为'not-allowed'"
echo "   - 没有左右晃动效果"
echo "4. 点击抽奖按钮，应该没有任何反应（静默处理）"
echo "5. 设置废墟状态为false"
echo "6. 刷新页面，抽奖按钮应该恢复正常状态"
echo ""
echo "预期结果："
echo "- 废墟状态下：按钮文本不变，样式为灰色不可点击，无晃动"
echo "- 正常状态下：按钮文本不变，样式为正常可点击"
echo ""
echo "样式说明："
echo "- 背景色：灰色 (#ccc)"
echo "- 透明度：0.6"
echo "- 鼠标指针：not-allowed"
echo "- 动画效果：无晃动"
echo "- 文本内容：保持'🎲 转动命运'不变"
