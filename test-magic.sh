#!/bin/bash

# 定义颜色
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== 测试魔法系统 ===${NC}"

# 1. 获取秦小淮的Token
echo -e "${GREEN}1. 尝试获取秦小淮的登录Token...${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/admin/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin2008"}')

ADMIN_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$ADMIN_TOKEN" ]; then
  echo -e "${RED}错误: 无法获取管理员Token，请检查后端服务是否运行或登录凭据是否正确。${NC}"
  exit 1
fi
echo -e "${GREEN}   管理员Token获取成功。${NC}"

# 2. 获取魔法列表
echo -e "${GREEN}2. 获取魔法列表...${NC}"
MAGICS_LIST=$(curl -s -X GET "http://localhost:5000/api/magic/list?userId=秦小淮")

echo -e "${BLUE}   魔法列表响应:${NC}"
echo "$MAGICS_LIST" | jq '.'

# 获取天降食物魔法
FOOD_MAGIC_CODE=$(echo "$MAGICS_LIST" | jq -r '.magics[] | select(.code=="FOOD_RAIN") | .code')
FOOD_REMAINING_USES_BEFORE=$(echo "$MAGICS_LIST" | jq -r '.magics[] | select(.code=="FOOD_RAIN") | .remainingUses')

# 获取改变天气魔法
WEATHER_MAGIC_CODE=$(echo "$MAGICS_LIST" | jq -r '.magics[] | select(.code=="CHANGE_WEATHER") | .code')
WEATHER_REMAINING_USES_BEFORE=$(echo "$MAGICS_LIST" | jq -r '.magics[] | select(.code=="CHANGE_WEATHER") | .remainingUses')

echo -e "${GREEN}   天降食物魔法: ${FOOD_MAGIC_CODE}, 剩余次数: ${FOOD_REMAINING_USES_BEFORE}${NC}"
echo -e "${GREEN}   改变天气魔法: ${WEATHER_MAGIC_CODE}, 剩余次数: ${WEATHER_REMAINING_USES_BEFORE}${NC}"

# 3. 获取施展前的食物数量和天气
echo -e "${GREEN}3. 获取施展魔法前的食物数量和天气...${NC}"
STARCITY_BEFORE=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
FOOD_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.food')
WEATHER_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.weather')
echo -e "${GREEN}   施展前食物数量: ${FOOD_BEFORE}${NC}"
echo -e "${GREEN}   施展前天气: ${WEATHER_BEFORE}${NC}"

# 4. 施展天降食物魔法
echo -e "${GREEN}4. 施展 '天降食物' 魔法...${NC}"
CAST_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/magic/cast" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=${FOOD_MAGIC_CODE}&userId=秦小淮")

CAST_SUCCESS=$(echo "$CAST_RESPONSE" | jq -r '.success')
CAST_MESSAGE=$(echo "$CAST_RESPONSE" | jq -r '.message')
FOOD_REMAINING_USES_AFTER=$(echo "$CAST_RESPONSE" | jq -r '.remainingUses')

if [ "$CAST_SUCCESS" = "true" ]; then
  echo -e "${GREEN}   天降食物魔法施展成功: ${CAST_MESSAGE}${NC}"
  echo -e "${GREEN}   剩余次数: ${FOOD_REMAINING_USES_AFTER}${NC}"
else
  echo -e "${RED}错误: 天降食物魔法施展失败: ${CAST_MESSAGE}${NC}"
  exit 1
fi

# 5. 验证食物数量增加
echo -e "${GREEN}5. 验证食物数量是否增加了10000...${NC}"
STARCITY_AFTER_FOOD=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
FOOD_AFTER=$(echo "$STARCITY_AFTER_FOOD" | jq -r '.food')
echo -e "${GREEN}   施展后食物数量: ${FOOD_AFTER}${NC}"

FOOD_INCREASE=$((FOOD_AFTER - FOOD_BEFORE))
if [ "$FOOD_INCREASE" -eq 10000 ]; then
  echo -e "${GREEN}   验证成功: 食物增加了 ${FOOD_INCREASE}${NC}"
else
  echo -e "${RED}错误: 食物增加异常。预期增加: 10000, 实际增加: ${FOOD_INCREASE}${NC}"
  exit 1
fi

# 6. 验证天降食物魔法次数减少
if [ "$FOOD_REMAINING_USES_AFTER" -lt "$FOOD_REMAINING_USES_BEFORE" ]; then
  echo -e "${GREEN}   验证成功: 天降食物魔法次数从 ${FOOD_REMAINING_USES_BEFORE} 减少到 ${FOOD_REMAINING_USES_AFTER}${NC}"
else
  echo -e "${RED}错误: 天降食物魔法次数未正确减少${NC}"
  exit 1
fi

# 7. 施展改变天气魔法
echo -e "${GREEN}7. 施展 '改变天气' 魔法...${NC}"
WEATHER_CAST_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/magic/cast" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=${WEATHER_MAGIC_CODE}&userId=秦小淮")

WEATHER_CAST_SUCCESS=$(echo "$WEATHER_CAST_RESPONSE" | jq -r '.success')
WEATHER_CAST_MESSAGE=$(echo "$WEATHER_CAST_RESPONSE" | jq -r '.message')
WEATHER_REMAINING_USES_AFTER=$(echo "$WEATHER_CAST_RESPONSE" | jq -r '.remainingUses')

