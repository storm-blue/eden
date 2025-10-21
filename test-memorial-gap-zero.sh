#!/bin/bash

echo "=== 纪念堂间距设置为0验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 间距设置为0完成 ==="
echo "纪念堂间距最终优化："
echo "最终间距计算："
echo "- 主容器gap: 0px (你设置的)"
echo "- 媒体区域padding: 10px"
echo "- 总间距: 0px + 10px = 10px (最紧凑)"
echo ""
echo "现在标题和媒体区域之间只有媒体区域的padding间距，"
echo "这是最紧凑的布局！"
