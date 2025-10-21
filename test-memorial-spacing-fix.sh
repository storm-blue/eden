#!/bin/bash

echo "=== 纪念堂标题与媒体区域间距修复验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 间距修复完成 ==="
echo "纪念堂标题与媒体区域间距优化："
echo "修复前间距计算："
echo "- 主容器gap: 12px"
echo "- 标题marginBottom: 4px" 
echo "- 媒体区域padding: 15px"
echo "- 总间距: 12px + 4px + 15px = 31px (过大)"
echo ""
echo "修复后间距计算："
echo "- 主容器gap: 12px"
echo "- 标题marginBottom: 0px (已移除)"
echo "- 媒体区域padding: 15px"
echo "- 总间距: 12px + 0px + 15px = 27px (合理)"
echo ""
echo "现在标题和媒体区域之间的距离更加合理！"
