# 🔧 废墟状态数据库字段问题修复说明

## 🚨 问题描述

后台设置废墟状态显示成功，但数据库中的废墟状态字段还是0，说明数据库字段映射有问题。

## 🔍 问题原因

1. **StarCityMapper.xml缺少字段映射**: resultMap和SQL查询中没有包含`is_ruins`字段
2. **数据库字段类型问题**: SQLite的BOOLEAN类型可能有问题，应该使用INTEGER类型

## ✅ 修复内容

### 1. 更新StarCityMapper.xml

#### 结果映射添加is_ruins字段
```xml
<result column="is_ruins" property="isRuins"/>
```

#### 查询语句添加is_ruins字段
```sql
SELECT id, population, food, happiness, level, weather, is_ruins, last_update_time, create_time, update_time
```

#### 插入语句添加is_ruins字段
```sql
INSERT INTO star_city (population, food, happiness, level, weather, is_ruins, last_update_time, create_time, update_time)
VALUES (#{population}, #{food}, #{happiness}, #{level}, #{weather}, #{isRuins}, #{lastUpdateTime}, #{createTime}, #{updateTime})
```

#### 更新语句添加is_ruins字段
```sql
UPDATE star_city 
SET population = #{population},
    food = #{food},
    happiness = #{happiness},
    level = #{level},
    weather = #{weather},
    is_ruins = #{isRuins},
    last_update_time = #{lastUpdateTime},
    update_time = #{updateTime}
WHERE id = #{id}
```

### 2. 修改数据库迁移代码

#### 字段类型从BOOLEAN改为INTEGER
```java
// 修改前
statement.execute("ALTER TABLE star_city ADD COLUMN is_ruins BOOLEAN DEFAULT 0");

// 修改后  
statement.execute("ALTER TABLE star_city ADD COLUMN is_ruins INTEGER DEFAULT 0");
```

## 🚀 修复步骤

### 1. 重启后端服务
```bash
cd eden-server
mvn spring-boot:run
```

### 2. 验证数据库字段
```bash
./check-db-ruins-field.sh
```

### 3. 测试废墟状态功能
```bash
./test-db-ruins-field.sh
```

## 🔍 验证方法

### 1. 检查数据库表结构
```sql
PRAGMA table_info(star_city);
```
应该看到`is_ruins`字段，类型为`INTEGER`

### 2. 检查数据库数据
```sql
SELECT id, population, food, happiness, level, weather, is_ruins FROM star_city ORDER BY id DESC LIMIT 1;
```

### 3. 测试API接口
```bash
# 设置废墟状态
curl -X POST "http://localhost:5000/api/star-city/admin/set-ruins" \
  -H "Content-Type: application/json" \
  -d '{"isRuins": true}'

# 检查结果
curl "http://localhost:5000/api/star-city/info" | jq '.data.isRuins'
```

## 📋 预期结果

修复后：
- 数据库中的`is_ruins`字段应该正确更新为1（true）或0（false）
- API返回的`isRuins`字段应该与数据库中的值一致
- 废墟状态功能应该正常工作

## ⚠️ 注意事项

1. **重启服务**: 修改MyBatis映射文件后必须重启后端服务
2. **数据库迁移**: 如果数据库中没有`is_ruins`字段，重启服务时会自动添加
3. **字段类型**: SQLite中BOOLEAN类型建议使用INTEGER类型（0=false, 1=true）

---

**修复完成时间**: 2025年1月21日  
**修复状态**: ✅ 已完成  
**测试状态**: ✅ 测试脚本已创建
