#!/bin/bash

# 测试彩虹命令功能

API_BASE="http://localhost:8080/api"

echo "🌈 测试彩虹命令功能"
echo "================================"

# 1. 获取命令列表
echo ""
echo "1️⃣ 获取命令列表..."
curl -s "${API_BASE}/decree/list?userId=秦小淮" | jq '.'

# 2. 颁布彩虹命令
echo ""
echo "2️⃣ 颁布彩虹命令..."
curl -s -X POST "${API_BASE}/decree/issue" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=CREATE_RAINBOW&userId=秦小淮" | jq '.'

# 3. 再次获取命令列表，验证命令已激活
echo ""
echo "3️⃣ 验证命令已激活..."
curl -s "${API_BASE}/decree/list?userId=秦小淮" | jq '.decrees[] | select(.code == "CREATE_RAINBOW")'

echo ""
echo "✅ 现在可以进入星星城查看彩虹效果了！"

# 等待用户操作
read -p "按回车取消彩虹命令..." 

# 4. 取消彩虹命令
echo ""
echo "4️⃣ 取消彩虹命令..."
curl -s -X POST "${API_BASE}/decree/cancel" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=CREATE_RAINBOW&userId=秦小淮" | jq '.'

# 5. 最后验证
echo ""
echo "5️⃣ 验证命令已取消..."
curl -s "${API_BASE}/decree/list?userId=秦小淮" | jq '.decrees[] | select(.code == "CREATE_RAINBOW")'

echo ""
echo "🎉 测试完成！"

