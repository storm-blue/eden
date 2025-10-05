#!/bin/bash

echo "=== 测试头像功能修复 ==="

echo "1. 检查头像上传目录"
ls -la eden-server/uploads/avatars/ 2>/dev/null || echo "头像目录不存在或为空"

echo -e "\n2. 检查后端服务是否运行"
curl -s http://localhost:5000/api/avatar/测试用户 > /dev/null
if [ $? -eq 0 ]; then
    echo "后端服务正常运行"
else
    echo "后端服务未运行或无法访问"
fi

echo -e "\n3. 检查静态文件访问配置"
echo "静态文件应该通过以下路径访问："
echo "http://localhost:5000/uploads/avatars/文件名.jpg"

echo -e "\n=== 测试完成 ==="
