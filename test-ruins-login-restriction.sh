#!/bin/bash

# 测试废墟状态下用户登录限制
echo "🔐 测试废墟状态下用户登录限制"
echo "================================"

API_BASE="http://localhost:5000/api"

echo "1. 获取当前星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n2. 设置废墟状态为true（只允许秦小淮和李星斗登录）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n3. 再次获取星星城信息（确认废墟状态）..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n4. 设置废墟状态为false（恢复正常登录）..."
curl -s -X POST "${API_BASE}/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": false}' | jq '.' || echo "设置废墟状态失败"

echo -e "\n5. 最终检查星星城信息..."
curl -s "${API_BASE}/star-city/info" | jq '.data | {level, isRuins, weather}' || echo "获取星星城信息失败"

echo -e "\n🔐 废墟状态下用户登录限制测试完成！"
echo ""
echo "前端测试说明："
echo "1. 设置废墟状态为true"
echo "2. 刷新页面"
echo "3. 尝试输入不同的用户名："
echo "   - 输入'秦小淮'：应该能正常登录"
echo "   - 输入'李星斗'：应该能正常登录"
echo "   - 输入其他用户名（如'张三'）：应该显示'用户不存在'"
echo "4. 设置废墟状态为false"
echo "5. 刷新页面"
echo "6. 尝试输入任何用户名：应该都能正常登录"
echo ""
echo "预期结果："
echo "- 废墟状态下：只有秦小淮和李星斗可以登录，其他人显示'用户不存在'"
echo "- 正常状态下：所有用户都可以正常登录"
echo ""
echo "功能说明："
echo "- 允许用户：秦小淮、李星斗"
echo "- 拒绝用户：除秦小淮和李星斗外的所有用户"
echo "- 拒绝效果：显示'用户不存在'，无法进行任何操作"
echo "- 检查时机：用户输入姓名时和获取用户信息时"
echo ""
echo "技术实现："
echo "- fetchUserInfo函数：废墟状态下检查用户名白名单"
echo "- handleNameConfirm函数：用户确认姓名时检查白名单"
echo "- 白名单：['秦小淮', '李星斗']"
echo "- 拒绝处理：设置userInfo.message为'用户不存在'"
