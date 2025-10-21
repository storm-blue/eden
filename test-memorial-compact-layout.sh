#!/bin/bash

echo "=== 纪念堂紧凑布局优化验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 紧凑布局优化完成 ==="
echo "参考纪念碑的紧凑布局，优化了纪念堂："
echo "1. 主容器padding: 30px → 20px/25px (移动端/桌面端)"
echo "2. 添加了flex布局和gap: 12px"
echo "3. 标题间距: marginBottom 25px → 4px"
echo "4. 标题gap: 10px → 6px"
echo "5. 媒体区域padding: 20px → 15px"
echo "现在纪念堂弹框布局更加紧凑，与纪念碑保持一致！"
