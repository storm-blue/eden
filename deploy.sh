#!/bin/bash

# Eden转盘抽奖系统部署脚本
# 适用于 Ubuntu/Debian 系统

set -e

echo "🚀 开始部署 Eden 转盘抽奖系统..."

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then 
    echo "请使用 sudo 运行此脚本"
    exit 1
fi

# 更新系统包
echo "📦 更新系统包..."
apt update && apt upgrade -y

# 安装基础依赖
echo "🔧 安装基础依赖..."
apt install -y curl wget unzip git nginx

# 安装 Node.js 18
echo "📦 安装 Node.js..."
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# 安装 Java 17
echo "☕ 安装 Java 17..."
apt install -y openjdk-17-jdk

# 验证安装
echo "✅ 验证安装..."
node --version
npm --version
java -version

# 创建应用目录
echo "📁 创建应用目录..."
mkdir -p /opt/eden
mkdir -p /var/log/eden

echo "✅ 环境准备完成！"
echo "请将项目文件上传到 /opt/eden 目录"
