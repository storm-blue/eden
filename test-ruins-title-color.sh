#!/bin/bash

# 测试废墟状态标题颜色变化
echo "🎨 测试废墟状态标题颜色变化"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（标题应该变为灰白色）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（标题应该恢复原色）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🎨 废墟状态标题颜色变化测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开星星城页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，标题'废墟'应该显示为灰白色"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面，标题应该恢复为对应等级的颜色"
echo ""
echo "预期结果："
echo "- 废墟状态下：标题显示为灰白色 (#D3D3D3)，营造荒凉感"
echo "- 正常状态下：标题显示为对应等级的颜色，保持活力感"
echo ""
echo "颜色说明："
echo "- 废墟状态：灰白色 (#D3D3D3) + 灰色阴影效果"
echo "- 动画效果：ruinsFade 动画，营造暗淡的废墟氛围"
