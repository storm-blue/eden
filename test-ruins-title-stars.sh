#!/bin/bash

# 测试废墟状态标题星星装饰变化
echo "⭐ 测试废墟状态标题星星装饰变化"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（标题应该没有星星装饰）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（标题应该恢复星星装饰）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n⭐ 废墟状态标题星星装饰变化测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开星星城页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，标题应该显示为'废墟'（没有星星装饰）"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面，标题应该显示为'✨城市名称✨'（有星星装饰）"
echo ""
echo "预期结果："
echo "- 废墟状态下：标题显示为'废墟'，简洁无装饰，营造荒凉感"
echo "- 正常状态下：标题显示为'✨城市名称✨'，有星星装饰，保持活力感"
echo ""
echo "视觉效果对比："
echo "- 废墟状态：'废墟' - 简洁、暗淡、无装饰"
echo "- 正常状态：'✨晨曦小镇✨' - 华丽、明亮、有装饰"
