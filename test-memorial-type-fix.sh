#!/bin/bash

echo "=== 纪念堂图片类型判断修复验证 ==="

# 1. 检查现有文件类型
echo "1. 检查现有文件类型..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data[] | {id, fileName, fileType, url}'

# 2. 上传新图片测试
echo -e "\n2. 上传新图片测试..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 3. 验证新文件的类型
echo -e "\n3. 验证新文件的类型..."
NEW_FILE_ID=$(echo $UPLOAD_RESPONSE | jq -r '.data.id')
curl -s "http://localhost:5000/api/memorial/list" | jq '.data[] | select(.id == '$NEW_FILE_ID') | {id, fileName, fileType, url}'

echo -e "\n=== 测试完成 ==="
echo "现在前端应该能正确识别图片类型了！"
echo "前端使用: media.fileType === 'image' 来判断是否为图片"
