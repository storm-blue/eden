#!/bin/bash

# Eden抽奖系统服务器部署脚本

echo "🚀 开始部署Eden抽奖系统..."

# 1. 停止现有服务（如果在运行）
echo "📋 停止现有服务..."
pkill -f "eden-server" || true

# 2. 备份数据库（如果存在）
if [ -f "eden_lottery.db" ]; then
    echo "💾 备份现有数据库..."
    cp eden_lottery.db eden_lottery.db.backup.$(date +%Y%m%d_%H%M%S)
fi

# 3. 编译项目
echo "🔨 编译项目..."
cd eden-server
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ 编译失败！"
    exit 1
fi

# 4. 启动服务
echo "🎯 启动服务..."
nohup java -jar target/*.jar > ../server.log 2>&1 &

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 10

# 5. 检查服务状态
if curl -s http://localhost:5000/api/user/test > /dev/null; then
    echo "✅ 服务启动成功！"
    echo "📊 访问地址: http://localhost:5000"
    echo "📝 日志文件: server.log"
else
    echo "❌ 服务启动失败，请检查日志文件 server.log"
    exit 1
fi

echo "🎉 部署完成！"