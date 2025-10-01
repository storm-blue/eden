#!/bin/bash

# Eden项目构建和部署脚本
set -e

PROJECT_DIR="/opt/eden"
FRONTEND_DIR="$PROJECT_DIR/eden-web"
BACKEND_DIR="$PROJECT_DIR/eden-server"
NGINX_DIR="/etc/nginx/sites-available"

echo "🏗️ 开始构建和部署 Eden 项目..."

# 检查项目目录
if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ 项目目录不存在: $PROJECT_DIR"
    exit 1
fi

cd $PROJECT_DIR

# 构建前端
echo "🎨 构建前端应用..."
cd $FRONTEND_DIR

# 安装依赖
npm install

# 构建生产版本
npm run build

# 复制构建文件到nginx目录
sudo mkdir -p /var/www/eden
sudo cp -r dist/* /var/www/eden/
sudo chown -R www-data:www-data /var/www/eden

echo "✅ 前端构建完成"

# 构建后端
echo "☕ 构建后端应用..."
cd $BACKEND_DIR

# 使用Maven构建
if command -v mvn &> /dev/null; then
    mvn clean package -DskipTests
else
    # 如果没有Maven，下载Maven Wrapper
    if [ ! -f "mvnw" ]; then
        echo "📦 下载 Maven Wrapper..."
        curl -o maven-wrapper.jar https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
        echo "exec java -classpath maven-wrapper.jar org.apache.maven.wrapper.MavenWrapperMain \"\$@\"" > mvnw
        chmod +x mvnw
    fi
    ./mvnw clean package -DskipTests
fi

echo "✅ 后端构建完成"

# 创建启动脚本
echo "📝 创建启动脚本..."
cat > /opt/eden/start-backend.sh << 'EOF'
#!/bin/bash
cd /opt/eden/eden-server
java -jar target/eden-lottery-server-1.0.0.jar \
    --server.port=5000 \
    --spring.profiles.active=prod \
    > /var/log/eden/backend.log 2>&1 &
echo $! > /var/run/eden-backend.pid
echo "后端服务已启动，PID: $(cat /var/run/eden-backend.pid)"
EOF

chmod +x /opt/eden/start-backend.sh

# 创建停止脚本
cat > /opt/eden/stop-backend.sh << 'EOF'
#!/bin/bash
if [ -f /var/run/eden-backend.pid ]; then
    PID=$(cat /var/run/eden-backend.pid)
    if kill -0 $PID 2>/dev/null; then
        kill $PID
        echo "后端服务已停止"
    else
        echo "后端服务未运行"
    fi
    rm -f /var/run/eden-backend.pid
else
    echo "PID文件不存在"
fi
EOF

chmod +x /opt/eden/stop-backend.sh

echo "✅ 构建和部署完成！"