if [ "$WEATHER_CAST_SUCCESS" = "true" ]; then
  echo -e "${GREEN}   改变天气魔法施展成功: ${WEATHER_CAST_MESSAGE}${NC}"
  echo -e "${GREEN}   剩余次数: ${WEATHER_REMAINING_USES_AFTER}${NC}"
else
  echo -e "${RED}错误: 改变天气魔法施展失败: ${WEATHER_CAST_MESSAGE}${NC}"
  exit 1
fi

# 8. 验证天气是否改变
echo -e "${GREEN}8. 验证天气是否改变...${NC}"
STARCITY_AFTER_WEATHER=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
WEATHER_AFTER=$(echo "$STARCITY_AFTER_WEATHER" | jq -r '.weather')
echo -e "${GREEN}   施展后天气: ${WEATHER_AFTER}${NC}"

if [ "$WEATHER_AFTER" != "$WEATHER_BEFORE" ]; then
  echo -e "${GREEN}   验证成功: 天气从 ${WEATHER_BEFORE} 改变为 ${WEATHER_AFTER}${NC}"
else
  echo -e "${YELLOW}   注意: 天气未改变（可能随机到了相同天气）${NC}"
fi

# 9. 验证改变天气魔法次数减少
if [ "$WEATHER_REMAINING_USES_AFTER" -lt "$WEATHER_REMAINING_USES_BEFORE" ]; then
  echo -e "${GREEN}   验证成功: 改变天气魔法次数从 ${WEATHER_REMAINING_USES_BEFORE} 减少到 ${WEATHER_REMAINING_USES_AFTER}${NC}"
else
  echo -e "${RED}错误: 改变天气魔法次数未正确减少${NC}"
  exit 1
fi

# 10. 测试次数用完的情况（如果还有次数，继续施展）
echo -e "${GREEN}10. 测试连续施展魔法直到用完次数...${NC}"
CURRENT_FOOD_USES=$FOOD_REMAINING_USES_AFTER
CURRENT_WEATHER_USES=$WEATHER_REMAINING_USES_AFTER

while [ "$CURRENT_FOOD_USES" -gt 0 ] || [ "$CURRENT_WEATHER_USES" -gt 0 ]; do
  if [ "$CURRENT_FOOD_USES" -gt 0 ]; then
    echo -e "${BLUE}   天降食物魔法剩余次数: ${CURRENT_FOOD_USES}, 继续施展...${NC}"
    CAST_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/magic/cast" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "code=${FOOD_MAGIC_CODE}&userId=秦小淮")
    
    CAST_SUCCESS=$(echo "$CAST_RESPONSE" | jq -r '.success')
    if [ "$CAST_SUCCESS" = "true" ]; then
      CURRENT_FOOD_USES=$(echo "$CAST_RESPONSE" | jq -r '.remainingUses')
      echo -e "${GREEN}   天降食物魔法施展成功，剩余次数: ${CURRENT_FOOD_USES}${NC}"
    else
      echo -e "${RED}   天降食物魔法施展失败: $(echo "$CAST_RESPONSE" | jq -r '.message')${NC}"
      break
    fi
  fi
  
  if [ "$CURRENT_WEATHER_USES" -gt 0 ]; then
    echo -e "${BLUE}   改变天气魔法剩余次数: ${CURRENT_WEATHER_USES}, 继续施展...${NC}"
    CAST_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/magic/cast" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "code=${WEATHER_MAGIC_CODE}&userId=秦小淮")
    
    CAST_SUCCESS=$(echo "$CAST_RESPONSE" | jq -r '.success')
    if [ "$CAST_SUCCESS" = "true" ]; then
      CURRENT_WEATHER_USES=$(echo "$CAST_RESPONSE" | jq -r '.remainingUses')
      echo -e "${GREEN}   改变天气魔法施展成功，剩余次数: ${CURRENT_WEATHER_USES}${NC}"
    else
      echo -e "${RED}   改变天气魔法施展失败: $(echo "$CAST_RESPONSE" | jq -r '.message')${NC}"
      break
    fi
  fi
done

# 11. 尝试在次数用完后再次施展
echo -e "${GREEN}11. 测试次数用完后施展魔法...${NC}"
FINAL_CAST_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/magic/cast" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "code=${FOOD_MAGIC_CODE}&userId=秦小淮")

FINAL_CAST_SUCCESS=$(echo "$FINAL_CAST_RESPONSE" | jq -r '.success')
FINAL_CAST_MESSAGE=$(echo "$FINAL_CAST_RESPONSE" | jq -r '.message')

if [ "$FINAL_CAST_SUCCESS" = "false" ]; then
  echo -e "${GREEN}   验证成功: 次数用完后无法施展。错误信息: ${FINAL_CAST_MESSAGE}${NC}"
else
  echo -e "${RED}错误: 次数用完后仍然可以施展魔法${NC}"
  exit 1
fi

echo -e "${YELLOW}=== 魔法系统测试完成 ===${NC}"
echo -e "${BLUE}提示: 魔法次数将在每天凌晨0点自动刷新为3次${NC}"
echo -e "${BLUE}支持的魔法:${NC}"
echo -e "${BLUE}  - 天降食物: 增加10000食物，播放食物下落特效${NC}"
echo -e "${BLUE}  - 改变天气: 立即刷新天气，播放天气变化特效${NC}"

