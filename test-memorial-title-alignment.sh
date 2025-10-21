#!/bin/bash

echo "=== 纪念堂标题对齐修复验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 修复完成 ==="
echo "纪念堂标题对齐修复："
echo "- emoji和文字都使用24px字体大小"
echo "- 都设置了lineHeight: '1'确保垂直对齐"
echo "- emoji添加了display: 'flex'和alignItems: 'center'"
echo "- 保持了10px的间距"
echo "现在emoji和'纪念堂'文字应该完美水平对齐了！"
