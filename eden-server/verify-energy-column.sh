#!/bin/bash

# 验证 energy_refresh_time 列修复的脚本

DB_PATH="/Users/g01d-01-0924/eden/eden-server/eden_lottery.db"

echo "🔍 验证精力系统数据库迁移"
echo "================================"
echo ""

if [ ! -f "$DB_PATH" ]; then
    echo "❌ 数据库文件不存在: $DB_PATH"
    exit 1
fi

echo "✅ 数据库文件存在"
echo ""

echo "📋 检查 users 表结构:"
echo "-----------------------------------"
sqlite3 "$DB_PATH" "PRAGMA table_info(users);" | grep -E "(energy|max_energy|energy_refresh_time)" || echo "⚠️  未找到精力相关列"
echo ""

echo "📊 检查现有用户的精力数据:"
echo "-----------------------------------"
sqlite3 "$DB_PATH" <<EOF
.mode column
.headers on
SELECT 
    user_id AS '用户ID', 
    energy AS '精力', 
    max_energy AS '最大精力', 
    energy_refresh_time AS '刷新时间'
FROM users
LIMIT 10;
EOF
echo ""

echo "🔢 统计精力数据:"
echo "-----------------------------------"
echo "总用户数:"
sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM users;"

echo ""
echo "精力不为NULL的用户数:"
sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM users WHERE energy IS NOT NULL;"

echo ""
echo "energy_refresh_time不为NULL的用户数:"
sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM users WHERE energy_refresh_time IS NOT NULL;"

echo ""
echo "精力为15的用户数:"
sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM users WHERE energy = 15;"

echo ""
echo "================================"
echo "✅ 验证完成！"
echo ""
echo "💡 如果所有用户的 energy_refresh_time 都不为 NULL，说明修复成功！"

