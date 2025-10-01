#!/bin/bash

# 快速修复Maven问题并构建项目
set -e

echo "🔧 快速修复Maven问题..."

BACKEND_DIR="/opt/eden/eden-server"

if [ ! -d "$BACKEND_DIR" ]; then
    echo "❌ 后端目录不存在: $BACKEND_DIR"
    exit 1
fi

cd $BACKEND_DIR

# 方案1: 直接安装并使用系统Maven
echo "📦 安装系统Maven..."
if command -v apt &> /dev/null; then
    sudo apt update
    sudo apt install -y maven
elif command -v yum &> /dev/null; then
    sudo yum install -y maven
elif command -v dnf &> /dev/null; then
    sudo dnf install -y maven
fi

# 验证Maven安装
if command -v mvn &> /dev/null; then
    echo "✅ Maven安装成功"
    mvn --version
    
    # 直接使用Maven构建
    echo "🏗️ 开始构建项目..."
    mvn clean package -DskipTests
    
    echo "✅ 项目构建完成！"
    echo "JAR文件位置: $(find target -name "*.jar" -type f)"
else
    echo "❌ Maven安装失败，尝试手动下载..."
    
    # 方案2: 手动下载Maven
    cd /tmp
    wget https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz
    tar -xzf apache-maven-3.9.5-bin.tar.gz
    sudo mv apache-maven-3.9.5 /opt/maven
    
    # 使用下载的Maven构建
    cd $BACKEND_DIR
    echo "🏗️ 使用下载的Maven构建..."
    /opt/maven/bin/mvn clean package -DskipTests
    
    echo "✅ 项目构建完成！"
    echo "JAR文件位置: $(find target -name "*.jar" -type f)"
fi
