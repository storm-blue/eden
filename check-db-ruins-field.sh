#!/bin/bash

# 直接检查数据库中的废墟状态字段
echo "🗄️ 直接检查数据库废墟状态字段"
echo "================================"

# 假设数据库文件在eden-server目录下
DB_FILE="eden-server/eden_lottery.db"

if [ ! -f "$DB_FILE" ]; then
    echo "❌ 数据库文件不存在: $DB_FILE"
    echo "请确保后端服务已启动并创建了数据库"
    exit 1
fi

echo "📊 检查star_city表结构..."
sqlite3 "$DB_FILE" ".schema star_city"

echo -e "\n📊 检查star_city表数据..."
sqlite3 "$DB_FILE" "SELECT id, population, food, happiness, level, weather, is_ruins, last_update_time FROM star_city ORDER BY id DESC LIMIT 1;"

echo -e "\n📊 检查is_ruins字段类型..."
sqlite3 "$DB_FILE" "PRAGMA table_info(star_city);" | grep is_ruins

echo -e "\n🗄️ 数据库检查完成！"
echo "如果is_ruins字段不存在或类型不正确，需要："
echo "1. 重启后端服务以执行数据库迁移"
echo "2. 检查数据库迁移代码是否正确"
