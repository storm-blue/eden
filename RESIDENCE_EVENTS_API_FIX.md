# 居所事件历史概览API修复

## 🐛 **问题描述**

**错误信息**：
```
java.lang.UnsupportedOperationException: null
	at java.base/java.util.ImmutableCollections.uoe(ImmutableCollections.java:142)
	at java.base/java.util.ImmutableCollections$AbstractImmutableMap.put(ImmutableCollections.java:1079)
	at com.eden.lottery.controller.AdminController.getResidenceEventHistoryOverview(AdminController.java:720)
```

**问题原因**：
1. `ResidenceEventService.getEventHistoryStats()` 返回的是通过 `Map.of()` 创建的不可变 `Map`
2. `AdminController` 尝试向这个不可变 `Map` 添加 `residenceName` 字段
3. 不可变集合不允许修改操作，导致 `UnsupportedOperationException`

## ✅ **修复方案**

### 1. **修复AdminController中的Map操作**

**文件**：`eden-server/src/main/java/com/eden/lottery/controller/AdminController.java`

**修改前**：
```java
Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);
stats.put("residenceName", getResidenceName(residence)); // ❌ 向不可变Map添加元素
```

**修改后**：
```java
Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);

// 创建一个新的可变Map来避免UnsupportedOperationException
Map<String, Object> residenceStats = new java.util.HashMap<>(stats);
residenceStats.put("residenceName", getResidenceName(residence));
```

### 2. **统一字段命名**

**文件**：`eden-server/src/main/java/com/eden/lottery/service/ResidenceEventService.java`

**问题**：前端期望 `totalCount` 字段，但后端返回 `historyCount`

**修改前**：
```java
return Map.of(
    "residence", residence,
    "historyCount", count,        // ❌ 字段名不匹配
    "totalHistoryCount", totalCount,
    "lastUpdated", LocalDateTime.now()
);
```

**修改后**：
```java
return Map.of(
    "residence", residence,
    "totalCount", count,          // ✅ 匹配前端期望
    "globalTotalCount", totalCount,
    "lastUpdated", LocalDateTime.now()
);
```

## 🔍 **根本原因分析**

### **Java不可变集合**
- `Map.of()` 创建的是不可变集合（JDK 9+）
- 不可变集合的优势：线程安全、防止意外修改
- 不可变集合的限制：不能添加、删除或修改元素

### **解决策略**
1. **复制策略**：创建可变副本 `new HashMap<>(immutableMap)`
2. **构建策略**：在服务层就构建完整的数据结构
3. **封装策略**：返回包装对象而非直接的Map

## 🧪 **测试验证**

创建了测试脚本 `test-admin-residence-events-fix.sh` 验证修复：

```bash
./test-admin-residence-events-fix.sh
```

**验证要点**：
1. ✅ API调用不再抛出异常
2. ✅ 返回数据包含 `totalCount` 字段
3. ✅ 返回数据包含 `residenceName` 字段
4. ✅ 所有居所数据结构正确

## 📋 **修复文件清单**

1. **AdminController.java**
   - 修复不可变Map操作问题
   - 使用HashMap副本策略

2. **ResidenceEventService.java**
   - 统一字段命名（historyCount → totalCount）
   - 修复异常情况下的返回值

3. **test-admin-residence-events-fix.sh**
   - 新增API测试脚本
   - 验证修复效果

## 🚀 **部署步骤**

1. **重新编译后端**：
   ```bash
   cd eden-server
   mvn clean compile
   ```

2. **重启后端服务**：
   ```bash
   sudo systemctl restart eden-backend
   # 或手动重启
   ./start.sh
   ```

3. **验证修复**：
   ```bash
   ./test-admin-residence-events-fix.sh
   ```

4. **测试管理界面**：
   - 访问 `http://localhost:5000/admin.html`
   - 登录管理员账户
   - 点击"居所事件"标签页
   - 点击"🔄 刷新概览"按钮

## 💡 **经验总结**

1. **使用不可变集合时要注意其限制**
2. **API设计时要考虑字段命名的一致性**
3. **前后端字段名要保持同步**
4. **复杂数据结构建议使用DTO类而非Map**
5. **及时添加单元测试避免此类问题**

修复完成后，管理界面的居所事件历史概览功能应该能正常工作了！🎉
