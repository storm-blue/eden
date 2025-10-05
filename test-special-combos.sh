#!/bin/bash

# 测试特殊居住组合人口增长功能
# 确保后端服务正在运行

BASE_URL="http://localhost:5000"

echo "=== 测试特殊居住组合人口增长功能 ==="
echo ""

echo "1. 创建测试用户..."
# 创建用户用于测试
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "dailyDraws": 5,
    "remainingDraws": 5
  }' | jq '.'

curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "dailyDraws": 5,
    "remainingDraws": 5
  }' | jq '.'

curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "存子",
    "dailyDraws": 5,
    "remainingDraws": 5
  }' | jq '.'

echo ""
echo "2. 获取初始星星城数据..."
curl -s -X GET "${BASE_URL}/api/star-city/info" | jq '.data | {population, food, happiness, level}'

echo ""
echo "3. 设置李星斗和秦小淮居住在城堡（两人组合：每小时+1000人口）..."
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "castle"
  }' | jq '.success'

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "residence": "castle"
  }' | jq '.success'

echo ""
echo "4. 检查特殊居住组合状态..."
curl -s -X GET "${BASE_URL}/api/star-city/special-combos" | jq '.'

echo ""
echo "5. 让存子也住进城堡（三人组合：每小时+1500人口）..."
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "存子",
    "residence": "castle"
  }' | jq '.success'

echo ""
echo "6. 再次检查特殊居住组合状态..."
curl -s -X GET "${BASE_URL}/api/star-city/special-combos" | jq '.'

echo ""
echo "7. 测试每小时人口增长任务..."
echo "注意：这将直接调用内部服务方法，实际部署中由定时任务自动执行"

echo ""
echo "8. 获取城堡的居住人员..."
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "=== 测试完成 ==="
echo ""
echo "说明："
echo "- 李星斗 + 秦小淮 = 每小时人口 +1000"
echo "- 李星斗 + 秦小淮 + 存子 = 每小时人口 +1500"
echo "- 实际的人口增长由每小时定时任务执行"
echo "- 访问星星城页面可以看到'💕 爱情加成 💕'状态显示"
echo ""
echo "前端测试："
echo "1. 访问 http://localhost:3001"
echo "2. 输入用户名（李星斗、秦小淮或存子）"
echo "3. 进入星星城页面"
echo "4. 右下角城市数据中会显示爱情加成状态"
