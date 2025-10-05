#!/bin/bash

echo "=== 测试用户头像功能 ==="

# 测试获取用户头像信息
echo "1. 测试获取用户头像信息（用户不存在）"
curl -s "http://localhost:5000/api/avatar/测试用户" | jq '.' || echo "请求失败"

echo -e "\n2. 测试获取用户头像信息（假设用户存在）"
curl -s "http://localhost:5000/api/avatar/李星斗" | jq '.' || echo "请求失败"

echo -e "\n=== 头像功能测试完成 ==="
