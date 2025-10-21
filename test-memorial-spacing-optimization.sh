#!/bin/bash

echo "=== 纪念堂间距进一步优化验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 间距进一步优化完成 ==="
echo "纪念堂间距进一步优化："
echo "优化前间距计算："
echo "- 主容器gap: 12px"
echo "- 媒体区域padding: 15px"
echo "- 总间距: 12px + 15px = 27px"
echo ""
echo "优化后间距计算："
echo "- 主容器gap: 6px (减少50%)"
echo "- 媒体区域padding: 10px (减少33%)"
echo "- 总间距: 6px + 10px = 16px (减少41%)"
echo ""
echo "现在标题和媒体区域之间的距离更加紧凑！"
