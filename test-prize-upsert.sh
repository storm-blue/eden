#!/bin/bash

# 测试奖品UPSERT初始化功能的脚本

echo "🔄 测试奖品UPSERT初始化功能"
echo "================================"

# 设置API基础URL
API_BASE="http://localhost:5000/api"

echo "1. 查看当前奖品列表"
curl -s -X GET "$API_BASE/prizes" | jq '.'

echo -e "\n2. 重启后端服务来触发UPSERT初始化"
echo "请手动重启后端服务，观察日志输出："
echo "sudo systemctl restart eden-backend"
echo "sudo journalctl -u eden-backend -f --lines=50"

echo -e "\n3. 预期的日志输出："
echo "✅ 开始重新初始化奖品数据..."
echo "✅ 当前数据库中有 X 个奖品"
echo "✅ 概率验证通过：总和为 1.0"
echo "✅ 插入奖品[0]: 🍰 吃的～ - 概率: 8.0% - 级别: common"
echo "✅ 更新奖品[1]: 🥤 喝的～ - 概率: 8.0% - 级别: common"
echo "✅ ... (其他奖品)"
echo "✅ 🎉 奖品重新初始化完成！共配置 8 个奖品，概率总和: 100.0%"

echo -e "\n4. 验证UPSERT后的奖品配置："
echo "ID1/索引0: 🍰 吃的～ (8%)"
echo "ID2/索引1: 🥤 喝的～ (8%)"
echo "ID3/索引2: ❤️ 爱 (0.2%)"
echo "ID4/索引3: 💸 空空如也 (40%)"
echo "ID5/索引4: 🧧 红包 (5%)"
echo "ID6/索引5: 🔄 再转一次 (30%)"
echo "ID7/索引6: 🎁 随机礼物 (2.8%)"
echo "ID8/索引7: 💬 陪聊服务 (6%)"

echo -e "\n🎯 UPSERT模式特性："
echo "- ✅ 使用固定ID（1-8），保持数据一致性"
echo "- ✅ 存在则更新，不存在则插入"
echo "- ✅ 不会删除现有数据，避免外键约束问题"
echo "- ✅ 保持与前端转盘顺序一致"
echo "- ✅ 支持历史抽奖记录的完整性"

echo -e "\n📊 概率配置："
echo "- 🍰 吃的～: 8%"
echo "- 🥤 喝的～: 8%"
echo "- ❤️ 爱: 0.2%"
echo "- 💸 空空如也: 40%"
echo "- 🧧 红包: 5%"
echo "- 🔄 再转一次: 30%"
echo "- 🎁 随机礼物: 2.8%"
echo "- 💬 陪聊服务: 6%"
echo "总计: 100%"

echo -e "\n🔧 优势："
echo "- 保护历史数据完整性"
echo "- 避免外键约束冲突"
echo "- 支持配置热更新"
echo "- 固定ID便于数据分析"

echo -e "\n⚠️ 注意事项："
echo "- 奖品ID固定为1-8，不会自动生成"
echo "- 前端转盘顺序必须与数组索引对应"
echo "- 修改概率配置需要重新编译部署"
