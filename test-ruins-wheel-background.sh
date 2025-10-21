#!/bin/bash

# 测试废墟状态下转盘背景的变化
echo "🎡 测试废墟状态下转盘背景的变化"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（转盘背景应该变为灰色）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（转盘背景应该恢复正常）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🎡 废墟状态下转盘背景变化测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开抽奖页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，转盘应该："
echo "   - 整体透明度降低 (opacity: 0.7)"
echo "   - 阴影变为灰色 (rgba(128, 128, 128, 0.3))"
echo "   - 背景光环变为灰色渐变"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面，转盘应该恢复正常状态"
echo ""
echo "预期结果："
echo "- 废墟状态下：转盘整体呈现灰色调，营造荒凉感"
echo "- 正常状态下：转盘保持原有的彩色效果"
echo ""
echo "样式说明："
echo "- 转盘容器：透明度0.7，灰色阴影"
echo "- 背景光环：灰色渐变 (rgba(128, 128, 128, 0.1))"
echo "- 整体效果：暗淡的灰色调，符合废墟氛围"
echo ""
echo "视觉效果对比："
echo "- 废墟状态：暗淡灰色，无活力"
echo "- 正常状态：彩色渐变，充满活力"
