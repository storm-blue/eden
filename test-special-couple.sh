#!/bin/bash

echo "=== 测试特殊情侣爱心效果 ==="

# 后端服务地址
BASE_URL="http://localhost:5000"

echo ""
echo "1. 创建测试用户 李星斗、秦小淮 和 存子..."

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

# 创建存子用户（如果不存在）
echo ""
echo "创建用户: 存子"
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "存子",
    "dailyDraws": 3,
    "remainingDraws": 3
  }' | jq '.'

echo ""
echo "=== 测试场景1：两人组合（城堡） ==="
echo "让李星斗和秦小淮居住在城堡..."

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
echo "查看城堡的居住人员（应该显示两人爱心效果）..."
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "=== 测试场景2：三人组合（市政厅） ==="
echo "让秦小淮、李星斗和存子居住在市政厅..."

# 秦小淮居住在市政厅
echo "秦小淮居住在市政厅:"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "residence": "city_hall"
  }' | jq '.'

# 李星斗居住在市政厅
echo ""
echo "李星斗居住在市政厅:"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "city_hall"
  }' | jq '.'

# 存子居住在市政厅
echo ""
echo "存子居住在市政厅:"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "存子",
    "residence": "city_hall"
  }' | jq '.'

echo ""
echo "查看市政厅的居住人员（应该显示三人爱心效果）..."
curl -s -X GET "${BASE_URL}/api/residence/residents/city_hall" | jq '.'

echo ""
echo "=== 测试完成 ==="
echo ""
echo "现在可以："
echo "1. 打开前端页面"
echo "2. 输入任意用户名进入星星城"
echo "3. 点击城堡白圈 🏰 - 应该看到：'秦小淮和李星斗正在爱爱'"
echo "4. 点击市政厅白圈 🏛️ - 应该看到：'秦小淮、李星斗和存子正在爱爱'"
echo ""
echo "特殊效果："
echo "- 两人组合：15个爱心飘动 + 两人文字"
echo "- 三人组合：15个爱心飘动 + 三人文字"
echo "- 爱心不会出现在按钮区域"
