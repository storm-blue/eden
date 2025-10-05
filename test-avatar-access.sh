#!/bin/bash

echo "=== 测试头像访问路径修复 ==="

echo "1. 检查后端服务是否运行"
if curl -s http://localhost:5000/api/avatar/测试用户 > /dev/null; then
    echo "✅ 后端服务正常运行"
else
    echo "❌ 后端服务未运行或无法访问"
    exit 1
fi

echo -e "\n2. 测试头像API访问"
echo "测试获取用户头像信息："
curl -s "http://localhost:5000/api/avatar/李星斗" | jq '.' || echo "请求失败"

echo -e "\n3. 检查静态文件服务配置"
echo "头像文件现在应该通过以下路径访问："
echo "http://localhost:5000/uploads/avatars/文件名.jpg"

echo -e "\n4. 检查上传目录"
if [ -d "eden-server/uploads/avatars" ]; then
    echo "✅ 上传目录存在"
    ls -la eden-server/uploads/avatars/ 2>/dev/null || echo "目录为空"
else
    echo "❌ 上传目录不存在"
fi

echo -e "\n=== 修复说明 ==="
echo "现在头像访问流程："
echo "1. 上传：文件保存到 eden-server/uploads/avatars/"
echo "2. 访问：通过后端服务 http://localhost:5000/uploads/avatars/"
echo "3. 前端：显示完整的后端URL"

echo -e "\n=== 测试完成 ==="
