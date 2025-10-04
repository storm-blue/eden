#!/bin/bash

# 居住地点功能测试脚本

echo "=== Eden抽奖系统居住地点功能测试 ==="
echo ""

# 检查后端API是否可用
echo "=== 测试后端API ==="

# 测试获取居住人员API
echo "1. 测试获取城堡居住人员："
curl -s "http://localhost:5000/api/residence/residents/castle" | jq '.' || echo "API暂不可用，请启动后端服务"

echo ""
echo "2. 测试获取市政厅居住人员："
curl -s "http://localhost:5000/api/residence/residents/city_hall" | jq '.' || echo "API暂不可用，请启动后端服务"

echo ""
echo "=== 功能测试步骤 ==="
echo "1. 启动后端服务（cd eden-server && mvn spring-boot:run）"
echo "2. 启动前端服务（cd eden-web && npm run dev）"
echo "3. 在浏览器中打开应用"
echo "4. 输入用户名并进入星星城"
echo "5. 点击任意建筑白圈"
echo "6. 观察弹窗中是否显示当前居住人员"

echo ""
echo "=== 预期功能 ==="
echo "✓ 点击建筑时显示加载动画"
echo "✓ 显示当前居住人员数量"
echo "✓ 列出所有居住人员姓名"
echo "✓ 如果无人居住显示'暂无居住人员'"
echo "✓ 用户可以选择在该地点居住"

echo ""
echo "=== 数据库查询测试 ==="
if [ -f "eden-server/eden_lottery.db" ]; then
    echo "当前数据库中的用户居住情况："
    sqlite3 "eden-server/eden_lottery.db" "
    SELECT 
        residence,
        COUNT(*) as resident_count,
        GROUP_CONCAT(user_id, ', ') as residents
    FROM users 
    WHERE residence IS NOT NULL 
    GROUP BY residence
    ORDER BY resident_count DESC;
    " || echo "查询失败"
else
    echo "数据库文件不存在，请先启动后端服务"
fi
