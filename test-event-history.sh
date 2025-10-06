#!/bin/bash

SERVER_URL="http://localhost:5000"

echo "=========================================="
echo "居所事件历史功能测试脚本"
echo "=========================================="
echo ""

echo "1. 测试触发事件生成（产生历史记录）..."
buildings=("castle" "park" "city_hall" "white_dove_house" "palace")
building_names=("城堡🏰" "公园🌳" "市政厅🏛️" "小白鸽家🕊️" "行宫🏯")

# 先生成一些事件记录
for i in "${!buildings[@]}"; do
    building="${buildings[$i]}"
    building_name="${building_names[$i]}"
    
    echo "   触发 ${building_name} 事件生成..."
    curl -s -X POST "${SERVER_URL}/api/residence-events/${building}/refresh" | jq -r '.message // "触发完成"'
done

echo ""
echo "2. 等待3秒后开始检查历史记录..."
sleep 3

echo ""
echo "3. 检查各居所的事件历史："
echo ""

for i in "${!buildings[@]}"; do
    building="${buildings[$i]}"
    building_name="${building_names[$i]}"
    
    echo "📍 ${building_name} (${building}):"
    echo "   🔍 获取事件历史..."
    
    HISTORY_RESPONSE=$(curl -s -X GET "${SERVER_URL}/api/residence-event-history/${building}")
    
    # 检查API响应
    SUCCESS=$(echo "$HISTORY_RESPONSE" | jq -r '.success // false')
    
    if [ "$SUCCESS" = "true" ]; then
        HISTORY_COUNT=$(echo "$HISTORY_RESPONSE" | jq -r '.history | length')
        STATS=$(echo "$HISTORY_RESPONSE" | jq -r '.stats.historyCount // 0')
        
        echo "   📊 历史记录数量: ${HISTORY_COUNT} 条"
        echo "   📈 统计信息: ${STATS} 条总记录"
        
        if [ "$HISTORY_COUNT" -gt 0 ]; then
            echo "   📝 最新事件历史："
            
            # 显示最新的3条历史记录
            echo "$HISTORY_RESPONSE" | jq -r '.history[0:3][] | 
                "      ⏰ " + (.createdAt | split("T")[0] + " " + (.createdAt | split("T")[1] | split(".")[0])) + 
                " | 👥 " + ((.residentsInfo | fromjson) | join(", ")) + 
                " | 📋 " + ((.eventData | fromjson) | map(.description) | join(" / "))'
        else
            echo "   ❌ 暂无历史记录"
        fi
    else
        ERROR_MSG=$(echo "$HISTORY_RESPONSE" | jq -r '.message // "未知错误"')
        echo "   ❌ 获取历史失败: ${ERROR_MSG}"
    fi
    
    echo ""
done

echo "4. 测试获取指定数量的历史记录..."
echo "   🔍 获取城堡最近5条历史记录："

CASTLE_HISTORY=$(curl -s -X GET "${SERVER_URL}/api/residence-event-history/castle/5")
CASTLE_SUCCESS=$(echo "$CASTLE_HISTORY" | jq -r '.success // false')

if [ "$CASTLE_SUCCESS" = "true" ]; then
    CASTLE_COUNT=$(echo "$CASTLE_HISTORY" | jq -r '.history | length')
    echo "   📊 获取到 ${CASTLE_COUNT} 条记录"
    
    if [ "$CASTLE_COUNT" -gt 0 ]; then
        echo "   📝 历史记录详情："
        echo "$CASTLE_HISTORY" | jq -r '.history[] | 
            "      🕐 " + (.createdAt | split("T")[0] + " " + (.createdAt | split("T")[1] | split(".")[0])) + 
            "\n      👥 居住人员: " + ((.residentsInfo | fromjson) | join(", ")) + 
            "\n      📋 事件: " + ((.eventData | fromjson) | map(.description + " (" + .type + ")") | join("; ")) + 
            "\n      ---"'
    fi
else
    CASTLE_ERROR=$(echo "$CASTLE_HISTORY" | jq -r '.message // "未知错误"')
    echo "   ❌ 获取失败: ${CASTLE_ERROR}"
fi

echo ""
echo "5. 测试获取历史统计信息..."

for building in "castle" "park"; do
    echo "   📊 ${building} 统计信息："
    STATS_RESPONSE=$(curl -s -X GET "${SERVER_URL}/api/residence-event-history/${building}/stats")
    
    STATS_SUCCESS=$(echo "$STATS_RESPONSE" | jq -r '.success // false')
    if [ "$STATS_SUCCESS" = "true" ]; then
        echo "$STATS_RESPONSE" | jq -r '.data | 
            "      🏠 居所: " + .residence + 
            "\n      📈 历史记录数: " + (.historyCount | tostring) + 
            "\n      🌍 全局总记录数: " + (.totalHistoryCount | tostring)'
    else
        STATS_ERROR=$(echo "$STATS_RESPONSE" | jq -r '.message // "未知错误"')
        echo "      ❌ 获取统计失败: ${STATS_ERROR}"
    fi
    echo ""
done

echo "6. 测试获取所有居所历史概览..."
OVERVIEW_RESPONSE=$(curl -s -X GET "${SERVER_URL}/api/residence-event-history/overview")
OVERVIEW_SUCCESS=$(echo "$OVERVIEW_RESPONSE" | jq -r '.success // false')

if [ "$OVERVIEW_SUCCESS" = "true" ]; then
    echo "   📊 全局概览："
    echo "$OVERVIEW_RESPONSE" | jq -r '.overview | to_entries[] | 
        "      🏠 " + .key + ": " + (.value.historyCount | tostring) + " 条记录"'
else
    OVERVIEW_ERROR=$(echo "$OVERVIEW_RESPONSE" | jq -r '.message // "未知错误"')
    echo "   ❌ 获取概览失败: ${OVERVIEW_ERROR}"
fi

echo ""
echo "=========================================="
echo "测试完成"
echo ""
echo "📋 功能说明："
echo "   • 每次生成事件时会自动记录到历史表"
echo "   • 历史记录包含事件数据、居住人员、时间等信息"
echo "   • 自动清理旧记录，保持最新20条"
echo "   • 支持获取指定数量的历史记录"
echo "   • 提供统计信息和概览功能"
echo "   • 前端居所弹框新增'📜 历史'按钮查看"
echo ""
echo "🎯 使用方法："
echo "   1. 点击星星城中的任意建筑"
echo "   2. 在居所弹框中点击'📜 历史'按钮"
echo "   3. 查看该居所的最近20次事件历史"
echo "   4. 历史记录按时间正序显示（最早的在上面），包含居住人员和事件详情"
echo "   5. 移动端横屏模式下弹框高度已优化，确保完整显示"
