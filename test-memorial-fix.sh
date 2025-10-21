#!/bin/bash

echo "=== 纪念堂修复验证测试 ==="

# 测试纪念堂API
echo "1. 测试纪念堂文件列表API..."
curl -s "http://localhost:5000/api/memorial/list" | jq '.success, .data[0].url'

echo -e "\n2. 测试纪念堂文件访问..."
curl -s -I "http://localhost:5000/uploads/memorial/memorial_1761075095766_f8119849.png" | head -5

echo -e "\n3. 对比头像文件访问..."
curl -s -I "http://localhost:5000/uploads/avatars/avatar_1761074508514_a43cba0d.jpg" | head -5

echo -e "\n4. 测试纪念堂统计API..."
curl -s "http://localhost:5000/api/memorial/stats" | jq '.success, .data.totalCount'

echo -e "\n=== 测试完成 ==="
echo "如果所有测试都返回200状态码，说明纪念堂功能已修复！"
