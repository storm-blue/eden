#!/bin/bash

echo "=== 测试特殊情侣爱心效果 ==="

# 后端服务地址
BASE_URL="http://localhost:5000"

echo ""
echo "1. 创建测试用户 李星斗 和 秦小淮..."

# 创建李星斗用户（如果不存在）
echo "创建用户: 李星斗"
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "dailyDraws": 3,
    "remainingDraws": 3
  }' | jq '.'

# 创建秦小淮用户（如果不存在）
echo ""
echo "创建用户: 秦小淮"
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "dailyDraws": 3,
    "remainingDraws": 3
  }' | jq '.'

echo ""
echo "2. 让两人都居住在城堡..."

# 李星斗居住在城堡
echo "李星斗居住在城堡:"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "castle"
  }' | jq '.'

# 秦小淮居住在城堡
echo ""
echo "秦小淮居住在城堡:"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "residence": "castle"
  }' | jq '.'

echo ""
echo "3. 查看城堡的居住人员..."
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "=== 测试完成 ==="
echo ""
echo "现在可以："
echo "1. 打开前端页面"
echo "2. 输入任意用户名进入星星城"
echo "3. 点击中心的城堡白圈"
echo "4. 应该能看到满屏爱心和特殊文字：'秦小淮和李星斗正在爱爱'"
echo ""
echo "注意：如果看不到效果，请确保："
echo "- 城堡中只有这两个用户"
echo "- 后端服务正在运行"
echo "- 前端页面已刷新"
