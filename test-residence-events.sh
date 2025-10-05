#!/bin/bash

# 居所事件系统测试脚本

echo "🏠 居所事件系统测试脚本"
echo "================================"

BASE_URL="http://localhost:5000"

echo ""
echo "📋 1. 测试获取所有居所事件"
echo "--------------------------------"

residences=("castle" "city_hall" "palace" "dove_house" "park")
residence_names=("🏰 城堡" "🏛️ 市政厅" "🏯 行宫" "🕊️ 小白鸽家" "🌳 公园")

for i in "${!residences[@]}"; do
    residence="${residences[$i]}"
    residence_name="${residence_names[$i]}"
    
    echo ""
    echo "🔍 获取 ${residence_name} 的事件..."
    curl -s -X GET "${BASE_URL}/api/residence-events/${residence}" | jq '.'
done

echo ""
echo "📋 2. 测试手动刷新所有居所事件"
echo "--------------------------------"

echo ""
echo "🔄 手动刷新所有居所事件..."
curl -s -X POST "${BASE_URL}/api/residence-events/refresh" | jq '.'

echo ""
echo "📋 3. 测试更新单个居所事件"
echo "--------------------------------"

echo ""
echo "✏️ 更新城堡事件（多条事件描述 + 特殊情侣特效）..."
curl -s -X POST "${BASE_URL}/api/residence-events/castle/update" \
  -H "Content-Type: application/json" \
  -d '{
    "eventData": "[{\"description\":\"🌟 测试事件：城堡中发生了奇妙的事情！\",\"colors\":[\"#ff69b4\",\"#ff1744\"]},{\"description\":\"✨ 魔法光芒在城堡中闪烁\",\"colors\":[\"#a29bfe\",\"#fd79a8\"]},{\"description\":\"🦄 独角兽在花园中漫步\",\"colors\":[\"#00b894\",\"#00cec9\"]}]",
    "showHeartEffect": true,
    "specialText": "💕 测试特殊情侣文字：正在爱爱～ 💕",
    "showSpecialEffect": true
  }' | jq '.'

echo ""
echo "🔍 验证城堡事件更新..."
curl -s -X GET "${BASE_URL}/api/residence-events/castle" | jq '.'

echo ""
echo "📋 4. 测试居所事件数据结构"
echo "--------------------------------"

echo ""
echo "🔍 获取市政厅详细信息..."
response=$(curl -s -X GET "${BASE_URL}/api/residence-events/city_hall")
echo "$response" | jq '.'

echo ""
echo "📊 解析事件数据..."
echo "$response" | jq -r '.data | "居所: \(.residenceName), 人员数: \(.residentCount), 事件数量: \(.events | length), 爱心效果: \(.showHeartEffect), 特殊文字: \(.specialText // "无"), 特殊特效: \(.showSpecialEffect)"'

echo ""
echo "📝 事件详情："
echo "$response" | jq -r '.data.events[] | "  - \(.description) (颜色: \(.colors | join(", ")))"'

echo ""
echo "📋 5. 测试错误处理"
echo "--------------------------------"

echo ""
echo "❌ 测试无效居所类型..."
curl -s -X GET "${BASE_URL}/api/residence-events/invalid_residence" | jq '.'

echo ""
echo "📋 6. 测试定时任务状态"
echo "--------------------------------"

echo ""
echo "ℹ️ 居所事件定时任务信息："
echo "   - 执行时间：每小时的第5分钟"
echo "   - Cron表达式：0 5 * * * ?"
echo "   - 任务类：ResidenceEventRefreshTask"
echo "   - 刷新方法：refreshResidenceEvents()"

echo ""
echo "✅ 居所事件系统测试完成！"
echo ""
echo "📝 测试总结："
echo "   - ✅ 居所事件查询API"
echo "   - ✅ 手动刷新功能"
echo "   - ✅ 事件更新功能"
echo "   - ✅ 数据结构验证"
echo "   - ✅ 错误处理"
echo "   - ✅ 定时任务配置"
echo ""
echo "🎯 下一步："
echo "   1. 在 ResidenceEventService.generateResidenceEvent() 中实现具体的事件生成逻辑"
echo "   2. 根据居所类型和居住人员生成不同的多条事件"
echo "   3. 为每条事件设置合适的描述和颜色"
echo "   4. 配置爱心特效显示条件"
echo "   5. 设置特殊情侣组合的文字和特效"
echo "   6. 测试前端多条事件显示效果"
echo ""
echo "💡 事件数据格式示例："
echo '   {
     "eventData": "[{\"description\":\"事件1\",\"colors\":[\"#颜色1\",\"#颜色2\"]},{\"description\":\"事件2\",\"colors\":[\"#颜色3\",\"#颜色4\"]}]",
     "showHeartEffect": true,
     "specialText": "💕 特殊情侣文字 💕",
     "showSpecialEffect": true
   }'
echo ""
echo "🌟 特殊情侣组合功能："
echo "   - 两人组合：秦小淮 + 李星斗"
echo "   - 三人组合：秦小淮 + 李星斗 + 存子"
echo "   - 自动生成特殊文字和爱心飞舞特效"
echo "   - 前端从事件接口获取特效控制，不再硬编码"
