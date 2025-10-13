#!/bin/bash

echo "===== Eden命令系统测试脚本 ====="
echo ""

BASE_URL="http://localhost:8080"

echo "1. 获取命令列表（秦小淮）"
curl -X GET "${BASE_URL}/api/decree/list?userId=秦小淮"
echo -e "\n"

echo "2. 颁布命令：不得靠近城堡"
curl -X POST "${BASE_URL}/api/decree/issue" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=NO_CASTLE_ACCESS&userId=秦小淮"
echo -e "\n"

echo "3. 再次获取命令列表（查看状态）"
curl -X GET "${BASE_URL}/api/decree/list?userId=秦小淮"
echo -e "\n"

echo "4. 查看城堡居民（应该只剩李星斗）"
curl -X GET "${BASE_URL}/api/residence-event/castle"
echo -e "\n"

echo "5. 取消命令：不得靠近城堡"
curl -X POST "${BASE_URL}/api/decree/cancel" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=NO_CASTLE_ACCESS&userId=秦小淮"
echo -e "\n"

echo "6. 最后查看命令列表"
curl -X GET "${BASE_URL}/api/decree/list?userId=秦小淮"
echo -e "\n"

echo "===== 测试完成 ====="

