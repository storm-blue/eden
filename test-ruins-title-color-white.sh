#!/bin/bash

# 测试废墟状态标题颜色调整（更偏白色）
echo "🎨 测试废墟状态标题颜色调整（更偏白色）"
echo "========================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（标题应该显示为更偏白的颜色）..."
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

echo -e "\n🎨 废墟状态标题颜色调整测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开星星城页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，标题'废墟'应该显示为更偏白的颜色"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面，标题应该恢复为对应等级的颜色"
echo ""
echo "颜色调整说明："
echo "- 修改前：灰白色 (#D3D3D3) - 较深的灰色"
echo "- 修改后：更偏白的灰色 (#F5F5F5) - 更接近白色"
echo ""
echo "视觉效果："
echo "- 废墟状态：更偏白的标题，营造苍白荒凉的废墟感"
echo "- 正常状态：保持原有的彩色标题，充满活力"
echo ""
echo "阴影效果："
echo "- 内层阴影：rgba(245, 245, 245, 0.6) - 更亮的白色阴影"
echo "- 外层阴影：rgba(200, 200, 200, 0.4) - 更亮的灰色阴影"
