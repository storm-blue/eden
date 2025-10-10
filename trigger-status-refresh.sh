#!/bin/bash

# 手动触发用户状态刷新

BASE_URL="http://localhost:8080"

echo "========================================="
echo "手动触发用户状态刷新"
echo "========================================="
echo ""

# 1. 登录获取token
echo "1️⃣  登录中..."
TOKEN=$(curl -s -X POST "${BASE_URL}/api/admin/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}' | jq -r '.data.token')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo "❌ 登录失败，请检查管理员账号密码"
    exit 1
fi

echo "✅ 登录成功"
echo ""

# 2. 查看当前用户状态
echo "2️⃣  当前用户状态："
echo "-----------------------------------------"
curl -s "${BASE_URL}/api/admin/users" | jq -r '.data[] | "  \(.userId): 居所=\(.residence // "无"), 状态=\(.status // "无")"'
echo ""

# 3. 触发状态刷新
echo "3️⃣  触发状态刷新任务..."
RESULT=$(curl -s -X POST "${BASE_URL}/api/admin/trigger-status-refresh" \
    -H "Authorization: Bearer $TOKEN")

echo "$RESULT" | jq .
echo ""

# 4. 查看刷新后的用户状态
echo "4️⃣  刷新后用户状态："
echo "-----------------------------------------"
curl -s "${BASE_URL}/api/admin/users" | jq -r '.data[] | "  \(.userId): 居所=\(.residence // "无"), 状态=\(.status // "无")"'
echo ""

echo "========================================="
echo "✅ 完成"
echo "========================================="

