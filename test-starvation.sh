#!/bin/bash

# 测试星星城人口饥饿机制
# 当食物少于人口时，每小时人口减少2000

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== 星星城人口饥饿机制测试 ===${NC}"

# 1. 获取管理员Token
echo -e "${GREEN}1. 获取管理员Token...${NC}"
ADMIN_TOKEN=$(curl -s -X POST "http://localhost:5000/api/admin/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin123" | jq -r '.token')

if [ "$ADMIN_TOKEN" = "null" ] || [ -z "$ADMIN_TOKEN" ]; then
  echo -e "${RED}错误: 无法获取管理员Token，请检查后端服务是否运行或登录凭据是否正确。${NC}"
  exit 1
fi
echo -e "${GREEN}   管理员Token获取成功。${NC}"

# 2. 获取当前星星城数据
echo -e "${GREEN}2. 获取当前星星城数据...${NC}"
STARCITY_BEFORE=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
FOOD_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.food')
POPULATION_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.population')
HAPPINESS_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.happiness')

echo -e "${BLUE}   当前数据:${NC}"
echo -e "${BLUE}   食物: ${FOOD_BEFORE}${NC}"
echo -e "${BLUE}   人口: ${POPULATION_BEFORE}${NC}"
echo -e "${BLUE}   幸福度: ${HAPPINESS_BEFORE}${NC}"

# 3. 检查是否满足饥饿条件
echo -e "${GREEN}3. 检查饥饿条件...${NC}"
if [ "$FOOD_BEFORE" -lt "$POPULATION_BEFORE" ]; then
    echo -e "${YELLOW}   当前食物(${FOOD_BEFORE}) < 人口(${POPULATION_BEFORE})，满足饥饿条件${NC}"
    EXPECTED_POPULATION=$((POPULATION_BEFORE - 2000))
    echo -e "${YELLOW}   预期人口减少2000，新人口应为: ${EXPECTED_POPULATION}${NC}"
else
    echo -e "${GREEN}   当前食物(${FOOD_BEFORE}) >= 人口(${POPULATION_BEFORE})，不满足饥饿条件${NC}"
    echo -e "${GREEN}   预期人口不变: ${POPULATION_BEFORE}${NC}"
    EXPECTED_POPULATION=$POPULATION_BEFORE
fi

# 4. 手动触发饥饿检查（模拟每小时任务）
echo -e "${GREEN}4. 手动触发饥饿检查...${NC}"
STARVATION_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/admin/test-hourly-tasks" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}")

echo -e "${BLUE}   饥饿检查响应:${NC}"
echo "$STARVATION_RESPONSE" | jq '.'

# 5. 验证结果
echo -e "${GREEN}5. 验证饥饿检查结果...${NC}"
STARCITY_AFTER=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
FOOD_AFTER=$(echo "$STARCITY_AFTER" | jq -r '.food')
POPULATION_AFTER=$(echo "$STARCITY_AFTER" | jq -r '.population')
HAPPINESS_AFTER=$(echo "$STARCITY_AFTER" | jq -r '.happiness')

echo -e "${BLUE}   检查后数据:${NC}"
echo -e "${BLUE}   食物: ${FOOD_AFTER}${NC}"
echo -e "${BLUE}   人口: ${POPULATION_AFTER}${NC}"
echo -e "${BLUE}   幸福度: ${HAPPINESS_AFTER}${NC}"

# 6. 验证人口变化
echo -e "${GREEN}6. 验证人口变化...${NC}"
POPULATION_CHANGE=$((POPULATION_AFTER - POPULATION_BEFORE))

if [ "$POPULATION_CHANGE" -eq -2000 ] && [ "$FOOD_BEFORE" -lt "$POPULATION_BEFORE" ]; then
    echo -e "${GREEN}   ✅ 验证成功: 人口减少了2000 (${POPULATION_CHANGE})${NC}"
elif [ "$POPULATION_CHANGE" -eq 0 ] && [ "$FOOD_BEFORE" -ge "$POPULATION_BEFORE" ]; then
    echo -e "${GREEN}   ✅ 验证成功: 人口未变化 (${POPULATION_CHANGE})，因为食物充足${NC}"
else
    echo -e "${RED}   ❌ 验证失败: 人口变化异常${NC}"
    echo -e "${RED}   预期变化: ${EXPECTED_POPULATION} - ${POPULATION_BEFORE} = $((EXPECTED_POPULATION - POPULATION_BEFORE))${NC}"
    echo -e "${RED}   实际变化: ${POPULATION_CHANGE}${NC}"
    exit 1
fi

# 7. 测试边界情况：人口为0时
echo -e "${GREEN}7. 测试边界情况...${NC}"
if [ "$POPULATION_AFTER" -eq 0 ]; then
    echo -e "${YELLOW}   人口已为0，测试边界情况...${NC}"
    
    # 再次触发饥饿检查
    curl -s -X POST "http://localhost:5000/api/admin/test-hourly-tasks" \
      -H "Authorization: Bearer ${ADMIN_TOKEN}" > /dev/null
    
    # 检查人口是否仍为0
    STARCITY_FINAL=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
    POPULATION_FINAL=$(echo "$STARCITY_FINAL" | jq -r '.population')
    
    if [ "$POPULATION_FINAL" -eq 0 ]; then
        echo -e "${GREEN}   ✅ 边界测试成功: 人口为0时不再减少${NC}"
    else
        echo -e "${RED}   ❌ 边界测试失败: 人口为0时仍然减少${NC}"
        exit 1
    fi
fi

echo -e "${YELLOW}=== 饥饿机制测试完成 ===${NC}"
echo -e "${BLUE}饥饿机制规则:${NC}"
echo -e "${BLUE}  - 每小时检查一次${NC}"
echo -e "${BLUE}  - 当食物 < 人口时，人口减少2000${NC}"
echo -e "${BLUE}  - 人口不会低于0${NC}"
echo -e "${BLUE}  - 由HourlyRefreshTask每小时自动执行${NC}"
