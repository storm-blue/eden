#!/bin/bash

echo "=== 测试管理后台愿望管理功能 ==="

# 后端服务地址
BASE_URL="http://localhost:5000"

# 管理员登录信息
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin2008"

echo ""
echo "1. 管理员登录..."

# 获取管理员token
LOGIN_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/admin/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"${ADMIN_USERNAME}\",\"password\":\"${ADMIN_PASSWORD}\"}")

echo "登录响应: $LOGIN_RESPONSE"

# 提取token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.token // empty')

if [ -z "$TOKEN" ]; then
    echo "❌ 管理员登录失败，无法获取token"
    exit 1
fi

echo "✅ 管理员登录成功，token: ${TOKEN:0:20}..."

echo ""
echo "2. 测试获取愿望列表..."

curl -s -X GET "${BASE_URL}/api/admin/wishes" \
  -H "Authorization: Bearer ${TOKEN}" | jq '.'

echo ""
echo "3. 测试获取愿望统计..."

curl -s -X GET "${BASE_URL}/api/admin/wishes/stats" \
  -H "Authorization: Bearer ${TOKEN}" | jq '.'

echo ""
echo "4. 创建测试愿望（需要先有用户和许愿次数）..."

# 创建测试用户
echo "创建测试用户: 测试用户"
curl -s -X POST "${BASE_URL}/api/admin/users/add" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -d '{
    "userId": "测试用户",
    "dailyDraws": 3,
    "remainingDraws": 5
  }' | jq '.'

# 给用户增加许愿次数（模拟抽中"许愿一次"）
echo ""
echo "模拟用户抽中许愿一次奖品..."
# 这里需要直接操作数据库或通过其他方式给用户增加wish_count
# 为了测试，我们可以通过用户服务API来增加

echo ""
echo "5. 创建测试愿望..."
curl -s -X POST "${BASE_URL}/api/wishes" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "测试用户",
    "wishContent": "希望系统功能完善，用户体验更好！"
  }' | jq '.'

echo ""
echo "6. 再次获取愿望列表（应该包含新创建的愿望）..."

WISHES_RESPONSE=$(curl -s -X GET "${BASE_URL}/api/admin/wishes" \
  -H "Authorization: Bearer ${TOKEN}")

echo "$WISHES_RESPONSE" | jq '.'

# 提取第一个愿望的ID用于删除测试
WISH_ID=$(echo "$WISHES_RESPONSE" | jq -r '.data[0].id // empty')

if [ -n "$WISH_ID" ]; then
    echo ""
    echo "7. 测试删除愿望（ID: $WISH_ID）..."
    
    curl -s -X DELETE "${BASE_URL}/api/admin/wishes/${WISH_ID}" \
      -H "Authorization: Bearer ${TOKEN}" | jq '.'
    
    echo ""
    echo "8. 验证愿望已删除..."
    curl -s -X GET "${BASE_URL}/api/admin/wishes" \
      -H "Authorization: Bearer ${TOKEN}" | jq '.'
else
    echo ""
    echo "⚠️ 没有找到可删除的愿望ID"
fi

echo ""
echo "=== 测试完成 ==="
echo ""
echo "现在可以："
echo "1. 打开管理后台: http://localhost:5000/admin.html"
echo "2. 使用 admin/admin2008 登录"
echo "3. 点击'愿望管理'标签页"
echo "4. 查看和管理所有用户的愿望"
echo ""
echo "功能说明："
echo "- 📊 查看愿望统计信息"
echo "- 📋 查看所有愿望列表"
echo "- 🗑️ 删除不当愿望"
echo "- 🔄 刷新数据"
