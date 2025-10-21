#!/bin/bash

# 测试废墟状态下巨人进攻停止功能
echo "👹 测试废墟状态下巨人进攻停止功能"
echo "=========================================="

API_BASE="http://localhost:5000/api"

echo "1. 获取当前巨人进攻状态..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive, startTime}' || echo "获取巨人进攻状态失败"

echo -e "\n2. 获取当前废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.data | {isRuins, level}' || echo "获取废墟状态失败"

echo -e "\n3. 如果巨人正在进攻，先手动结束巨人进攻..."
curl -s -X POST "${API_BASE}/giant-attack/end" | jq '.' || echo "结束巨人进攻失败"

echo -e "\n4. 手动触发巨人进攻..."
curl -s -X POST "${API_BASE}/giant-attack/trigger" | jq '.' || echo "触发巨人进攻失败"

echo -e "\n5. 确认巨人进攻已开始..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive, startTime}' || echo "获取巨人进攻状态失败"

echo -e "\n6. 设置废墟状态为true（应该自动停止巨人进攻）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n7. 检查巨人进攻是否已停止..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive, endTime}' || echo "获取巨人进攻状态失败"

echo -e "\n8. 检查废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.data | {isRuins, level}' || echo "获取废墟状态失败"

echo -e "\n9. 恢复正常状态..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n10. 最终检查巨人进攻状态..."
curl -s "${API_BASE}/giant-attack/status" | jq '.data | {isAttacking, isActive}' || echo "获取巨人进攻状态失败"

echo -e "\n👹 废墟状态下巨人进攻停止功能测试完成！"
echo "预期结果："
echo "- 废墟状态下巨人进攻应该自动停止"
echo "- 恢复正常状态后巨人进攻可以重新开始"
echo "- 废墟状态下不会触发新的巨人进攻"
