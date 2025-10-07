#!/bin/bash

echo "🔍 居所常量简化验证"
echo "=================="

echo "1. 检查ResidenceConstants.java中是否还有用户偏好常量..."
pref_constants=$(grep -c "PREFERRED" eden-server/src/main/java/com/eden/lottery/constants/ResidenceConstants.java)
echo "   - 用户偏好常量数量: $pref_constants"

if [ $pref_constants -eq 0 ]; then
    echo "   ✅ 用户偏好常量已成功移除"
else
    echo "   ❌ 仍然存在用户偏好常量"
fi

echo ""
echo "2. 检查UserRoamingLogicService.java中的硬编码数组..."
hardcoded_arrays=$(grep -c "new String\[\]" eden-server/src/main/java/com/eden/lottery/service/UserRoamingLogicService.java)
echo "   - 硬编码数组数量: $hardcoded_arrays"

if [ $hardcoded_arrays -eq 3 ]; then
    echo "   ✅ 正确使用了3个硬编码数组（白婆婆、大祭祀、严伯升）"
else
    echo "   ❌ 硬编码数组数量不正确"
fi

echo ""
echo "3. 检查是否仍然使用ResidenceConstants中的基础常量..."
basic_constants=$(grep -c "ResidenceConstants\." eden-server/src/main/java/com/eden/lottery/service/UserRoamingLogicService.java)
echo "   - ResidenceConstants使用次数: $basic_constants"

if [ $basic_constants -gt 0 ]; then
    echo "   ✅ 仍然正确使用ResidenceConstants中的基础常量"
else
    echo "   ❌ 没有使用ResidenceConstants中的基础常量"
fi

echo ""
echo "4. 显示具体的用户偏好配置..."
echo "   白婆婆偏好:"
grep -A 1 "白婆婆" eden-server/src/main/java/com/eden/lottery/service/UserRoamingLogicService.java | tail -1 | sed 's/^[[:space:]]*/     /'

echo "   大祭祀偏好:"
grep -A 1 "大祭祀" eden-server/src/main/java/com/eden/lottery/service/UserRoamingLogicService.java | tail -1 | sed 's/^[[:space:]]*/     /'

echo "   严伯升偏好:"
grep -A 1 "严伯升" eden-server/src/main/java/com/eden/lottery/service/UserRoamingLogicService.java | tail -1 | sed 's/^[[:space:]]*/     /'

echo ""
echo "🎉 简化验证完成！"
echo "=================="
