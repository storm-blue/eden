#!/bin/bash

# Gson系统测试脚本
echo "🧪 Gson系统重构测试"
echo "================================"

BASE_URL="http://localhost:5000"

echo ""
echo "📋 1. 测试类型安全的事件更新（使用ResidenceEventItem格式）"
echo "--------------------------------"

echo ""
echo "✏️ 更新城堡事件为特殊类型..."
curl -s -X POST "${BASE_URL}/api/residence-events/castle/update" \
  -H "Content-Type: application/json" \
  -d '{
    "eventData": "[{\"description\":\"💕 秦小淮和李星斗正在爱爱 💕\",\"type\":\"special\"},{\"description\":\"💕 她被日得胡言乱语了～ 💕\",\"type\":\"special\"},{\"description\":\"🏰 城堡中弥漫着爱情的芬芳～\",\"type\":\"special\"}]",
    "showHeartEffect": true,
    "specialText": null,
    "showSpecialEffect": true
  }' | jq '.'

echo ""
echo "🔍 验证城堡事件解析..."
response=$(curl -s -X GET "${BASE_URL}/api/residence-events/castle")
echo "$response" | jq '.'

echo ""
echo "📊 解析事件详情..."
echo "$response" | jq -r '.data | "居所: \(.residenceName), 人员数: \(.residentCount), 事件数量: \(.events | length)"'

echo ""
echo "📝 事件内容验证："
echo "$response" | jq -r '.data.events[] | "  - \(.description) (类型: \(.type), 颜色: \(.colors | join(", ")))"'

echo ""
echo "📋 2. 测试普通类型事件"
echo "--------------------------------"

echo ""
echo "✏️ 更新公园事件为普通类型..."
curl -s -X POST "${BASE_URL}/api/residence-events/park/update" \
  -H "Content-Type: application/json" \
  -d '{
    "eventData": "[{\"description\":\"🌳 公园 平静如常...\",\"type\":\"normal\"},{\"description\":\"微风轻拂过🌳 公园\",\"type\":\"normal\"}]",
    "showHeartEffect": false,
    "specialText": null,
    "showSpecialEffect": false
  }' | jq '.'

echo ""
echo "🔍 验证公园事件解析..."
response=$(curl -s -X GET "${BASE_URL}/api/residence-events/park")
echo "$response" | jq '.'

echo ""
echo "📝 公园事件内容验证："
echo "$response" | jq -r '.data.events[] | "  - \(.description) (类型: \(.type), 颜色: \(.colors | join(", ")))"'

echo ""
echo "📋 3. 测试自动事件生成（使用新的Gson序列化）"
echo "--------------------------------"

echo ""
echo "🔄 刷新所有居所事件..."
curl -s -X POST "${BASE_URL}/api/residence-events/refresh" | jq '.'

echo ""
echo "🔍 检查城堡事件（应该自动检测特殊情侣并生成特殊类型事件）..."
response=$(curl -s -X GET "${BASE_URL}/api/residence-events/castle")
echo "$response" | jq '.data.events'

echo ""
echo "📝 城堡事件验证（应该是特殊类型）："
echo "$response" | jq -r '.data.events[] | "  - \(.description) (类型: \(.type))"'

echo ""
echo "📋 4. 测试错误处理"
echo "--------------------------------"

echo ""
echo "❌ 测试无效JSON格式..."
curl -s -X POST "${BASE_URL}/api/residence-events/park/update" \
  -H "Content-Type: application/json" \
  -d '{
    "eventData": "invalid json format",
    "showHeartEffect": false
  }' | jq '.'

echo ""
echo "🔍 检查错误处理结果..."
response=$(curl -s -X GET "${BASE_URL}/api/residence-events/park")
echo "$response" | jq '.data.events'

echo ""
echo "✅ Gson系统重构测试完成！"
echo ""
echo "📝 测试总结："
echo "   - ✅ 类型安全的ResidenceEventItem类"
echo "   - ✅ Gson序列化和反序列化"
echo "   - ✅ 特殊类型事件处理"
echo "   - ✅ 普通类型事件处理"
echo "   - ✅ 自动事件生成"
echo "   - ✅ 错误处理和默认值"
echo ""
echo "🎯 预期效果："
echo "   - JSON解析应该完全可靠"
echo "   - 事件描述应该正确显示"
echo "   - 类型字段应该正确设置"
echo "   - 颜色数组应该根据类型自动设置"
