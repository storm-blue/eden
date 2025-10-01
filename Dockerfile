# 多阶段构建 Dockerfile
FROM node:18-alpine AS frontend-build

WORKDIR /app/frontend
COPY eden-web/package*.json ./
RUN npm ci --only=production

COPY eden-web/ ./
RUN npm run build

# Java后端构建
FROM openjdk:17-jdk-alpine AS backend-build

WORKDIR /app/backend
COPY eden-server/pom.xml ./
COPY eden-server/mvnw ./
COPY eden-server/.mvn ./.mvn

# 下载依赖
RUN ./mvnw dependency:go-offline -B

COPY eden-server/src ./src
RUN ./mvnw clean package -DskipTests

# 生产环境镜像
FROM openjdk:17-jre-alpine

# 安装nginx
RUN apk add --no-cache nginx

# 创建应用目录
RUN mkdir -p /app/data /var/log/eden /var/www/eden

# 复制前端构建文件
COPY --from=frontend-build /app/frontend/dist/ /var/www/eden/

# 复制后端JAR文件
COPY --from=backend-build /app/backend/target/eden-lottery-server-1.0.0.jar /app/app.jar

# 复制配置文件
COPY production-config.yml /app/application-prod.yml
COPY nginx.conf /etc/nginx/http.d/default.conf

# 创建启动脚本
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'nginx &' >> /app/start.sh && \
    echo 'java -jar /app/app.jar --spring.profiles.active=prod --spring.config.location=/app/application-prod.yml' >> /app/start.sh && \
    chmod +x /app/start.sh

EXPOSE 80 5000

VOLUME ["/app/data"]

CMD ["/app/start.sh"]
