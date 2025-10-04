#!/bin/bash

# 数据库迁移测试脚本
# 用于验证residence字段的自动添加功能

echo "=== Eden抽奖系统数据库迁移测试 ==="
echo ""

# 检查SQLite数据库文件
DB_FILE="eden-server/eden_lottery.db"

if [ -f "$DB_FILE" ]; then
    echo "✓ 发现现有数据库文件: $DB_FILE"
    
    # 检查当前表结构
    echo ""
    echo "=== 当前users表结构 ==="
    sqlite3 "$DB_FILE" ".schema users"
    
    echo ""
    echo "=== 当前users表列信息 ==="
    sqlite3 "$DB_FILE" "PRAGMA table_info(users);"
    
    echo ""
    echo "=== 检查residence列是否存在 ==="
    RESIDENCE_EXISTS=$(sqlite3 "$DB_FILE" "PRAGMA table_info(users);" | grep -c "residence")
    
    if [ "$RESIDENCE_EXISTS" -gt 0 ]; then
        echo "✓ residence列已存在"
    else
        echo "✗ residence列不存在"
        echo "  -> 启动后端服务时会自动添加此列"
    fi
    
    echo ""
    echo "=== 检查wish_count列是否存在 ==="
    WISH_COUNT_EXISTS=$(sqlite3 "$DB_FILE" "PRAGMA table_info(users);" | grep -c "wish_count")
    
    if [ "$WISH_COUNT_EXISTS" -gt 0 ]; then
        echo "✓ wish_count列已存在"
    else
        echo "✗ wish_count列不存在"
        echo "  -> 启动后端服务时会自动添加此列"
    fi
    
else
    echo "✗ 未发现数据库文件: $DB_FILE"
    echo "  -> 首次启动时会自动创建数据库和表结构"
fi

echo ""
echo "=== 测试建议 ==="
echo "1. 备份现有数据库（如果存在）"
echo "2. 启动后端服务，观察日志中的迁移信息"
echo "3. 检查数据库表结构是否正确更新"
echo "4. 测试居住地点选择功能"

echo ""
echo "=== 预期的迁移日志 ==="
echo "INFO  - 开始数据库迁移检查..."
echo "INFO  - users表缺少residence列，添加列..."
echo "INFO  - residence列添加成功"
echo "INFO  - users表结构检查完成"
echo "INFO  - 数据库迁移检查完成"
