#!/bin/bash

echo "🔍 居所重构验证脚本"
echo "==================="

# 检查是否还有硬编码的居所字符串
echo "1. 检查硬编码居所字符串..."
hardcoded_count=$(grep -r "case \"castle\"" eden-server/src/main/java/com/eden/lottery/ | wc -l)
hardcoded_count2=$(grep -r "case \"park\"" eden-server/src/main/java/com/eden/lottery/ | wc -l)
hardcoded_count3=$(grep -r "case \"city_hall\"" eden-server/src/main/java/com/eden/lottery/ | wc -l)

echo "   - 硬编码 'case \"castle\"' 数量: $hardcoded_count"
echo "   - 硬编码 'case \"park\"' 数量: $hardcoded_count2"
echo "   - 硬编码 'case \"city_hall\"' 数量: $hardcoded_count3"

if [ $hardcoded_count -eq 0 ] && [ $hardcoded_count2 -eq 0 ] && [ $hardcoded_count3 -eq 0 ]; then
    echo "   ✅ 没有发现硬编码居所字符串"
else
    echo "   ❌ 仍然存在硬编码居所字符串"
fi

# 检查是否正确使用了ResidenceConstants和ResidenceUtils
echo ""
echo "2. 检查ResidenceConstants和ResidenceUtils的使用..."

constants_usage=$(grep -r "ResidenceConstants\." eden-server/src/main/java/com/eden/lottery/ | wc -l)
utils_usage=$(grep -r "ResidenceUtils\." eden-server/src/main/java/com/eden/lottery/ | wc -l)

echo "   - ResidenceConstants 使用次数: $constants_usage"
echo "   - ResidenceUtils 使用次数: $utils_usage"

if [ $constants_usage -gt 0 ] && [ $utils_usage -gt 0 ]; then
    echo "   ✅ 正确使用了ResidenceConstants和ResidenceUtils"
else
    echo "   ❌ ResidenceConstants或ResidenceUtils使用不足"
fi

# 检查重复方法是否已删除
echo ""
echo "3. 检查重复方法是否已删除..."

duplicate_methods=0
duplicate_methods=$(grep -r "private.*getResidenceName" eden-server/src/main/java/com/eden/lottery/ | wc -l)
duplicate_methods2=$(grep -r "private.*isValidResidence" eden-server/src/main/java/com/eden/lottery/ | wc -l)

echo "   - 重复的 getResidenceName 方法: $duplicate_methods"
echo "   - 重复的 isValidResidence 方法: $duplicate_methods2"

if [ $duplicate_methods -eq 0 ] && [ $duplicate_methods2 -eq 0 ]; then
    echo "   ✅ 重复方法已成功删除"
else
    echo "   ❌ 仍然存在重复方法"
fi

# 检查特定文件的重构情况
echo ""
echo "4. 检查各文件重构情况..."

files=(
    "service/ResidenceEventService.java"
    "controller/AdminController.java"
    "service/SpecialResidenceService.java"
    "controller/ResidenceEventController.java"
    "service/PrizeInitService.java"
    "service/ResidenceHistoryService.java"
    "controller/ResidenceEventHistoryController.java"
)

for file in "${files[@]}"; do
    filepath="eden-server/src/main/java/com/eden/lottery/$file"
    if [ -f "$filepath" ]; then
        has_constants=$(grep -q "ResidenceConstants\|ResidenceUtils" "$filepath" && echo "✅" || echo "❌")
        echo "   - $file: $has_constants"
    else
        echo "   - $file: 文件不存在"
    fi
done

# 检查核心常量和工具类是否存在
echo ""
echo "5. 检查核心类是否存在..."

if [ -f "eden-server/src/main/java/com/eden/lottery/constants/ResidenceConstants.java" ]; then
    echo "   ✅ ResidenceConstants.java 存在"
else
    echo "   ❌ ResidenceConstants.java 不存在"
fi

if [ -f "eden-server/src/main/java/com/eden/lottery/utils/ResidenceUtils.java" ]; then
    echo "   ✅ ResidenceUtils.java 存在"
else
    echo "   ❌ ResidenceUtils.java 不存在"
fi

# 检查编译是否成功
echo ""
echo "6. 检查代码编译..."
cd eden-server
if mvn compile -q > /dev/null 2>&1; then
    echo "   ✅ 代码编译成功"
else
    echo "   ❌ 代码编译失败"
fi
cd ..

echo ""
echo "🎉 重构验证完成！"
echo "==================="
