#!/bin/bash

# 测试巨人进攻功能
# 每6小时有1/5的概率出现巨人进攻，每10分钟造成0.5%的人口损失和1点幸福指数下降

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== 巨人进攻功能测试 ===${NC}"

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

# 2. 获取当前巨人进攻状态
echo -e "${GREEN}2. 获取当前巨人进攻状态...${NC}"
GIANT_STATUS=$(curl -s -X GET "http://localhost:5000/api/giant-attack/status")
IS_ATTACKING=$(echo "$GIANT_STATUS" | jq -r '.isAttacking')

echo -e "${BLUE}   当前巨人进攻状态:${NC}"
echo "$GIANT_STATUS" | jq '.'

if [ "$IS_ATTACKING" = "true" ]; then
    echo -e "${YELLOW}   巨人正在进攻中${NC}"
else
    echo -e "${GREEN}   巨人未在进攻${NC}"
fi

# 3. 获取当前星星城数据
echo -e "${GREEN}3. 获取当前星星城数据...${NC}"
STARCITY_BEFORE=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
POPULATION_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.population')
HAPPINESS_BEFORE=$(echo "$STARCITY_BEFORE" | jq -r '.happiness')

echo -e "${BLUE}   当前人口: ${POPULATION_BEFORE}${NC}"
echo -e "${BLUE}   当前幸福指数: ${HAPPINESS_BEFORE}${NC}"

# 4. 手动触发巨人进攻（管理员功能）
echo -e "${GREEN}4. 手动触发巨人进攻...${NC}"
TRIGGER_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/giant-attack/trigger" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}")

echo -e "${BLUE}   触发响应:${NC}"
echo "$TRIGGER_RESPONSE" | jq '.'

# 5. 验证巨人进攻状态
echo -e "${GREEN}5. 验证巨人进攻状态...${NC}"
GIANT_STATUS_AFTER=$(curl -s -X GET "http://localhost:5000/api/giant-attack/status")
IS_ATTACKING_AFTER=$(echo "$GIANT_STATUS_AFTER" | jq -r '.isAttacking')

echo -e "${BLUE}   触发后巨人进攻状态:${NC}"
echo "$GIANT_STATUS_AFTER" | jq '.'

if [ "$IS_ATTACKING_AFTER" = "true" ]; then
    echo -e "${GREEN}   ✅ 巨人进攻触发成功${NC}"
else
    echo -e "${RED}   ❌ 巨人进攻触发失败${NC}"
    exit 1
fi

# 6. 手动处理巨人伤害（模拟10分钟后的伤害）
echo -e "${GREEN}6. 手动处理巨人伤害...${NC}"
DAMAGE_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/giant-attack/damage" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}")

echo -e "${BLUE}   伤害处理响应:${NC}"
echo "$DAMAGE_RESPONSE" | jq '.'

# 7. 验证人口和幸福指数变化
echo -e "${GREEN}7. 验证人口和幸福指数变化...${NC}"
STARCITY_AFTER=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
POPULATION_AFTER=$(echo "$STARCITY_AFTER" | jq -r '.population')
HAPPINESS_AFTER=$(echo "$STARCITY_AFTER" | jq -r '.happiness')

echo -e "${BLUE}   伤害后人口: ${POPULATION_AFTER}${NC}"
echo -e "${BLUE}   伤害后幸福指数: ${HAPPINESS_AFTER}${NC}"

POPULATION_CHANGE=$((POPULATION_AFTER - POPULATION_BEFORE))
HAPPINESS_CHANGE=$((HAPPINESS_AFTER - HAPPINESS_BEFORE))
echo -e "${BLUE}   人口变化: ${POPULATION_CHANGE}${NC}"
echo -e "${BLUE}   幸福指数变化: ${HAPPINESS_CHANGE}${NC}"

# 计算预期的伤害（0.5%）
EXPECTED_DAMAGE=$(echo "scale=0; ${POPULATION_BEFORE} * 0.005" | bc)
echo -e "${BLUE}   预期人口伤害: ${EXPECTED_DAMAGE} (0.5%)${NC}"
echo -e "${BLUE}   预期幸福指数下降: 1${NC}"

