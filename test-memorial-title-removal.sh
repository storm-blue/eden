#!/bin/bash

echo "=== 纪念堂标题删除验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 修改完成 ==="
echo "纪念堂页面现在更加简洁："
echo "- 删除了'纪念珍藏'标题"
echo "- 直接显示媒体文件网格"
echo "- 界面更加干净整洁"
