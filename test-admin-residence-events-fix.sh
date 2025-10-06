#!/bin/bash

SERVER_URL="http://localhost:5000"

echo "===================="
echo "测试居所事件历史概览API修复"
echo "===================="
echo ""

# 管理员登录信息
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin2008"

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

echo "2. 测试居所事件历史概览API..."
OVERVIEW_RESPONSE=$(curl -s -X GET "${SERVER_URL}/api/admin/residence-event-history/overview" \
  -H "Authorization: Bearer ${TOKEN}")

echo "响应结果："
echo "$OVERVIEW_RESPONSE" | jq '.'

# 检查是否成功
SUCCESS=$(echo "$OVERVIEW_RESPONSE" | jq -r '.success // false')
if [ "$SUCCESS" = "true" ]; then
    echo ""
    echo "✅ API调用成功！"
    
    # 检查数据结构
    echo ""
    echo "3. 验证数据结构..."
    
    # 检查是否包含预期的居所
    CASTLE_DATA=$(echo "$OVERVIEW_RESPONSE" | jq -r '.data.overview.castle // empty')
    if [ -n "$CASTLE_DATA" ]; then
        echo "✅ 城堡数据存在"
        
        # 检查字段
        TOTAL_COUNT=$(echo "$CASTLE_DATA" | jq -r '.totalCount // empty')
        RESIDENCE_NAME=$(echo "$CASTLE_DATA" | jq -r '.residenceName // empty')
        
        if [ -n "$TOTAL_COUNT" ]; then
            echo "✅ totalCount字段存在: $TOTAL_COUNT"
        else
            echo "❌ totalCount字段缺失"
        fi
        
        if [ -n "$RESIDENCE_NAME" ]; then
            echo "✅ residenceName字段存在: $RESIDENCE_NAME"
        else
            echo "❌ residenceName字段缺失"
        fi
    else
        echo "❌ 城堡数据缺失"
    fi
    
else
    echo ""
    echo "❌ API调用失败"
    ERROR_MSG=$(echo "$OVERVIEW_RESPONSE" | jq -r '.message // "未知错误"')
    echo "错误信息: $ERROR_MSG"
fi

echo ""
echo "4. 管理员登出..."
LOGOUT_RESPONSE=$(curl -s -X POST "${SERVER_URL}/api/admin/logout" \
  -H "Authorization: Bearer ${TOKEN}")

echo "$LOGOUT_RESPONSE" | jq '.'

echo ""
echo "===================="
echo "测试完成"
echo "===================="

if [ "$SUCCESS" = "true" ]; then
    echo "🎉 居所事件历史概览API修复成功！"
    echo "现在可以在管理界面中正常使用居所事件管理功能了。"
else
    echo "⚠️  API仍有问题，请检查后端日志获取更多信息。"
fi
