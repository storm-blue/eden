#!/bin/bash

# 测试废墟状态下音乐控制功能
echo "🎵 测试废墟状态下音乐控制功能"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（应该停止音乐）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（应该恢复音乐）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🎵 废墟状态下音乐控制功能测试完成！"
echo ""
echo "前端测试说明："
echo "1. 打开星星城页面"
echo "2. 设置废墟状态为true"
echo "3. 刷新页面，应该听不到背景音乐"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面，应该能听到背景音乐"
echo ""
echo "预期结果："
echo "- 废墟状态下：无背景音乐，营造寂静的废墟氛围"
echo "- 正常状态下：播放背景音乐，营造活跃的城市氛围"
