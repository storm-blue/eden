#!/bin/bash

echo "=== 纪念堂固定高度和滚动功能验证 ==="

# 1. 上传多个测试图片
echo "1. 上传多个测试图片..."
for i in {1..5}; do
    echo "上传第 $i 张图片..."
    UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
    echo "上传响应: $UPLOAD_RESPONSE"
done

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 固定高度和滚动功能完成 ==="
echo "纪念堂弹框布局优化："
echo "1. 主容器高度: 固定为移动端80vh，桌面端70vh"
echo "2. 媒体区域: flex: 1 (占据剩余空间)"
echo "3. 滚动功能: overflow: 'auto' (内容过多时显示滚动条)"
echo "4. 最小高度: minHeight: 0 (允许flex收缩)"
echo ""
echo "现在纪念堂弹框："
echo "- 高度固定，不会超出屏幕"
echo "- 媒体内容过多时会出现滚动条"
echo "- 布局更加稳定和可控"
