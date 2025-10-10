#!/bin/bash

# 用户状态刷新功能测试脚本

echo "========================================="
echo "用户状态刷新功能测试"
echo "========================================="
echo ""

BASE_URL="http://localhost:8080"

echo "1️⃣  查看所有用户当前状态"
echo "-----------------------------------------"
curl -s "${BASE_URL}/api/admin/users" | jq -r '.data[] | "用户: \(.userId), 居所: \(.residence // "无"), 状态: \(.status // "无")"'
echo ""

echo ""
echo "2️⃣  手动触发状态刷新"
echo "-----------------------------------------"
echo "请先登录获取管理员token："
echo "TOKEN=\$(curl -s -X POST \"${BASE_URL}/api/admin/login\" -H \"Content-Type: application/json\" -d '{\"username\":\"admin\",\"password\":\"admin123\"}' | jq -r '.data.token')"
echo ""
echo "然后执行手动刷新："
echo "curl -X POST \"${BASE_URL}/api/admin/trigger-status-refresh\" -H \"Authorization: Bearer \$TOKEN\" | jq ."
echo ""

echo ""
echo "3️⃣  实时监控日志"
echo "-----------------------------------------"
echo "运行以下命令监控状态刷新日志："
echo "tail -f eden-server/logs/*.log | grep -E '(用户状态|UserStatusRefresh)'"
echo ""

echo ""
echo "4️⃣  查询数据库验证"
echo "-----------------------------------------"
echo "进入数据库查看用户状态："
echo "sqlite3 eden-server/eden_lottery.db"
echo "SELECT user_id, residence, status, update_time FROM users ORDER BY update_time DESC;"
echo ""

echo ""
echo "========================================="
echo "定时任务执行时间"
echo "========================================="
echo "用户状态刷新：每30分钟（每小时的0分和30分）"
echo "下次执行时间："
current_minute=$(date +%M)
if [ $current_minute -lt 30 ]; then
    echo "  - 今天 $(date +%H):30"
else
    next_hour=$(($(date +%H) + 1))
    if [ $next_hour -ge 24 ]; then
        next_hour=0
    fi
    printf "  - 今天 %02d:00\n" $next_hour
fi
echo ""

echo "========================================="
echo "实现状态决定逻辑"
echo "========================================="
echo "编辑文件："
echo "  eden-server/src/main/java/com/eden/lottery/service/UserStatusService.java"
echo ""
echo "修改方法："
echo "  public String determineUserStatus(String userId, String residence)"
echo ""
echo "详细文档："
echo "  eden-server/USER_STATUS_REFRESH.md"
echo ""

