#!/bin/bash

# 测试废墟状态下X关闭按钮的显示
echo "❌ 测试废墟状态下X关闭按钮的显示"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（X关闭按钮应该仍然显示）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（X关闭按钮应该仍然显示）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n❌ 废墟状态下X关闭按钮显示测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开星星城页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，右上角应该仍然显示X关闭按钮"
echo "4. 点击X按钮，应该能够正常关闭星星城页面"
echo "5. 设置废墟状态为false"
echo "6. 刷新页面，X关闭按钮应该仍然显示"
echo ""
echo "预期结果："
echo "- 废墟状态下：X关闭按钮始终显示，用户可以正常关闭页面"
echo "- 正常状态下：X关闭按钮始终显示，用户可以正常关闭页面"
echo ""
echo "功能说明："
echo "- X关闭按钮位置：右上角 (top: 30px, right: 30px)"
echo "- 按钮样式：圆形白色半透明背景，带模糊效果"
echo "- 交互效果：鼠标悬停时放大和变亮"
echo "- 功能：点击后关闭星星城页面并返回愿望星空"
