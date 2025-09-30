#!/bin/bash

echo "🎪 启动Eden抽奖系统Java后端..."

# 检查Java版本
if ! command -v java &> /dev/null; then
    echo "❌ 未找到Java，请先安装Java 17或更高版本"
    exit 1
fi

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ 未找到Maven，请先安装Maven 3.6或更高版本"
    exit 1
fi

echo "☕ Java版本:"
java -version

echo "📦 Maven版本:"
mvn -version

echo "🔧 编译项目..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ 编译失败"
    exit 1
fi

echo "🚀 启动服务..."
mvn spring-boot:run
