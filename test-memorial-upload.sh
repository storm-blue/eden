#!/bin/bash

# 纪念堂文件上传测试脚本
# 测试目录创建和文件上传功能

echo "🏛️ 纪念堂文件上传测试"
echo "================================"

# 检查后端服务是否运行
echo "1. 检查后端服务状态..."
if curl -s http://localhost:5000/api/memorial/stats > /dev/null; then
    echo "✅ 后端服务运行正常"
else
    echo "❌ 后端服务未运行，请先启动后端服务"
    exit 1
fi

# 测试获取统计信息
echo ""
echo "2. 测试获取纪念堂统计信息..."
response=$(curl -s -X GET "http://localhost:5000/api/memorial/stats")
echo "响应: $response"

# 测试获取文件列表
echo ""
echo "3. 测试获取纪念堂文件列表..."
response=$(curl -s -X GET "http://localhost:5000/api/memorial/list")
echo "响应: $response"

# 检查上传目录是否存在
echo ""
echo "4. 检查上传目录..."
if [ -d "./uploads/memorial" ]; then
    echo "✅ 上传目录存在: ./uploads/memorial"
    ls -la ./uploads/memorial/
else
    echo "❌ 上传目录不存在: ./uploads/memorial"
    echo "正在创建目录..."
    mkdir -p ./uploads/memorial
    echo "✅ 目录创建完成"
fi

# 测试文件上传（如果有测试文件）
echo ""
echo "5. 测试文件上传..."
if [ -f "test-image.jpg" ]; then
    echo "发现测试文件 test-image.jpg，开始上传..."
    response=$(curl -s -X POST -F "file=@test-image.jpg" -F "type=image" "http://localhost:5000/api/memorial/upload")
    echo "上传响应: $response"
else
    echo "未发现测试文件 test-image.jpg"
    echo "请准备一个测试图片文件，然后运行："
    echo "curl -X POST -F 'file=@your-image.jpg' -F 'type=image' http://localhost:5000/api/memorial/upload"
fi

echo ""
echo "🏛️ 纪念堂文件上传测试完成"
echo "================================"
