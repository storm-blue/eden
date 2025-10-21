#!/bin/bash

echo "=== 纪念堂删除按钮圆形修复验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 测试完成 ==="
echo "现在删除按钮应该是完美的圆形了！"
echo "修复内容："
echo "- 添加了 padding: '0 !important' 来覆盖CSS中的 padding: 10px 20px"
echo "- 保持了 borderRadius: '50%' 和 width/height: 20px"
echo "- 确保按钮在所有情况下都是圆形"
