#!/bin/bash

# 测试静态奖品配置功能的脚本

echo "🎯 测试静态奖品配置功能"
echo "================================"

# 设置API基础URL
API_BASE="http://localhost:5000/api"

echo "1. 测试获取奖品列表（应该不包含概率）"
curl -s -X GET "$API_BASE/prizes" | jq '.'

echo -e "\n2. 测试抽奖功能（使用静态概率）"
for i in {1..5}; do
    echo "第 $i 次抽奖:"
    curl -s -X POST "$API_BASE/lottery" \
      -H "Content-Type: application/json" \
      -d '{"userId":"testuser"}' | jq '.data.prize.name'
done

echo -e "\n3. 测试统计信息"
curl -s -X GET "$API_BASE/stats" | jq '.'

echo -e "\n✅ 测试完成！"
echo "注意事项："
echo "- 奖品列表不再包含概率信息"
echo "- 概率配置现在在 LotteryService.STATIC_PRIZES 中"
echo "- 不再依赖数据库中的奖品概率数据"
