#!/bin/bash

# 修复管理后台404问题
set -e

echo "🔧 修复管理后台404问题..."

BACKEND_DIR="/opt/eden/eden-server"
FRONTEND_DIR="/var/www/eden"

# 检查前端目录
if [ ! -d "$FRONTEND_DIR" ]; then
    echo "❌ 前端目录不存在: $FRONTEND_DIR"
    exit 1
fi

echo "📁 当前前端目录内容:"
ls -la $FRONTEND_DIR/

# 查找admin.html文件
echo "🔍 查找admin.html文件..."
ADMIN_FILES=$(find /opt/eden -name "admin.html" -type f 2>/dev/null || true)

if [ -n "$ADMIN_FILES" ]; then
    echo "找到admin.html文件:"
    echo "$ADMIN_FILES"
    
    # 复制第一个找到的admin.html
    FIRST_ADMIN=$(echo "$ADMIN_FILES" | head -n1)
    echo "复制 $FIRST_ADMIN 到 $FRONTEND_DIR/"
    sudo cp "$FIRST_ADMIN" "$FRONTEND_DIR/"
    sudo chown www-data:www-data "$FRONTEND_DIR/admin.html"
    sudo chmod 644 "$FRONTEND_DIR/admin.html"
    
    echo "✅ admin.html复制完成"
