#!/bin/bash

# 测试星星城等级系统 (LV6-LV8)
# 作者: AI Assistant
# 日期: $(date)

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🌟 星星城等级系统测试 (LV6-LV8)${NC}"
echo "=================================="

# 管理员登录
echo -e "\n${YELLOW}1. 管理员登录${NC}"
ADMIN_LOGIN=$(curl -s -X POST "http://localhost:5000/api/admin/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin123")

ADMIN_TOKEN=$(echo "$ADMIN_LOGIN" | jq -r '.data.token')
if [ "$ADMIN_TOKEN" != "null" ] && [ -n "$ADMIN_TOKEN" ]; then
  echo -e "${GREEN}✅ 管理员登录成功${NC}"
else
  echo -e "${RED}❌ 管理员登录失败${NC}"
  exit 1
fi

# 获取当前星星城数据
echo -e "\n${YELLOW}2. 获取当前星星城数据${NC}"
STARCITY_DATA=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
CURRENT_LEVEL=$(echo "$STARCITY_DATA" | jq -r '.level')
CURRENT_POPULATION=$(echo "$STARCITY_DATA" | jq -r '.population')
CURRENT_FOOD=$(echo "$STARCITY_DATA" | jq -r '.food')
CURRENT_HAPPINESS=$(echo "$STARCITY_DATA" | jq -r '.happiness')

echo -e "当前等级: ${BLUE}LV${CURRENT_LEVEL}${NC}"
echo -e "当前人口: ${BLUE}${CURRENT_POPULATION}${NC}"
echo -e "当前食物: ${BLUE}${CURRENT_FOOD}${NC}"
echo -e "当前幸福: ${BLUE}${CURRENT_HAPPINESS}${NC}"

# 测试LV6升级条件
echo -e "\n${YELLOW}3. 测试LV6升级条件 (人口150万，食物150万，幸福150)${NC}"
LV6_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "population": 1500000,
    "food": 1500000,
    "happiness": 150
  }')

LV6_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
LV6_LEVEL=$(echo "$LV6_RESULT" | jq -r '.level')
LV6_INFO=$(echo "$LV6_RESULT" | jq -r '.levelInfo')

if [ "$LV6_LEVEL" = "6" ]; then
  echo -e "${GREEN}✅ LV6升级成功: ${LV6_INFO}${NC}"
else
  echo -e "${RED}❌ LV6升级失败，当前等级: ${LV6_LEVEL}${NC}"
fi

# 测试LV7升级条件
echo -e "\n${YELLOW}4. 测试LV7升级条件 (人口300万，食物300万，幸福300)${NC}"
LV7_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "population": 3000000,
    "food": 3000000,
    "happiness": 300
  }')

LV7_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
LV7_LEVEL=$(echo "$LV7_RESULT" | jq -r '.level')
LV7_INFO=$(echo "$LV7_RESULT" | jq -r '.levelInfo')

if [ "$LV7_LEVEL" = "7" ]; then
  echo -e "${GREEN}✅ LV7升级成功: ${LV7_INFO}${NC}"
else
  echo -e "${RED}❌ LV7升级失败，当前等级: ${LV7_LEVEL}${NC}"
fi

# 测试LV8升级条件
echo -e "\n${YELLOW}5. 测试LV8升级条件 (人口500万，食物500万，幸福500)${NC}"
LV8_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "population": 5000000,
    "food": 5000000,
    "happiness": 500
  }')

LV8_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
LV8_LEVEL=$(echo "$LV8_RESULT" | jq -r '.level')
LV8_INFO=$(echo "$LV8_RESULT" | jq -r '.levelInfo')

if [ "$LV8_LEVEL" = "8" ]; then
  echo -e "${GREEN}✅ LV8升级成功: ${LV8_INFO}${NC}"
else
  echo -e "${RED}❌ LV8升级失败，当前等级: ${LV8_LEVEL}${NC}"
fi

# 测试LV9升级条件
echo -e "\n${YELLOW}6. 测试LV9升级条件 (人口1000万，食物1000万，幸福1000)${NC}"
LV9_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "population": 10000000,
    "food": 10000000,
    "happiness": 1000
  }')

LV9_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
LV9_LEVEL=$(echo "$LV9_RESULT" | jq -r '.level')
LV9_INFO=$(echo "$LV9_RESULT" | jq -r '.levelInfo')

if [ "$LV9_LEVEL" = "9" ]; then
  echo -e "${GREEN}✅ LV9升级成功: ${LV9_INFO}${NC}"
else
  echo -e "${RED}❌ LV9升级失败，当前等级: ${LV9_LEVEL}${NC}"
fi

# 测试LV10升级条件
echo -e "\n${YELLOW}7. 测试LV10升级条件 (人口2000万，食物2000万，幸福2000)${NC}"
LV10_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "population": 20000000,
    "food": 20000000,
    "happiness": 2000
  }')

LV10_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
LV10_LEVEL=$(echo "$LV10_RESULT" | jq -r '.level')
LV10_INFO=$(echo "$LV10_RESULT" | jq -r '.levelInfo')
LV10_CAN_UPGRADE=$(echo "$LV10_RESULT" | jq -r '.canUpgrade')

if [ "$LV10_LEVEL" = "10" ]; then
  echo -e "${GREEN}✅ LV10升级成功: ${LV10_INFO}${NC}"
  if [ "$LV10_CAN_UPGRADE" = "false" ]; then
    echo -e "${GREEN}✅ LV10是最高等级，无法继续升级${NC}"
  else
    echo -e "${YELLOW}⚠️ LV10仍可升级，可能存在问题${NC}"
  fi
else
  echo -e "${RED}❌ LV10升级失败，当前等级: ${LV10_LEVEL}${NC}"
fi

# 测试升级条件显示
echo -e "\n${YELLOW}8. 测试升级条件显示${NC}"
echo -e "LV6升级条件: 人口150万，食物150万，幸福150"
echo -e "LV7升级条件: 人口300万，食物300万，幸福300"
echo -e "LV8升级条件: 人口500万，食物500万，幸福500"
echo -e "LV9升级条件: 人口1000万，食物1000万，幸福1000"
echo -e "LV10升级条件: 人口2000万，食物2000万，幸福2000"

# 恢复原始数据
echo -e "\n${YELLOW}9. 恢复原始数据${NC}"
RESTORE_TEST=$(curl -s -X POST "http://localhost:5000/api/admin/set-star-city" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d "{
    \"population\": ${CURRENT_POPULATION},
    \"food\": ${CURRENT_FOOD},
    \"happiness\": ${CURRENT_HAPPINESS}
  }")

RESTORE_RESULT=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
RESTORE_LEVEL=$(echo "$RESTORE_RESULT" | jq -r '.level')

if [ "$RESTORE_LEVEL" = "$CURRENT_LEVEL" ]; then
  echo -e "${GREEN}✅ 数据恢复成功，等级: LV${RESTORE_LEVEL}${NC}"
else
  echo -e "${YELLOW}⚠️ 数据恢复后等级: LV${RESTORE_LEVEL} (原始: LV${CURRENT_LEVEL})${NC}"
fi

echo -e "\n${BLUE}🌟 等级系统测试完成！${NC}"
echo "=================================="
