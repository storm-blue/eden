#!/bin/bash

# 用户漫游系统测试脚本

echo "🚶 用户漫游系统测试"
echo "===================="

# 服务器地址
SERVER_URL="http://localhost:5000"

echo ""
echo "1. 获取漫游系统状态..."
curl -s -X GET "${SERVER_URL}/api/user-roaming/status" | jq '.'

echo ""
echo "2. 手动触发漫游系统..."
TRIGGER_RESULT=$(curl -s -X POST "${SERVER_URL}/api/user-roaming/trigger" \
  -H "Content-Type: application/json")

echo "$TRIGGER_RESULT" | jq '.'

if echo "$TRIGGER_RESULT" | jq -e '.success' > /dev/null; then
    echo ""
    echo "✅ 漫游系统触发成功！"
    
    echo ""
    echo "3. 等待3秒后检查用户居住变化..."
    sleep 3
    
    echo ""
    echo "4. 检查各建筑的居住人员和事件："
    
    buildings=("castle" "park" "city_hall" "white_dove_house" "palace")
    building_names=("城堡🏰" "公园🌳" "市政厅🏛️" "小白鸽家🕊️" "行宫🏯")
    
    for i in "${!buildings[@]}"; do
        building="${buildings[$i]}"
        building_name="${building_names[$i]}"
        
        echo ""
        echo "📍 ${building_name} (${building}):"
        
        # 获取居所信息
        RESIDENCE_INFO=$(curl -s -X GET "${SERVER_URL}/api/residence-events/${building}")
        
        # 检查居住人员
        RESIDENTS=$(echo "$RESIDENCE_INFO" | jq -r '.residents[]?.username // empty' 2>/dev/null)
        
        if [ -z "$RESIDENTS" ]; then
            echo "   👥 居住人员: (无人居住)"
        else
            echo "   👥 居住人员:"
            echo "$RESIDENTS" | while read -r resident; do
                if [ -n "$resident" ]; then
                    if [ "$resident" = "存子" ] || [ "$resident" = "小白鸽" ]; then
                        echo "      🚶 $resident (可漫游用户)"
                    else
                        echo "      🏠 $resident (固定用户)"
                    fi
                fi
            done
        fi
        
        # 检查居所事件
        EVENTS=$(echo "$RESIDENCE_INFO" | jq -r '.events[]?.description // empty' 2>/dev/null | head -2)
        if [ -n "$EVENTS" ]; then
            echo "   📝 当前事件:"
            echo "$EVENTS" | while read -r event; do
                if [ -n "$event" ]; then
                    echo "      • $event"
                fi
            done
        fi
    done
    
    echo ""
    echo "5. 再次获取漫游系统状态..."
    curl -s -X GET "${SERVER_URL}/api/user-roaming/status" | jq '.'
    
else
    echo ""
    echo "❌ 漫游系统触发失败"
    echo "$TRIGGER_RESULT" | jq -r '.message // "未知错误"'
fi

echo ""
echo "===================="
echo "测试完成"
echo ""
echo "📋 漫游规则说明："
echo "   • 只有'存子'和'小白鸽'会移动"
echo "   • 其他用户保持在当前居所"
echo "   • 每2小时自动执行一次"
echo "   • 可移动到任意其他居所"
echo "   • 用户移动后会自动刷新居所事件"