# 验证人口伤害
if [ "$POPULATION_CHANGE" -eq "-$EXPECTED_DAMAGE" ]; then
    echo -e "${GREEN}   ✅ 巨人人口伤害验证成功: 人口减少了${EXPECTED_DAMAGE} (0.5%)${NC}"
else
    echo -e "${RED}   ❌ 巨人人口伤害验证失败: 预期减少${EXPECTED_DAMAGE}，实际变化${POPULATION_CHANGE}${NC}"
fi

# 验证幸福指数下降
if [ "$HAPPINESS_CHANGE" -eq "-1" ]; then
    echo -e "${GREEN}   ✅ 巨人幸福指数伤害验证成功: 幸福指数下降了1${NC}"
else
    echo -e "${RED}   ❌ 巨人幸福指数伤害验证失败: 预期下降1，实际变化${HAPPINESS_CHANGE}${NC}"
fi

# 8. 测试边界情况：人口为0时和幸福指数为0时
echo -e "${GREEN}8. 测试边界情况...${NC}"
if [ "$POPULATION_AFTER" -eq 0 ]; then
    echo -e "${YELLOW}   人口已为0，测试边界情况...${NC}"
    
    # 再次处理巨人伤害
    curl -s -X POST "http://localhost:5000/api/giant-attack/damage" \
      -H "Authorization: Bearer ${ADMIN_TOKEN}" > /dev/null
    
    # 检查人口和幸福指数是否仍为0
    STARCITY_FINAL=$(curl -s -X GET "http://localhost:5000/api/star-city?userId=秦小淮")
    POPULATION_FINAL=$(echo "$STARCITY_FINAL" | jq -r '.population')
    HAPPINESS_FINAL=$(echo "$STARCITY_FINAL" | jq -r '.happiness')
    
    if [ "$POPULATION_FINAL" -eq 0 ]; then
        echo -e "${GREEN}   ✅ 人口边界测试成功: 人口为0时不再减少${NC}"
    else
        echo -e "${RED}   ❌ 人口边界测试失败: 人口为0时仍然减少${NC}"
        exit 1
    fi
    
    if [ "$HAPPINESS_FINAL" -eq 0 ]; then
        echo -e "${GREEN}   ✅ 幸福指数边界测试成功: 幸福指数为0时不再减少${NC}"
    else
        echo -e "${RED}   ❌ 幸福指数边界测试失败: 幸福指数为0时仍然减少${NC}"
        exit 1
    fi
fi

# 9. 手动结束巨人进攻
echo -e "${GREEN}9. 手动结束巨人进攻...${NC}"
END_RESPONSE=$(curl -s -X POST "http://localhost:5000/api/giant-attack/end" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}")

echo -e "${BLUE}   结束响应:${NC}"
echo "$END_RESPONSE" | jq '.'

# 10. 验证巨人进攻已结束
echo -e "${GREEN}10. 验证巨人进攻已结束...${NC}"
GIANT_STATUS_FINAL=$(curl -s -X GET "http://localhost:5000/api/giant-attack/status")
IS_ATTACKING_FINAL=$(echo "$GIANT_STATUS_FINAL" | jq -r '.isAttacking')

echo -e "${BLUE}   最终巨人进攻状态:${NC}"
echo "$GIANT_STATUS_FINAL" | jq '.'

if [ "$IS_ATTACKING_FINAL" = "false" ]; then
    echo -e "${GREEN}   ✅ 巨人进攻结束成功${NC}"
else
    echo -e "${RED}   ❌ 巨人进攻结束失败${NC}"
    exit 1
fi

echo -e "${YELLOW}=== 巨人进攻功能测试完成 ===${NC}"
echo -e "${BLUE}巨人进攻机制规则:${NC}"
echo -e "${BLUE}  - 每6小时检查一次，有1/5的概率触发${NC}"
echo -e "${BLUE}  - 巨人进攻时，每10分钟造成0.5%的人口损失${NC}"
echo -e "${BLUE}  - 巨人进攻时，每10分钟幸福指数下降1${NC}"
echo -e "${BLUE}  - 巨人位置与城堡位置相同${NC}"
echo -e "${BLUE}  - 人口和幸福指数都不会低于0${NC}"
echo -e "${BLUE}  - 由GiantAttackTask定时执行${NC}"
