#!/bin/bash

# 测试数据库废墟状态字段更新
echo "🔍 测试数据库废墟状态字段更新"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, population, food}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（检查是否更新）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, population, food}' || echo "获取星星城信息失败"

echo -e "\n4. 获取管理员废墟状态..."
curl -s "${API_BASE}/star-city/admin/ruins-status" | jq '.data | {isRuins, level}' || echo "获取废墟状态失败"

echo -e "\n5. 设置废墟状态为false..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n6. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, population, food}' || echo "获取星星城信息失败"

echo -e "\n🔍 数据库废墟状态字段更新测试完成！"
echo "如果isRuins字段始终为null或false，说明数据库映射有问题。"
echo "需要检查："
echo "1. StarCityMapper.xml是否正确映射is_ruins字段"
echo "2. 数据库字段类型是否为INTEGER"
echo "3. 后端服务是否需要重启"
