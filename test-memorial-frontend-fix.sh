#!/bin/bash

echo "=== 纪念堂前端图片显示修复验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 提取文件URL
FILE_URL=$(echo $UPLOAD_RESPONSE | jq -r '.data.url')
echo "文件URL: $FILE_URL"

# 2. 测试完整URL访问
echo -e "\n2. 测试完整URL访问..."
FULL_URL="http://localhost:5000$FILE_URL"
echo "完整URL: $FULL_URL"
curl -s -I "$FULL_URL" | head -3

# 3. 测试纪念堂列表API
echo -e "\n3. 测试纪念堂列表API..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data[] | select(.url == "'$FILE_URL'") | {id, fileName, url}'

echo -e "\n=== 测试完成 ==="
echo "现在前端应该能正确显示图片了！"
echo "前端会使用: window.location.origin + media.url"
echo "即: http://localhost:5000 + $FILE_URL = $FULL_URL"
