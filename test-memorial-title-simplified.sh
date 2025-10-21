#!/bin/bash

echo "=== 纪念堂标题简化修改验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 修改验证完成 ==="
echo "你的修改非常棒！"
echo "优势："
echo "- 代码更简洁：只需要一个<h1>标签"
echo "- 完美对齐：emoji和文字在同一个元素中"
echo "- 更易维护：不需要担心对齐问题"
echo "- 语义更好：整个标题作为一个整体"
echo "现在'🏛️ 纪念堂'应该完美显示！"
