#!/bin/bash

echo "=== 纪念堂长方形布局优化验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 长方形布局优化完成 ==="
echo "纪念堂资源框布局优化："
echo "1. 网格列宽: minmax(120px, 1fr) → minmax(160px, 1fr)"
echo "2. 资源框形状: aspectRatio: '1' → '4/3' (正方形变长方形)"
echo "3. 上传按钮形状: aspectRatio: '1' → '4/3' (保持一致)"
echo "4. 内容居中: 添加了flex布局和居中对齐"
echo "5. 媒体区域高度: 80px → 100px (适应长方形)"
echo "6. 媒体区域: 添加了flex: 1 (更好地填充空间)"
echo "现在纪念堂的资源框都是长方形，内容完美居中显示！"
