#!/bin/bash

# 验证彩虹命令是否已正确初始化到数据库

echo "🔍 验证彩虹命令初始化状态"
echo "================================"

DB_PATH="eden-server/lottery.db"

# 检查数据库文件是否存在
if [ ! -f "$DB_PATH" ]; then
    echo "❌ 数据库文件不存在: $DB_PATH"
    echo "   请先启动后端服务"
    exit 1
fi

echo ""
echo "1️⃣ 检查 decree 表是否存在..."
TABLE_EXISTS=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='decree';")

if [ "$TABLE_EXISTS" -eq 0 ]; then
    echo "❌ decree 表不存在"
    echo "   请启动后端服务以创建表"
    exit 1
else
    echo "✅ decree 表存在"
fi

echo ""
echo "2️⃣ 检查所有命令记录..."
sqlite3 "$DB_PATH" << EOF
.mode column
.headers on
SELECT code, name, active FROM decree;
EOF

echo ""
echo "3️⃣ 检查 CREATE_RAINBOW 命令..."
RAINBOW_EXISTS=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM decree WHERE code='CREATE_RAINBOW';")

if [ "$RAINBOW_EXISTS" -eq 0 ]; then
    echo "❌ CREATE_RAINBOW 命令不存在"
    echo ""
    echo "📝 修复方法："
    echo "   方法1（推荐）: 重启后端服务，应用会自动添加"
    echo "   方法2: 手动执行以下SQL："
    echo ""
    echo "   sqlite3 $DB_PATH << 'SQLEOF'"
    echo "   INSERT INTO decree (code, name, description, active)"
    echo "   VALUES ('CREATE_RAINBOW', '创造彩虹',"
    echo "           '让星星城上空出现美丽的彩虹！彩虹将横跨整个星星城，为所有居民带来美好的祝福。', 0);"
    echo "   SQLEOF"
    exit 1
else
    echo "✅ CREATE_RAINBOW 命令存在"
    
    # 显示详细信息
    echo ""
    echo "   详细信息:"
    sqlite3 "$DB_PATH" << EOF
.mode line
SELECT * FROM decree WHERE code='CREATE_RAINBOW';
EOF
fi

echo ""
echo "4️⃣ 检查 NO_CASTLE_ACCESS 命令..."
CASTLE_EXISTS=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM decree WHERE code='NO_CASTLE_ACCESS';")

if [ "$CASTLE_EXISTS" -eq 0 ]; then
    echo "⚠️  NO_CASTLE_ACCESS 命令不存在（可选）"
else
    echo "✅ NO_CASTLE_ACCESS 命令存在"
fi

echo ""
echo "================================"
echo "✅ 验证完成！"
echo ""
echo "📚 相关文档:"
echo "   - DECREE_INIT_FIX.md - 初始化问题修复说明"
echo "   - RAINBOW_DECREE.md - 彩虹命令功能文档"
echo "   - test-rainbow-decree.sh - 功能测试脚本"

