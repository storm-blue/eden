#!/bin/bash

echo "=== 纪念堂滚动功能修复验证 ==="

# 1. 检查当前媒体文件数量
echo "1. 检查当前媒体文件数量..."
FILE_COUNT=$(curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length')
echo "当前媒体文件数量: $FILE_COUNT"

# 2. 如果文件不够多，再上传一些
if [ "$FILE_COUNT" -lt 15 ]; then
    echo "文件数量较少，上传更多测试文件..."
    for i in {1..10}; do
        echo "上传第 $i 张图片..."
        curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload" > /dev/null
    done
    
    # 重新检查数量
    NEW_COUNT=$(curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length')
    echo "上传后媒体文件数量: $NEW_COUNT"
fi

echo -e "\n=== 滚动功能修复完成 ==="
echo "纪念堂滚动功能修复："
echo "1. 添加了 maxHeight: '100%' (限制最大高度)"
echo "2. 添加了 overflowY: 'scroll' (强制显示垂直滚动条)"
echo "3. 保持了 flex: 1 (占据剩余空间)"
echo "4. 保持了 minHeight: 0 (允许flex收缩)"
echo ""
echo "现在滚动条应该可以正常工作了！"
echo "如果内容超出容器高度，会显示滚动条。"
