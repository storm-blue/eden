#!/bin/bash

# 测试废墟状态功能
echo "🏚️ 测试星星城废墟状态功能"
echo "================================"

API_BASE="http://localhost:5000/api"

# 测试获取废墟状态
echo "1. 获取当前废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.' || echo "获取废墟状态失败"

echo -e "\n2. 获取星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n3. 获取巨人进攻状态..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive}' || echo "获取巨人进攻状态失败"

echo -e "\n4. 设置废墟状态为true（应该自动停止巨人进攻）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 再次获取星星城信息（应该显示废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n6. 再次获取巨人进攻状态（应该显示已停止）..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive}' || echo "获取巨人进攻状态失败"

echo -e "\n7. 设置废墟状态为false..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n8. 最终获取星星城信息（应该恢复正常）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🏚️ 废墟状态功能测试完成！"
echo "注意：废墟状态下巨人进攻会自动停止，恢复正常状态后巨人进攻可以重新开始。"