else
    echo "❌ 未找到admin.html文件"
    echo "📝 创建基本的admin.html文件..."
    
    # 创建基本的admin.html
    sudo tee "$FRONTEND_DIR/admin.html" > /dev/null << 'EOF'
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eden抽奖系统 - 管理后台</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-form {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background: #5a6fd8;
        }
        .error {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
        .admin-panel {
            display: none;
        }
        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
        }
        .tab {
            padding: 10px 20px;
            cursor: pointer;
            border-bottom: 2px solid transparent;
        }
        .tab.active {
            border-bottom-color: #667eea;
            color: #667eea;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🎰 Eden抽奖系统 - 管理后台</h1>
        </div>

        <!-- 登录表单 -->
        <div id="loginForm" class="login-form">
            <h2>管理员登录</h2>
            <form onsubmit="login(event)">
                <div class="form-group">
                    <label for="username">用户名:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">密码:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit">登录</button>
                <div id="loginError" class="error"></div>
            </form>
            <p style="text-align: center; margin-top: 20px; color: #666;">
                默认账号: admin / admin2008
            </p>
        </div>

        <!-- 管理面板 -->
        <div id="adminPanel" class="admin-panel">
            <div class="tabs">
                <div class="tab active" onclick="showTab('users')">用户管理</div>
                <div class="tab" onclick="showTab('lottery')">抽奖记录</div>
                <div class="tab" onclick="showTab('stats')">统计信息</div>
            </div>

            <div id="usersTab" class="tab-content active">
                <h3>用户管理</h3>
                <button onclick="loadUsers()">刷新用户列表</button>
                <div id="usersList"></div>
            </div>

            <div id="lotteryTab" class="tab-content">
                <h3>抽奖记录</h3>
                <button onclick="loadLotteryHistory()">刷新记录</button>
                <div id="lotteryHistory"></div>
            </div>

            <div id="statsTab" class="tab-content">
                <h3>统计信息</h3>
                <button onclick="loadStats()">刷新统计</button>
                <div id="statsInfo"></div>
            </div>
        </div>
    </div>

    <script>
        let authToken = '';

        async function login(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('loginError');

            try {
                const response = await fetch('/api/admin/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ username, password })
                });

                const result = await response.json();

                if (result.success) {
                    authToken = result.data.token;
                    document.getElementById('loginForm').style.display = 'none';
                    document.getElementById('adminPanel').style.display = 'block';
                    loadUsers();
                } else {
                    errorDiv.textContent = result.message || '登录失败';
                }
            } catch (error) {
                errorDiv.textContent = '网络错误，请检查后端服务';
                console.error('Login error:', error);
            }
        }

        function showTab(tabName) {
            // 隐藏所有标签页
            document.querySelectorAll('.tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            document.querySelectorAll('.tab').forEach(tab => {
                tab.classList.remove('active');
            });

            // 显示选中的标签页
            document.getElementById(tabName + 'Tab').classList.add('active');
            event.target.classList.add('active');
        }

        async function loadUsers() {
            try {
                const response = await fetch('/api/admin/users', {
                    headers: {
                        'Authorization': 'Bearer ' + authToken
                    }
                });
                const result = await response.json();

                if (result.success) {
                    displayUsers(result.data);
                } else {
                    document.getElementById('usersList').innerHTML = '<p>加载用户失败: ' + result.message + '</p>';
                }
            } catch (error) {
                document.getElementById('usersList').innerHTML = '<p>网络错误</p>';
            }
        }

        function displayUsers(users) {
            let html = '<table><tr><th>用户ID</th><th>剩余次数</th><th>每日次数</th><th>创建时间</th></tr>';
            users.forEach(user => {
                html += `<tr>
                    <td>${user.userId}</td>
                    <td>${user.remainingDraws}</td>
                    <td>${user.dailyDraws}</td>
                    <td>${user.createTime}</td>
                </tr>`;
            });
            html += '</table>';
            document.getElementById('usersList').innerHTML = html;
        }

        async function loadLotteryHistory() {
            try {
                const response = await fetch('/api/admin/lottery-history', {
                    headers: {
                        'Authorization': 'Bearer ' + authToken
                    }
                });
                const result = await response.json();

                if (result.success) {
                    displayLotteryHistory(result.data);
                } else {
                    document.getElementById('lotteryHistory').innerHTML = '<p>加载记录失败: ' + result.message + '</p>';
                }
            } catch (error) {
                document.getElementById('lotteryHistory').innerHTML = '<p>网络错误</p>';
            }
        }

        function displayLotteryHistory(records) {
            let html = '<table><tr><th>用户</th><th>奖品</th><th>时间</th></tr>';
            records.forEach(record => {
                html += `<tr>
                    <td>${record.userId}</td>
                    <td>${record.prizeName}</td>
                    <td>${record.drawTime}</td>
                </tr>`;
            });
            html += '</table>';
            document.getElementById('lotteryHistory').innerHTML = html;
        }

        async function loadStats() {
            try {
                const response = await fetch('/api/admin/stats', {
                    headers: {
                        'Authorization': 'Bearer ' + authToken
                    }
                });
                const result = await response.json();

                if (result.success) {
                    displayStats(result.data);
                } else {
                    document.getElementById('statsInfo').innerHTML = '<p>加载统计失败: ' + result.message + '</p>';
                }
            } catch (error) {
                document.getElementById('statsInfo').innerHTML = '<p>网络错误</p>';
            }
        }

        function displayStats(stats) {
            let html = '<h4>总体统计</h4>';
            html += `<p>总抽奖次数: ${stats.totalDraws}</p>`;
            html += '<h4>奖品统计</h4>';
            if (stats.prizeStats) {
                stats.prizeStats.forEach(prize => {
                    html += `<p>${prize.prizeName}: ${prize.count}次</p>`;
                });
            }
            document.getElementById('statsInfo').innerHTML = html;
        }
    </script>
</body>
</html>
EOF

    sudo chown www-data:www-data "$FRONTEND_DIR/admin.html"
    sudo chmod 644 "$FRONTEND_DIR/admin.html"
    
    echo "✅ 基本admin.html创建完成"
fi

# 验证文件
echo "📋 验证admin.html文件:"
ls -la "$FRONTEND_DIR/admin.html"

# 测试访问
echo "🧪 测试admin.html访问:"
curl -I http://localhost/admin.html

echo "✅ 管理后台修复完成！"
echo "🌐 访问地址: http://your-server-ip/admin.html"
echo "🔑 登录信息: admin / admin2008"
