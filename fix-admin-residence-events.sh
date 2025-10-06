#!/bin/bash

echo "===================="
echo "更新管理界面居所事件标签页"
echo "===================="
echo ""

echo "✅ 已更新的文件："
echo "   - eden-server/src/main/resources/static/admin.html (后端静态资源)"
echo ""

echo "🔄 需要执行的步骤："
echo ""

echo "1. 重新编译后端项目："
echo "   cd eden-server"
echo "   mvn clean compile"
echo ""

echo "2. 重启后端服务："
echo "   # 如果使用systemctl"
echo "   sudo systemctl restart eden-backend"
echo ""
echo "   # 或者手动重启"
echo "   cd eden-server && ./start.sh"
echo ""

echo "3. 清除浏览器缓存："
echo "   - 按 Ctrl+Shift+R (或 Cmd+Shift+R) 强制刷新"
echo "   - 或者在开发者工具中右键刷新按钮选择'清空缓存并硬性重新加载'"
echo ""

echo "4. 访问管理界面："
echo "   http://localhost:5000/admin.html"
echo ""

echo "5. 验证功能："
echo "   ✅ 登录管理员账户 (admin/admin2008)"
echo "   ✅ 查看是否显示'居所事件'标签页"
echo "   ✅ 点击标签页测试功能"
echo ""

echo "📝 问题排查："
echo ""
echo "如果仍然看不到居所事件标签页："
echo ""
echo "1. 确认访问的是正确的admin.html文件"
echo "2. 强制刷新浏览器缓存"
echo "3. 检查浏览器控制台是否有JavaScript错误"
echo "4. 确认后端服务已重启并加载了新的静态文件"
echo ""

echo "===================="
