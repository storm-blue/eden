#!/bin/bash

# 测试奖品重新初始化功能的脚本

echo "🔄 测试奖品重新初始化功能"
echo "================================"

# 设置API基础URL
API_BASE="http://localhost:5000/api"

echo "1. 查看当前奖品列表"
curl -s -X GET "$API_BASE/prizes" | jq '.'

echo -e "\n2. 重启后端服务来触发重新初始化"
echo "请手动重启后端服务，观察日志输出："
echo "sudo systemctl restart eden-backend"
echo "sudo journalctl -u eden-backend -f --lines=50"

echo -e "\n3. 预期的日志输出："
echo "✅ 开始重新初始化奖品数据..."
echo "✅ 发现现有奖品 X 个，正在清理..."
echo "✅ 成功删除 X 个现有奖品"
echo "✅ 概率验证通过：总和为 1.0"
echo "✅ 插入奖品[0]: 🍰 吃的～ - 概率: 25.0% - 级别: 普通"
echo "✅ 插入奖品[1]: 🥤 喝的～ - 概率: 25.0% - 级别: 普通"
echo "✅ ... (其他奖品)"
echo "✅ 🎉 奖品重新初始化完成！共配置 7 个奖品，概率总和: 100.0%"

echo -e "\n4. 验证重新初始化后的奖品配置："
echo "索引0: 🍰 吃的～ (25%)"
echo "索引1: 🥤 喝的～ (25%)"
echo "索引2: ❤️ 爱 (1%)"
echo "索引3: 💸 空空如也 (20%)"
echo "索引4: 🧧 红包 (4%)"
echo "索引5: 🔄 再转一次 (25%)"
echo "索引6: 🎁 随机礼物 (0%)"

echo -e "\n🎯 重要特性："
echo "- ✅ 每次启动都会重新初始化奖品"
echo "- ✅ 清空现有数据，插入新配置"
echo "- ✅ 自动验证概率总和为100%"
echo "- ✅ 保持与前端转盘顺序一致"
echo "- ✅ 详细的日志输出便于调试"

echo -e "\n📋 使用场景："
echo "- 修改奖品配置后重启生效"
echo "- 确保配置一致性"
echo "- 开发测试环境快速重置"
echo "- 生产环境配置更新"

echo -e "\n⚠️ 注意事项："
echo "- 重启会清空所有现有奖品数据"
echo "- 概率配置在代码中，需要重新编译部署"
echo "- 建议在维护窗口期间重启"
