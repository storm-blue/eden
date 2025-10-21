#!/bin/bash

echo "=== 纪念堂路径修复验证测试 ==="

# 1. 上传新文件测试
echo "1. 上传新文件测试..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 提取文件ID和URL
FILE_ID=$(echo $UPLOAD_RESPONSE | jq -r '.data.id')
FILE_URL=$(echo $UPLOAD_RESPONSE | jq -r '.data.url')
echo "文件ID: $FILE_ID"
echo "文件URL: $FILE_URL"

# 2. 检查数据库存储
echo -e "\n2. 检查数据库存储..."
cd /Users/g01d-01-0924/eden/eden-server
DB_RESULT=$(sqlite3 eden_lottery.db "SELECT id, file_name, url FROM memorial_media WHERE id = $FILE_ID;")
echo "数据库记录: $DB_RESULT"

# 3. 测试文件访问
echo -e "\n3. 测试文件访问..."
curl -s -I "http://localhost:5000$FILE_URL" | head -3

# 4. 测试文件列表API
echo -e "\n4. 测试文件列表API..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data[] | select(.id == '$FILE_ID') | {id, fileName, url}'

# 5. 测试删除功能
echo -e "\n5. 测试删除功能..."
DELETE_RESPONSE=$(curl -s -X DELETE "http://localhost:5000/api/memorial/delete/$FILE_ID")
echo "删除响应: $DELETE_RESPONSE"

# 6. 验证文件已删除
echo -e "\n6. 验证文件已删除..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 测试完成 ==="
echo "如果所有步骤都成功，说明纪念堂路径修复完成！"
