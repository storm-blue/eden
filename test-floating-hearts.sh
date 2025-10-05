#!/bin/bash

echo "=== 测试特殊情侣居所飘动爱心功能 ==="

# 后端服务地址
BASE_URL="http://localhost:5000"

echo ""
echo "1. 创建测试用户..."

# 创建李星斗用户
echo "创建用户: 李星斗"
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "dailyDraws": 3,
    "remainingDraws": 3
  }' | jq '.'

# 创建秦小淮用户
echo ""
echo "创建用户: 秦小淮"
curl -s -X POST "${BASE_URL}/api/admin/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "dailyDraws": 3,
    "remainingDraws": 3
  }' | jq '.'

# 创建存子用户
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
echo "2. 设置测试场景..."

echo ""
echo "场景1: 李星斗和秦小淮住在城堡（两人爱心）"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "castle"
  }' | jq '.'

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "residence": "castle"
  }' | jq '.'

echo ""
echo "场景2: 秦小淮、李星斗和存子住在市政厅（三人爱心）"
curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "秦小淮",
    "residence": "city_hall"
  }' | jq '.'

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "city_hall"
  }' | jq '.'

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "存子",
    "residence": "city_hall"
  }' | jq '.'

echo ""
echo "3. 验证居住情况..."

echo ""
echo "城堡居住情况:"
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "市政厅居住情况:"
curl -s -X GET "${BASE_URL}/api/residence/residents/city_hall" | jq '.'

echo ""
echo "=== 测试场景设置完成 ==="
echo ""
echo "🧪 **测试步骤**："
echo "1. 打开前端页面"
echo "2. 输入任意用户名进入星星城"
echo "3. 观察建筑白圈上方的飘动爱心"
echo ""
echo "🎯 **预期效果**："
echo "- ✅ 城堡白圈上方飘动红色💗（李星斗+秦小淮两人组合）"
echo "- ✅ 市政厅白圈上方飘动红色💗（秦小淮+李星斗+存子三人组合）"
echo "- ✅ 其他建筑无爱心（没有特殊组合）"
echo ""
echo "🎨 **爱心动画特点**："
echo "- 红色💗位于白圈上方35px"
echo "- 上下飘动 + 缩放变化"
echo "- 3秒循环动画"
echo "- 不阻挡点击操作"
echo ""
echo "⚠️ **注意**："
echo "- 只有特殊组合才会显示爱心"
echo "- 两人组合: 李星斗 + 秦小淮"
echo "- 三人组合: 秦小淮 + 李星斗 + 存子"
echo "- 爱心会实时更新（进入星星城时加载）"
