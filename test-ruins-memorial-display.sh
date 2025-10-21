#!/bin/bash

# 测试废墟状态和纪念碑显示
echo "🗿 测试废墟状态和纪念碑显示"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 检查当前废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.' || echo "获取废墟状态失败"

echo -e "\n2. 设置废墟状态为true..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次检查废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.' || echo "获取废墟状态失败"

echo -e "\n4. 获取星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🗿 测试完成！"
echo ""
echo "前端测试步骤："
echo "1. 刷新浏览器页面"
echo "2. 点击星星城按钮进入星星城页面"
echo "3. 检查控制台是否有'废墟状态检查成功: true'的日志"
echo "4. 查看星星城中心是否显示纪念碑白圈（灰色，15px，脉冲动画）"
echo "5. 如果还是没有显示，请检查："
echo "   - 浏览器控制台是否有错误"
echo "   - 网络请求是否成功"
echo "   - isRuinsMode状态是否正确设置为true"
echo ""
echo "调试信息："
echo "- 纪念碑白圈使用条件：isRuinsMode && (而不是starCityData?.isRuins)"
echo "- 废墟状态检查：页面加载时和进入星星城时都会检查"
echo "- 状态同步：starCityData加载完成后会同步isRuinsMode"
echo ""
echo "如果纪念碑仍然不显示，可能需要："
echo "1. 检查浏览器开发者工具中的React状态"
echo "2. 确认API请求返回正确的废墟状态"
echo "3. 检查CSS动画是否正确加载"
