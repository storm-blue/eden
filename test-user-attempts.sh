#!/bin/bash

# 测试用户尝试记录功能的脚本

echo "🧪 测试用户尝试记录功能"
echo "================================"

# 设置API基础URL
API_BASE="http://localhost:5000/api"
ADMIN_API_BASE="http://localhost:5000/api/admin"

echo "1. 测试用户查询（会记录尝试）"
echo "查询存在的用户..."
curl -s -X GET "$API_BASE/user/testuser" | jq '.'

echo -e "\n查询不存在的用户..."
curl -s -X GET "$API_BASE/user/nonexistentuser" | jq '.'

echo -e "\n2. 管理员登录获取token"
LOGIN_RESPONSE=$(curl -s -X POST "$ADMIN_API_BASE/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin2008"}')

TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.token')
echo "获取到token: ${TOKEN:0:20}..."

if [ "$TOKEN" = "null" ]; then
    echo "❌ 登录失败，无法继续测试"
    exit 1
fi

echo -e "\n3. 查看用户尝试记录"
curl -s -X GET "$ADMIN_API_BASE/user-attempts?limit=10" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

echo -e "\n4. 查看用户尝试统计"
curl -s -X GET "$ADMIN_API_BASE/user-attempts/stats" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

echo -e "\n5. 查看系统统计（包含尝试统计）"
curl -s -X GET "$ADMIN_API_BASE/stats" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

echo -e "\n✅ 测试完成！"
echo "请检查管理后台的'用户尝试记录'标签页查看详细信息"
