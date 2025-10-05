#!/bin/bash

# 测试居住历史记录功能
# 确保后端服务正在运行

BASE_URL="http://localhost:5000"

echo "=== 测试居住历史记录功能 ==="
echo ""

echo "1. 创建测试用户..."
# 创建用户用于测试
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "测试用户1",
    "dailyDraws": 5,
    "remainingDraws": 5
  }' | jq '.'

echo ""
echo "2. 设置用户居住地点（城堡）..."
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "测试用户1",
    "residence": "castle"
  }' | jq '.'

echo ""
echo "3. 更换居住地点（市政厅）..."
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "测试用户1",
    "residence": "city_hall"
  }' | jq '.'

echo ""
echo "4. 再次更换居住地点（公园）..."
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "测试用户1",
    "residence": "park"
  }' | jq '.'

echo ""
echo "5. 查询用户居住历史..."
curl -s -X GET "${BASE_URL}/api/residence/history/测试用户1" | jq '.'

echo ""
echo "6. 查询城堡的居住历史..."
curl -s -X GET "${BASE_URL}/api/residence/history/location/castle" | jq '.'

echo ""
echo "7. 查询居住历史统计信息..."
curl -s -X GET "${BASE_URL}/api/residence/statistics" | jq '.'

echo ""
echo "=== 测试完成 ==="
echo ""
echo "请访问管理后台查看居住历史记录："
echo "http://localhost:5000/admin.html"
echo "登录信息：admin / admin2008"
echo "点击'居住历史'标签页查看记录"
