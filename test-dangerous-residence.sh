#!/bin/bash

echo "=== 测试危险居住警告功能 ==="

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
echo "2. 让李星斗单独居住在城堡..."

curl -s -X POST "${BASE_URL}/api/residence/set" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "李星斗",
    "residence": "castle"
  }' | jq '.'

echo ""
echo "3. 查看城堡的居住情况..."
curl -s -X GET "${BASE_URL}/api/residence/residents/castle" | jq '.'

echo ""
echo "=== 测试场景设置完成 ==="
echo ""
echo "现在可以测试危险居住警告功能："
echo ""
echo "🧪 **测试步骤**："
echo "1. 打开前端页面"
echo "2. 输入用户名'秦小淮'并进入星星城"
echo "3. 点击城堡白圈（李星斗单独居住的地方）"
echo "4. 观察弹框中的'确认居住'按钮"
echo ""
echo "🎯 **预期效果**："
echo "- ✅ 按钮变成红色渐变背景"
echo "- ✅ 按钮上方显示'⚠️警告！真的要住进来吗？'"
echo "- ✅ 按钮有红色边框和阴影"
echo "- ✅ 鼠标悬停时红色更深"
echo ""
echo "🔄 **对比测试**："
echo "- 用其他用户名（如'张三'）进入星星城"
echo "- 点击城堡白圈，按钮应该是正常的白色透明样式"
echo ""
echo "⚠️ **注意**："
echo "- 只有当'秦小淮'要住进只有'李星斗'一个人的居所时才会触发警告"
echo "- 如果居所里有其他人，或者是其他用户，不会显示警告"
