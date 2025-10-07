#!/bin/bash

echo "🔍 居所事件历史排序和数量限制修改验证"
echo "======================================="

echo "1. 检查Mapper中的SQL排序方式..."
asc_count=$(grep -c "ORDER BY created_at ASC" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)
desc_count=$(grep -c "ORDER BY created_at DESC" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java)

echo "   - ASC排序数量: $asc_count"
echo "   - DESC排序数量: $desc_count"

if [ $asc_count -eq 0 ] && [ $desc_count -gt 0 ]; then
    echo "   ✅ 已成功修改为倒序排列（DESC）"
else
    echo "   ❌ 排序修改不完整"
fi

echo ""
echo "2. 检查Service中的默认数量限制..."
limit_20=$(grep -c "getRecentEventHistory(residence, 20)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)
limit_10=$(grep -c "getRecentEventHistory(residence, 10)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)

echo "   - 20条限制数量: $limit_20"
echo "   - 10条限制数量: $limit_10"

if [ $limit_20 -eq 0 ] && [ $limit_10 -gt 0 ]; then
    echo "   ✅ 已成功修改为10条限制"
else
    echo "   ❌ 数量限制修改不完整"
fi

echo ""
echo "3. 检查最大限制参数..."
limit_50=$(grep -c "Math.min(limit, 50)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)
limit_10_max=$(grep -c "Math.min(limit, 10)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)

echo "   - 最大50条限制: $limit_50"
echo "   - 最大10条限制: $limit_10_max"

if [ $limit_50 -eq 0 ] && [ $limit_10_max -gt 0 ]; then
    echo "   ✅ 已成功修改最大限制为10条"
else
    echo "   ❌ 最大限制修改不完整"
fi

echo ""
echo "4. 检查清理逻辑..."
cleanup_20=$(grep -c "cleanupOldEventHistory(residence, 20)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)
cleanup_10=$(grep -c "cleanupOldEventHistory(residence, 10)" eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java)

echo "   - 保留20条的清理: $cleanup_20"
echo "   - 保留10条的清理: $cleanup_10"

if [ $cleanup_20 -eq 0 ] && [ $cleanup_10 -gt 0 ]; then
    echo "   ✅ 已成功修改清理逻辑为保留10条"
else
    echo "   ❌ 清理逻辑修改不完整"
fi

echo ""
echo "5. 显示具体的SQL查询语句..."
echo "   getRecentEventHistory SQL:"
grep -A 1 "getRecentEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "ORDER BY" | sed 's/^[[:space:]]*/     /'

echo "   getAllEventHistory SQL:"
grep -A 1 "getAllEventHistory" eden-server/src/main/java/com/eden/lottery/mapper/ResidenceEventHistoryMapper.java | grep "ORDER BY" | sed 's/^[[:space:]]*/     /'

echo ""
echo "🎉 修改验证完成！"
echo "======================================="
