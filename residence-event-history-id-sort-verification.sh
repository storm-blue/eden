#!/bin/bash

echo "🔍 居所事件历史排序字段修改验证"
echo "================================="

echo "1. 检查是否还有按时间排序的SQL..."
time_order_count=$(grep -c "ORDER BY created_at" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)
echo "   - 按时间排序的SQL数量: $time_order_count"

if [ $time_order_count -eq 0 ]; then
    echo "   ✅ 已成功移除所有按时间排序的SQL"
else
    echo "   ❌ 仍然存在按时间排序的SQL"
fi

echo ""
echo "2. 检查是否已改为按ID排序..."
id_order_count=$(grep -c "ORDER BY id" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)
echo "   - 按ID排序的SQL数量: $id_order_count"

if [ $id_order_count -eq 3 ]; then
    echo "   ✅ 已成功修改为按ID排序（3个SQL语句）"
else
    echo "   ❌ 按ID排序的SQL数量不正确"
fi

echo ""
echo "3. 检查具体的SQL语句..."
echo "   getRecentEventHistory SQL:"
grep -A 1 "getRecentEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "ORDER BY" | sed 's/^[[:space:]]*/     /'

echo "   getAllEventHistory SQL:"
grep -A 1 "getAllEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "ORDER BY" | sed 's/^[[:space:]]*/     /'

echo "   cleanupOldEventHistory SQL:"
grep -A 2 "cleanupOldEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "ORDER BY" | sed 's/^[[:space:]]*/     /'

echo ""
echo "4. 验证排序方向..."
desc_count=$(grep -c "ORDER BY id DESC" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)
echo "   - ID倒序排列数量: $desc_count"

if [ $desc_count -eq 3 ]; then
    echo "   ✅ 所有SQL都使用ID倒序排列"
else
    echo "   ❌ ID倒序排列数量不完整"
fi

echo ""
echo "🎉 排序字段修改验证完成！"
echo "================================="
