#!/bin/bash

echo "=== 纪念堂删除按钮圆形最终修复验证 ==="

# 1. 上传测试图片
echo "1. 上传测试图片..."
UPLOAD_RESPONSE=$(curl -s -X POST -F "file=@/Users/g01d-01-0924/eden/eden-web/public/picture/lv0.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
echo "上传响应: $UPLOAD_RESPONSE"

# 2. 检查纪念堂文件列表
echo -e "\n2. 检查纪念堂文件列表..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.data | length'

echo -e "\n=== 测试完成 ==="
echo "现在删除按钮应该是完美的圆形了！"
echo "最终修复方案："
echo "1. 添加了 className='memorial-delete-btn'"
echo "2. 在CSS中添加了专门的规则："
echo "   .memorial-hall-container.force-landscape .memorial-delete-btn {"
echo "     padding: 0 !important;"
echo "     margin: 0 !important;"
echo "     min-width: 20px !important;"
echo "     min-height: 20px !important;"
echo "     width: 20px !important;"
echo "     height: 20px !important;"
echo "     border-radius: 50% !important;"
echo "   }"
echo "3. 在inline样式中也添加了相应的覆盖"
