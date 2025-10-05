#!/bin/bash

echo "=== 测试居所改变时爱心状态刷新功能 ==="

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

echo ""
echo "2. 初始状态：李星斗单独住在城堡..."

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "castle"
  }' | jq '.'

echo ""
echo "验证城堡居住情况:"
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "=== 测试场景设置完成 ==="
echo ""
echo "🧪 **测试步骤**："
echo ""
echo "**步骤1：验证初始状态**"
echo "1. 打开前端页面，输入任意用户名进入星星城"
echo "2. 观察城堡：应该没有💗（只有李星斗一个人）"
echo "3. 观察其他建筑：应该都没有💗"
echo ""
echo "**步骤2：测试居所改变**"
echo "1. 输入用户名'秦小淮'进入星星城"
echo "2. 点击城堡白圈，选择居住"
echo "3. 点击'确认居住'"
echo ""
echo "🎯 **预期效果**："
echo "- ✅ 确认居住后，城堡立即显示飘动的红色💗"
echo "- ✅ 爱心状态实时更新，无需刷新页面"
echo "- ✅ 其他建筑保持无爱心状态"
echo ""
echo "**步骤3：测试居所迁移**"
echo "1. 秦小淮点击市政厅白圈，选择居住"
echo "2. 点击'确认居住'"
echo ""
echo "🎯 **预期效果**："
echo "- ✅ 城堡的💗消失（只剩李星斗一个人）"
echo "- ✅ 市政厅显示💗（秦小淮+李星斗）"
echo "- ✅ 爱心状态实时切换"
echo ""
echo "⚠️ **关键测试点**："
echo "- 居所改变后，爱心状态立即更新"
echo "- 不需要重新进入星星城"
echo "- 不需要刷新页面"
echo "- 所有建筑的爱心状态都会正确显示"
echo ""
echo "🔧 **技术实现**："
echo "- confirmResidence() 调用 loadAllBuildingResidents()"
echo "- 更新 allBuildingResidents 状态"
echo "- 触发所有建筑爱心的重新渲染"
