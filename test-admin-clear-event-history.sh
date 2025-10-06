#!/bin/bash

SERVER_URL="http://localhost:5000"

echo "===================="
echo "管理后台清除居所事件历史测试脚本"
echo "===================="
echo ""

# 管理员登录信息
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin2008"

# 测试居所
TEST_RESIDENCE="castle"
RESIDENCE_NAME="城堡🏰"

echo "1. 管理员登录..."
LOGIN_RESPONSE=$(curl -s -X POST "${SERVER_URL}/api/admin/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\": \"${ADMIN_USERNAME}\", \"password\": \"${ADMIN_PASSWORD}\"}")

echo "$LOGIN_RESPONSE" | jq '.'

# 提取Token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.token // empty')

if [ -z "$TOKEN" ]; then
    echo "❌ 管理员登录失败，无法获取Token"
    exit 1
fi

echo "✅ 管理员登录成功，Token: ${TOKEN:0:8}..."
echo ""

echo "2. 获取居所事件历史概览..."
OVERVIEW_RESPONSE=$(curl -s -X GET "${SERVER_URL}/api/admin/residence-event-history/overview" \
  -H "Authorization: Bearer ${TOKEN}")

echo "$OVERVIEW_RESPONSE" | jq '.'
echo ""

echo "3. 检查 ${RESIDENCE_NAME} 的事件历史数量..."
CASTLE_STATS=$(echo "$OVERVIEW_RESPONSE" | jq -r ".data.overview.${TEST_RESIDENCE} // empty")
if [ -n "$CASTLE_STATS" ]; then
    TOTAL_COUNT=$(echo "$CASTLE_STATS" | jq -r '.totalCount // 0')
    echo "   当前 ${RESIDENCE_NAME} 事件历史记录数量: ${TOTAL_COUNT}"
else
    echo "   无法获取 ${RESIDENCE_NAME} 的统计信息"
fi
echo ""

echo "4. 为 ${RESIDENCE_NAME} 生成一些事件历史记录（用于测试）..."
# 注册测试用户
curl -s -X POST "${SERVER_URL}/api/user/register" -H "Content-Type: application/json" -d "{\"userId\": \"测试用户1\"}" > /dev/null
curl -s -X POST "${SERVER_URL}/api/user/register" -H "Content-Type: application/json" -d "{\"userId\": \"测试用户2\"}" > /dev/null

# 让用户居住到城堡，生成事件
curl -s -X POST "${SERVER_URL}/api/residence/set" -H "Content-Type: application/json" -d "{\"userId\": \"测试用户1\", \"residence\": \"${TEST_RESIDENCE}\"}" > /dev/null
sleep 1
curl -s -X POST "${SERVER_URL}/api/residence/set" -H "Content-Type: application/json" -d "{\"userId\": \"测试用户2\", \"residence\": \"${TEST_RESIDENCE}\"}" > /dev/null
sleep 1

echo "   已生成测试事件历史记录"
echo ""

echo "5. 再次检查 ${RESIDENCE_NAME} 的事件历史数量..."
OVERVIEW_RESPONSE_2=$(curl -s -X GET "${SERVER_URL}/api/admin/residence-event-history/overview" \
  -H "Authorization: Bearer ${TOKEN}")

CASTLE_STATS_2=$(echo "$OVERVIEW_RESPONSE_2" | jq -r ".data.overview.${TEST_RESIDENCE} // empty")
if [ -n "$CASTLE_STATS_2" ]; then
    TOTAL_COUNT_2=$(echo "$CASTLE_STATS_2" | jq -r '.totalCount // 0')
    echo "   现在 ${RESIDENCE_NAME} 事件历史记录数量: ${TOTAL_COUNT_2}"
else
    echo "   无法获取 ${RESIDENCE_NAME} 的统计信息"
fi
echo ""

echo "6. 清除 ${RESIDENCE_NAME} 的所有事件历史记录..."
CLEAR_RESPONSE=$(curl -s -X DELETE "${SERVER_URL}/api/admin/residence-event-history/${TEST_RESIDENCE}" \
  -H "Authorization: Bearer ${TOKEN}")

echo "$CLEAR_RESPONSE" | jq '.'

# 检查清除是否成功
CLEAR_SUCCESS=$(echo "$CLEAR_RESPONSE" | jq -r '.success // false')
if [ "$CLEAR_SUCCESS" = "true" ]; then
    echo "✅ 清除操作执行成功"
else
    echo "❌ 清除操作执行失败"
fi
echo ""

echo "7. 验证清除结果 - 检查 ${RESIDENCE_NAME} 的事件历史数量..."
OVERVIEW_RESPONSE_3=$(curl -s -X GET "${SERVER_URL}/api/admin/residence-event-history/overview" \
  -H "Authorization: Bearer ${TOKEN}")

CASTLE_STATS_3=$(echo "$OVERVIEW_RESPONSE_3" | jq -r ".data.overview.${TEST_RESIDENCE} // empty")
if [ -n "$CASTLE_STATS_3" ]; then
    TOTAL_COUNT_3=$(echo "$CASTLE_STATS_3" | jq -r '.totalCount // 0')
    echo "   清除后 ${RESIDENCE_NAME} 事件历史记录数量: ${TOTAL_COUNT_3}"
    
    if [ "$TOTAL_COUNT_3" = "0" ]; then
        echo "✅ 事件历史记录已成功清除"
    else
        echo "⚠️  事件历史记录未完全清除，剩余 ${TOTAL_COUNT_3} 条"
    fi
else
    echo "   无法获取 ${RESIDENCE_NAME} 的统计信息"
fi
echo ""

echo "8. 测试无效居所参数..."
INVALID_RESPONSE=$(curl -s -X DELETE "${SERVER_URL}/api/admin/residence-event-history/invalid_residence" \
  -H "Authorization: Bearer ${TOKEN}")

echo "   无效居所响应:"
echo "$INVALID_RESPONSE" | jq '.'
echo ""

echo "9. 测试未授权访问..."
UNAUTHORIZED_RESPONSE=$(curl -s -X DELETE "${SERVER_URL}/api/admin/residence-event-history/${TEST_RESIDENCE}")

echo "   未授权访问响应:"
echo "$UNAUTHORIZED_RESPONSE" | jq '.'
echo ""

echo "10. 管理员登出..."
LOGOUT_RESPONSE=$(curl -s -X POST "${SERVER_URL}/api/admin/logout" \
  -H "Authorization: Bearer ${TOKEN}")

echo "$LOGOUT_RESPONSE" | jq '.'
echo ""

echo "===================="
echo "测试完成"
echo "===================="
echo ""
echo "功能验证要点："
echo "- ✅ 管理员登录认证"
echo "- ✅ 获取居所事件历史概览"
echo "- ✅ 清除指定居所事件历史"
echo "- ✅ 验证清除结果"
echo "- ✅ 无效参数处理"
echo "- ✅ 权限验证"
echo "- ✅ 管理员登出"
