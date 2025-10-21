#!/bin/bash

# 纪念堂功能测试脚本
# 测试纪念堂的文件上传、列表获取、删除功能

echo "🏛️ 纪念堂功能测试开始"
echo "================================"

# 测试1: 获取纪念堂媒体文件列表
echo "1. 测试获取纪念堂媒体文件列表..."
response1=$(curl -s -X GET "http://localhost:8080/api/memorial/list")
echo "响应: $response1"
echo ""

# 测试2: 获取纪念堂统计信息
echo "2. 测试获取纪念堂统计信息..."
response2=$(curl -s -X GET "http://localhost:8080/api/memorial/stats")
echo "响应: $response2"
echo ""

# 测试3: 上传测试文件（如果有的话）
echo "3. 测试文件上传功能..."
echo "注意: 需要准备测试图片或视频文件"
echo "示例命令: curl -X POST -F 'file=@test.jpg' -F 'type=image' http://localhost:8080/api/memorial/upload"
echo ""

# 测试4: 测试删除功能（需要先有文件）
echo "4. 测试文件删除功能..."
echo "注意: 需要先上传文件获取ID"
echo "示例命令: curl -X DELETE http://localhost:8080/api/memorial/delete/1"
echo ""

echo "🏛️ 纪念堂功能测试完成"
echo "================================"
echo ""
echo "API接口说明:"
echo "- POST /api/memorial/upload - 上传文件"
echo "- GET /api/memorial/list - 获取文件列表"
echo "- DELETE /api/memorial/delete/{id} - 删除文件"
echo "- GET /api/memorial/stats - 获取统计信息"
echo ""
echo "文件限制:"
echo "- 支持格式: 图片和视频"
echo "- 文件大小: 最大10MB"
echo "- 存储路径: ./uploads/memorial/"
echo "- 访问路径: /uploads/memorial/"
