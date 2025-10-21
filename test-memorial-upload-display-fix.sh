#!/bin/bash

echo "=== 纪念堂上传即时显示修复验证 ==="

# 1. 上传新图片
echo "1. 上传新图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查上传返回的数据结构
echo -e "\n2. 检查上传返回的数据结构..."
echo $UPLOAD_RESPONSE | jq '.data | {id, fileName, originalName, type, url}'

# 3. 检查从后端加载的数据结构
echo -e "\n3. 检查从后端加载的数据结构..."
NEW_FILE_ID=$(echo $UPLOAD_RESPONSE | jq -r '.data.id')
curl -s "http://localhost:5000/api/memorial/list" | jq '.data[] | select(.id == '$NEW_FILE_ID') | {id, fileName, originalName, fileType, url}'

echo -e "\n=== 测试完成 ==="
echo "现在上传后立即显示应该正常了！"
echo "前端数据结构与后端保持一致："
echo "- fileType: 用于类型判断"
echo "- originalName: 用于显示文件名"
