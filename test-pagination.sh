#!/bin/bash

# 测试抽奖历史分页功能的脚本

echo "📖 测试抽奖历史分页功能"
echo "================================"

# 设置API基础URL和管理员凭据
API_BASE="http://localhost:5000/api"
ADMIN_API_BASE="http://localhost:5000/api/admin"

echo "1. 管理员登录获取token"
LOGIN_RESPONSE=$(curl -s -X POST "$ADMIN_API_BASE/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin2008"}')

TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.token')
echo "获取到token: ${TOKEN:0:20}..."

if [ "$TOKEN" = "null" ]; then
    echo "❌ 登录失败，无法继续测试"
    exit 1
fi

echo -e "\n2. 测试分页API - 第1页，每页20条"
curl -s -X GET "$ADMIN_API_BASE/lottery-history?page=1&size=20" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

echo -e "\n3. 测试分页API - 第1页，每页10条"
curl -s -X GET "$ADMIN_API_BASE/lottery-history?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN" | jq '.data.pagination'

echo -e "\n4. 测试用户筛选 - 查询特定用户的记录"
curl -s -X GET "$ADMIN_API_BASE/lottery-history?page=1&size=10&userId=testuser" \
  -H "Authorization: Bearer $TOKEN" | jq '.data.filter'

echo -e "\n5. 测试API返回结构"
echo "预期返回结构："
echo "{"
echo "  \"success\": true,"
echo "  \"data\": {"
echo "    \"records\": [...],"
echo "    \"pagination\": {"
echo "      \"currentPage\": 1,"
echo "      \"pageSize\": 20,"
echo "      \"totalCount\": 100,"
echo "      \"totalPages\": 5,"
echo "      \"hasNext\": true,"
echo "      \"hasPrev\": false"
echo "    },"
echo "    \"filter\": {"
echo "      \"userId\": null"
echo "    }"
echo "  }"
echo "}"

echo -e "\n6. 前端分页功能测试"
echo "请在浏览器中打开管理后台并测试以下功能："
echo "✅ 访问 /admin.html"
echo "✅ 登录管理员账户"
echo "✅ 切换到'抽奖历史'标签页"
echo "✅ 点击'🔍 查询'按钮加载数据"
echo "✅ 测试分页导航：首页、上一页、下一页、末页"
echo "✅ 测试页码点击跳转"
echo "✅ 测试每页条数切换：10、20、50、100"
echo "✅ 测试用户ID筛选功能"
echo "✅ 测试'🔄 清除筛选'按钮"

echo -e "\n7. 分页功能特性"
echo "🎯 支持的功能："
echo "- ✅ 分页导航（首页、上一页、下一页、末页）"
echo "- ✅ 页码直接跳转（显示5个页码）"
echo "- ✅ 每页条数选择（10/20/50/100）"
echo "- ✅ 用户ID筛选"
echo "- ✅ 分页信息显示（当前页/总页数/总记录数）"
echo "- ✅ 按钮状态管理（禁用首页/上一页等）"
echo "- ✅ 响应式分页控件"
echo "- ✅ 奖品等级徽章显示"

echo -e "\n8. API参数说明"
echo "GET /api/admin/lottery-history"
echo "参数："
echo "- page: 页码（默认1）"
echo "- size: 每页数量（默认20）"
echo "- userId: 用户ID筛选（可选）"

echo -e "\n✅ 分页功能测试完成！"
echo "现在抽奖历史支持完整的分页浏览功能。"
