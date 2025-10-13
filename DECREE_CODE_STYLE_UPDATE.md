# 命令系统代码风格统一说明

## 更新内容

为了与Eden项目现有代码风格保持一致，对命令系统的后端实现进行了重构。

## 主要变更

### 1. 实体类（Decree.java）

**之前**：使用JPA注解
```java
@Entity
@Table(name = "decree")
public class Decree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    // ...
}
```

**现在**：纯POJO，不使用任何注解
```java
public class Decree {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime issuedAt;
    private LocalDateTime cancelledAt;
    private String issuedBy;
    
    // Getters, Setters, toString()
}
```

### 2. Mapper接口（DecreeMapper.java）

**之前**：使用MyBatis注解
```java
@Mapper
public interface DecreeMapper {
    @Select("SELECT * FROM decree WHERE code = #{code}")
    Decree selectDecreeByCode(String code);
    
    @Update("UPDATE decree SET active = #{active} WHERE code = #{code}")
    void updateDecree(Decree decree);
}
```

**现在**：纯接口定义，配置在XML文件中
```java
@Mapper
public interface DecreeMapper {
    void insert(Decree decree);
    Decree selectByCode(@Param("code") String code);
    void update(Decree decree);
    List<Decree> selectAll();
    List<Decree> selectActive();
    boolean isActive(@Param("code") String code);
}
```

### 3. MyBatis XML配置（新增）

**新增文件**：`DecreeMapper.xml`

完整的XML配置，包含：
- ResultMap映射
- 所有SQL语句定义
- 参数映射

```xml
<mapper namespace="com.eden.lottery.mapper.DecreeMapper">
    <resultMap id="DecreeResultMap" type="com.eden.lottery.entity.Decree">
        <id property="id" column="id"/>
        <result property="code" column="code"/>
        <!-- ... -->
    </resultMap>
    
    <select id="selectByCode" parameterType="string" resultMap="DecreeResultMap">
        SELECT id, code, name, description, active, issued_at, cancelled_at, issued_by
        FROM decree
        WHERE code = #{code}
    </select>
    <!-- ... -->
</mapper>
```

### 4. 方法命名统一

| 之前 | 现在 | 说明 |
|------|------|------|
| `selectAllDecrees()` | `selectAll()` | 统一使用简洁命名 |
| `selectDecreeByCode()` | `selectByCode()` | 统一命名规范 |
| `updateDecree()` | `update()` | 简化方法名 |
| `isDecreeActive()` | `isActive()` | 简化方法名 |

### 5. Service层适配

更新了`DecreeService`中所有调用Mapper的方法名：
```java
// 之前
decreeMapper.selectDecreeByCode(code)
decreeMapper.updateDecree(decree)

// 现在
decreeMapper.selectByCode(code)
decreeMapper.update(decree)
```

### 6. 依赖UserMapper的方法修正

```java
// 之前（不存在的方法）
String currentResidence = userMapper.getUserResidence(userName);
userMapper.updateUserResidence(userName, targetResidence);

// 现在（使用正确的方法）
User user = userMapper.selectByUserId(userName);
if (user != null && "castle".equals(user.getResidence())) {
    userMapper.updateResidence(userName, targetResidence);
}
```

### 7. 使用统一的移动方法

进一步优化，使用项目封装好的`StarCityService.moveUserToBuilding`方法：

```java
// 之前（直接调用mapper）
userMapper.updateResidence(userName, targetResidence);
// 需要手动刷新事件
residenceEventService.refreshResidenceEvents("castle");
residenceEventService.refreshResidenceEvents(targetResidence);

// 现在（使用统一移动方法）
starCityService.moveUserToBuilding(
    userName, 
    "castle", 
    targetResidence, 
    "decree"  // 标记为命令驱逐
);
// 自动刷新事件，自动记录日志，自动验证参数
```

**优势**：
- ✅ 代码复用，避免重复
- ✅ 自动刷新相关居所事件
- ✅ 统一的错误处理和日志
- ✅ 支持移动原因标记（"decree"）
- ✅ 与其他移动场景保持一致

## 统一后的优势

1. **一致性**：与项目其他实体（User、Prize、Wish等）保持相同的代码风格
2. **可维护性**：XML配置集中管理SQL语句，便于修改和优化
3. **可读性**：代码结构清晰，符合团队习惯
4. **灵活性**：XML方式可以编写更复杂的SQL语句

## 代码风格规范

### 实体类规范
- 不使用任何JPA或MyBatis注解
- 纯POJO，只包含属性、构造方法、Getter/Setter、toString()
- 字段使用驼峰命名（Java）
- 添加完整的JavaDoc注释

### Mapper接口规范
- 只保留`@Mapper`注解
- 参数使用`@Param`注解标注
- 方法命名简洁直接：`insert`, `update`, `selectByXxx`, `selectAll`
- 返回boolean的方法以`is`开头

### XML配置规范
- 定义ResultMap进行字段映射
- 数据库字段使用下划线命名
- SQL语句格式化，易于阅读
- 每个方法对应一个XML配置

### Service层规范
- 使用`@Autowired`注入Mapper
- 完整的JavaDoc注释
- 详细的日志记录
- 完善的异常处理

## 文件清单

修改的文件：
- ✅ `entity/Decree.java` - 移除JPA注解，改为纯POJO
- ✅ `mapper/DecreeMapper.java` - 移除MyBatis注解，改为纯接口
- ✅ `service/DecreeService.java` - 更新方法调用，使用统一移动方法
- ✅ 新增 `mapper/DecreeMapper.xml` - MyBatis XML配置

主要改进点：
1. **代码风格统一**：与User、Prize等实体保持一致
2. **使用封装方法**：用`StarCityService.moveUserToBuilding`替代直接调用mapper
3. **依赖优化**：从`ResidenceEventService`改为`StarCityService`
4. **代码简化**：移除手动刷新事件的代码，由统一方法自动处理

无需修改的文件：
- ✅ `controller/DecreeController.java` - 无变化
- ✅ 前端所有文件 - 无变化
- ✅ `PrizeInitService.java` - 只涉及数据库迁移，无变化

## 测试

所有功能保持不变，可以使用原测试脚本：
```bash
./test-decree.sh
```

预期结果与之前完全一致。

## 参考

统一风格参考了项目中的：
- `entity/User.java`
- `mapper/UserMapper.java`
- `mapper/UserMapper.xml`

---

✅ 代码风格已统一完成！

