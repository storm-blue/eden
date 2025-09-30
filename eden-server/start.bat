@echo off
chcp 65001
echo 🎪 启动Eden抽奖系统Java后端...

:: 检查Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ 未找到Java，请先安装Java 17或更高版本
    pause
    exit /b 1
)

:: 检查Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ❌ 未找到Maven，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo ☕ Java版本:
java -version

echo 📦 Maven版本:
mvn -version

echo 🔧 编译项目...
mvn clean compile

if errorlevel 1 (
    echo ❌ 编译失败
    pause
    exit /b 1
)

echo 🚀 启动服务...
mvn spring-boot:run

pause
