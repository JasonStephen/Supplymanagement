## 一、后端层级的介绍

### 1. **Controller 层**

   - **职责**：负责接收 HTTP 请求，解析请求参数，调用 Service 层处理业务逻辑，并返回响应。
   - **常用注解**：
     - `@RestController`：标识该类为控制器，并自动将返回值转换为 JSON。
     - `@RequestMapping`、`@GetMapping`、`@PostMapping` 等：定义请求路径和 HTTP 方法。
     - `@RequestParam`、`@PathVariable`、`@RequestBody`：用于接收请求参数。
   - **示例**：
     
     ```java
     @RestController
     @RequestMapping("/api/users")
     public class UserController {
     
         @Autowired
         private UserService userService;
     
         @GetMapping("/{id}")
         public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
             UserDTO user = userService.getUserById(id);
             return ResponseEntity.ok(user);
         }
     
         @PostMapping
         public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
             UserDTO createdUser = userService.createUser(userDTO);
             return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
         }
     }
     ```

---

### 2. **Service 层**
   - **职责**：实现核心业务逻辑，处理来自 Controller 层的请求，并调用 DAO/Repository 层进行数据操作。
   - **常用注解**：
     - `@Service`：标识该类为服务层组件。
     - `@Transactional`：用于声明事务管理。
   - **示例**：
     ```java
     @Service
     public class UserService {
     
         @Autowired
         private UserRepository userRepository;
     
         public UserDTO getUserById(Long id) {
             User user = userRepository.findById(id)
                     .orElseThrow(() -> new ResourceNotFoundException("User not found"));
             return convertToDTO(user);
         }
     
         @Transactional
         public UserDTO createUser(UserDTO userDTO) {
             User user = convertToEntity(userDTO);
             User savedUser = userRepository.save(user);
             return convertToDTO(savedUser);
         }
     
         private UserDTO convertToDTO(User user) {
             // 转换逻辑
         }
     
         private User convertToEntity(UserDTO userDTO) {
             // 转换逻辑
         }
     }
     ```

---

### 3. **DAO/Repository 层**
   - **职责**：负责与数据库交互，执行 CRUD 操作。
   - **常用注解**：
     - `@Repository`：标识该类为数据访问层组件。
     - Spring Data JPA 提供的接口：如 `JpaRepository`、`CrudRepository`。
   - **示例**：
     ```java
     @Repository
     public interface UserRepository extends JpaRepository<User, Long> {
         Optional<User> findByEmail(String email);
     }
     ```

---

### 4. **Model/Entity 层**
   - **职责**：定义数据模型，通常与数据库表一一对应。
   - **常用注解**：
     - `@Entity`：标识该类为实体类，映射到数据库表。
     - `@Table`：指定数据库表名。
     - `@Id`、`@GeneratedValue`：定义主键及其生成策略。
     - `@Column`：定义字段与数据库列的映射关系。
   - **示例**：
     ```java
     @Entity
     @Table(name = "users")
     public class User {
     
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long id;
     
         @Column(nullable = false, unique = true)
         private String email;
     
         @Column(nullable = false)
         private String password;
     
         // Getters and Setters
     }
     ```

---

### 5. **DTO（Data Transfer Object）**
   - **职责**：用于在不同层之间传输数据，避免直接暴露实体类，增强安全性。
   - **常用工具**：如 MapStruct、ModelMapper 用于实体类与 DTO 之间的转换。
   - **示例**：
     ```java
     public class UserDTO {
         private Long id;
         private String email;
         private String password;
     
         // Getters and Setters
     }
     ```

---

### 6. **Configuration 层**
   - **职责**：定义项目的配置类，用于配置 Bean、数据库连接、缓存等。
   - **常用注解**：
     - `@Configuration`：标识该类为配置类。
     - `@Bean`：定义 Spring Bean。
     - `@EnableWebSecurity`、`@EnableCaching` 等：启用特定功能。
   - **示例**：
     ```java
     @Configuration
     public class AppConfig {
     
         @Bean
         public PasswordEncoder passwordEncoder() {
             return new BCryptPasswordEncoder();
         }
     }
     ```

---

### 7. **Utility 层**
   - **职责**：提供通用的工具类或方法，如日期处理、字符串处理、加密解密等。
   - **示例**：
     ```java
     public class StringUtils {
     
         public static boolean isEmpty(String str) {
             return str == null || str.trim().isEmpty();
         }
     }
     ```

---

### 8. **Exception 层**
   - **职责**：定义自定义异常和全局异常处理。
   - **常用注解**：
     - `@ControllerAdvice`：定义全局异常处理类。
     - `@ExceptionHandler`：处理特定异常。
   - **示例**：
     ```java
     @ControllerAdvice
     public class GlobalExceptionHandler {
     
         @ExceptionHandler(ResourceNotFoundException.class)
         public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
         }
     }
     ```

---

### 9. **Resources 目录**
   - **配置文件**：`application.properties` 或 `application.yml`。
   - **静态资源**：如 HTML、CSS、JS 文件。
   - **SQL 脚本**：用于数据库迁移（如 Flyway、Liquibase）。

---

### 10. **测试目录**
   - **单元测试**：`src/test/java` 下的测试类。
   - **集成测试**：测试 Controller、Service 等层的集成功能。

---

### 典型项目结构示例
```
src/main/java
├── com.example.demo
│   ├── controller
│   │   └── UserController.java
│   ├── service
│   │   └── UserService.java
│   ├── repository
│   │   └── UserRepository.java
│   ├── model
│   │   └── User.java
│   ├── dto
│   │   └── UserDTO.java
│   ├── config
│   │   └── AppConfig.java
│   ├── exception
│   │   └── GlobalExceptionHandler.java
│   └── utils
│       └── StringUtils.java
src/main/resources
├── application.yml
├── static
└── templates
src/test/java
└── com.example.demo
    ├── controller
    └── service
```

通过这种分层结构，Spring Boot 项目可以清晰地分离关注点，便于团队协作和后续维护。



## 二、后端的书写顺序

在编写 Spring Boot 后端项目时，通常的撰写顺序是从**数据模型层**开始，逐步向上层推进，最后完成 **Controller 层** 和 **测试**。这种自底向上的顺序有助于确保每一层都建立在稳定的基础上。以下是推荐的撰写顺序和详细说明：

---

### 1. **Entity 层**

- **作用**: 定义数据模型，通常与数据库表对应。
- **理由**: 先定义数据模型，确保后续的DAO、Service等层有明确的数据结构依赖。

### 2. **DAO (Repository) 层**

- **作用**: 负责与数据库交互，提供数据的增删改查操作。
- **理由**: 数据访问是业务逻辑的基础，先实现DAO层可以为Service层提供数据支持。

### 3. **Service 层**

- **作用**: 实现核心业务逻辑，调用DAO层进行数据处理。
- **理由**: 在DAO层完成后，Service层可以基于数据访问实现具体的业务逻辑。

### 4. **Controller 层**

- **作用**: 处理HTTP请求，调用Service层并返回响应。
- **理由**: 在业务逻辑完成后，Controller层负责将业务逻辑暴露给前端或其他客户端。

### 5. **Configuration 层**

- **作用**: 配置应用，如数据库连接、Bean注入等。
- **理由**: 配置层通常贯穿整个应用，可以在其他层完成后进行细化和调整。

### 6. **Utility 层**

- **作用**: 提供工具类或通用方法，如日期处理、字符串操作等。
- **理由**: 工具类通常独立于业务逻辑，可以在需要时逐步添加。

### 总结

推荐的编写顺序是：**Entity → DAO → Service → Controller → Configuration → Utility**。这种顺序确保了代码的依赖关系清晰，便于逐步构建和测试。



## 三、服务接口与实现类

是的，这是一种非常常见的做法，尤其是在中大型项目中。将 **Service 层** 分为 **接口（Interface）** 和 **实现类（Impl）** 是一种良好的设计模式，符合面向对象编程中的 **接口隔离原则** 和 **依赖倒置原则**。以下是对这种设计的详细介绍：

---

### 1. **为什么需要将 Service 分为接口和实现类？**
   - **解耦**：接口定义了服务的行为，而实现类负责具体实现。这种分离使得代码更加灵活，便于替换实现或扩展功能。
   - **多态性**：可以通过不同的实现类来实现不同的行为（例如，针对不同环境或需求提供不同的实现）。
   - **测试方便**：在单元测试中，可以通过 Mock 接口来模拟 Service 的行为，而不需要依赖具体的实现。
   - **代码清晰**：接口明确了服务的功能，实现类专注于具体逻辑，代码结构更加清晰。

---

### 2. **Service 接口（Interface）**
   - **职责**：定义 Service 层的方法签名，明确服务的功能。
   - **特点**：
     - 只声明方法，不包含具体实现。
     - 方法命名应清晰表达其功能。
   - **示例**：
     ```java
     public interface UserService {
         UserDTO getUserById(Long id);
         UserDTO createUser(UserDTO userDTO);
         void deleteUser(Long id);
     }
     ```

---

### 3. **Service 实现类（Impl）**
   - **职责**：实现 Service 接口中定义的方法，包含具体的业务逻辑。
   - **特点**：
     - 使用 `@Service` 注解标识为 Spring 管理的 Bean。
     - 通过 `@Autowired` 注入依赖（如 Repository）。
     - 实现接口中定义的所有方法。
   - **示例**：
     ```java
     @Service
     public class UserServiceImpl implements UserService {
     
         @Autowired
         private UserRepository userRepository;
     
         @Override
         public UserDTO getUserById(Long id) {
             User user = userRepository.findById(id)
                     .orElseThrow(() -> new ResourceNotFoundException("User not found"));
             return convertToDTO(user);
         }
     
         @Override
         @Transactional
         public UserDTO createUser(UserDTO userDTO) {
             User user = convertToEntity(userDTO);
             User savedUser = userRepository.save(user);
             return convertToDTO(savedUser);
         }
     
         @Override
         @Transactional
         public void deleteUser(Long id) {
             userRepository.deleteById(id);
         }
     
         private UserDTO convertToDTO(User user) {
             // 转换逻辑
         }
     
         private User convertToEntity(UserDTO userDTO) {
             // 转换逻辑
         }
     }
     ```

---

### 4. **项目结构示例**
   - 典型的项目结构如下：
     ```
     src/main/java
     ├── com.example.demo
     │   ├── service
     │   │   ├── UserService.java          // Service 接口
     │   │   └── UserServiceImpl.java      // Service 实现类
     │   ├── repository
     │   │   └── UserRepository.java
     │   ├── controller
     │   │   └── UserController.java
     │   └── model
     │       └── User.java
     ```

---

### 5. **Controller 层如何使用 Service 接口**
   - 在 Controller 中，直接注入 Service 接口，而不是实现类。Spring 会自动找到对应的实现类并注入。
   - **示例**：
     ```java
     @RestController
     @RequestMapping("/api/users")
     public class UserController {
     
         @Autowired
         private UserService userService;  // 注入接口
     
         @GetMapping("/{id}")
         public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
             UserDTO user = userService.getUserById(id);
             return ResponseEntity.ok(user);
         }
     
         @PostMapping
         public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
             UserDTO createdUser = userService.createUser(userDTO);
             return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
         }
     }
     ```

---

### 6. **优点总结**
   - **灵活性**：可以轻松替换实现类，而不影响其他代码。
   - **可扩展性**：可以通过新增实现类来扩展功能。
   - **可测试性**：方便通过 Mock 接口进行单元测试。
   - **代码清晰**：接口和实现分离，代码结构更加清晰。

---

### 7. **适用场景**
   - **中大型项目**：需要更高的灵活性和可维护性。
   - **多环境支持**：例如，针对开发环境和生产环境提供不同的实现。
   - **团队协作**：接口定义明确，便于团队成员分工合作。

---

### 8. **注意事项**
   - **不要过度设计**：如果项目规模较小，业务逻辑简单，可以直接在 Service 类中实现，而不需要拆分接口和实现类。
   - **接口命名规范**：接口通常以 `Service` 结尾，实现类以 `ServiceImpl` 结尾。
   - **避免空接口**：如果接口中只有一个实现类，且没有扩展需求，可以考虑直接使用类。

---

通过将 Service 层分为接口和实现类，可以显著提高代码的可维护性和扩展性，是一种值得推荐的实践！



## 四、DAO层安排

### **1. RoleDAO**

- **功能**: 负责`Role`表的数据访问。
- **操作**:
  - 新增角色
  - 根据ID查询角色
  - 查询所有角色
  - 更新角色信息
  - 删除角色

------

### **2. UserDAO**

- **功能**: 负责`User`表的数据访问。
- **操作**:
  - 新增用户
  - 根据ID查询用户
  - 根据用户名查询用户
  - 查询所有用户
  - 更新用户信息
  - 删除用户
  - 根据角色ID查询用户列表

------

### **3. PermissionDAO**

- **功能**: 负责`Permission`表的数据访问。
- **操作**:
  - 新增权限
  - 根据ID查询权限
  - 查询所有权限
  - 更新权限信息
  - 删除权限

------

### **4. RolePermissionDAO**

- **功能**: 负责`Role_Permission`表的数据访问。
- **操作**:
  - 为角色分配权限
  - 根据角色ID查询权限列表
  - 删除角色的某个权限
  - 删除角色的所有权限

------

### **5. SupplierDAO**

- **功能**: 负责`Supplier`表的数据访问。
- **操作**:
  - 新增供应商
  - 根据ID查询供应商
  - 查询所有供应商
  - 更新供应商信息
  - 删除供应商

------

### **6. CustomerDAO**

- **功能**: 负责`Customer`表的数据访问。
- **操作**:
  - 新增客户
  - 根据ID查询客户
  - 查询所有客户
  - 更新客户信息
  - 删除客户

------

### **7. ProductCategoryDAO**

- **功能**: 负责`Product_Category`表的数据访问。
- **操作**:
  - 新增产品类别
  - 根据ID查询产品类别
  - 查询所有产品类别
  - 更新产品类别信息
  - 删除产品类别
  - 查询子类别列表

------

### **8. ProductDAO**

- **功能**: 负责`Product`表的数据访问。
- **操作**:
  - 新增产品
  - 根据ID查询产品
  - 查询所有产品
  - 更新产品信息
  - 删除产品
  - 根据类别ID查询产品列表

------

### **9. PurchaseOrderDAO**

- **功能**: 负责`Purchase_Order`表的数据访问。
- **操作**:
  - 新增采购订单
  - 根据ID查询采购订单
  - 查询所有采购订单
  - 更新采购订单信息
  - 删除采购订单
  - 根据供应商ID查询采购订单列表

------

### **10. PurchaseContractDAO**

- **功能**: 负责`Purchase_Contract`表的数据访问。
- **操作**:
  - 新增采购合同
  - 根据ID查询采购合同
  - 查询所有采购合同
  - 更新采购合同信息
  - 删除采购合同
  - 根据供应商ID查询采购合同列表

------

### **11. SalesOrderDAO**

- **功能**: 负责`Sales_Order`表的数据访问。
- **操作**:
  - 新增销售订单
  - 根据ID查询销售订单
  - 查询所有销售订单
  - 更新销售订单信息
  - 删除销售订单
  - 根据客户ID查询销售订单列表

------

### **12. SalesContractDAO**

- **功能**: 负责`Sales_Contract`表的数据访问。
- **操作**:
  - 新增销售合同
  - 根据ID查询销售合同
  - 查询所有销售合同
  - 更新销售合同信息
  - 删除销售合同
  - 根据客户ID查询销售合同列表

------

### **13. InventoryDAO**

- **功能**: 负责`Inventory`表的数据访问。
- **操作**:
  - 新增库存记录
  - 根据产品ID查询库存
  - 查询所有库存记录
  - 更新库存信息
  - 删除库存记录

------

### **14. InventoryAdjustmentDAO**

- **功能**: 负责`Inventory_Adjustment`表的数据访问。
- **操作**:
  - 新增库存调整记录
  - 根据ID查询库存调整记录
  - 查询所有库存调整记录
  - 更新库存调整记录
  - 删除库存调整记录
  - 根据产品ID查询库存调整记录

------

### **15. LogisticsCompanyDAO**

- **功能**: 负责`Logistics_Company`表的数据访问。
- **操作**:
  - 新增物流公司
  - 根据ID查询物流公司
  - 查询所有物流公司
  - 更新物流公司信息
  - 删除物流公司

------

### **16. LogisticsOrderDAO**

- **功能**: 负责`Logistics_Order`表的数据访问。
- **操作**:
  - 新增物流订单
  - 根据ID查询物流订单
  - 查询所有物流订单
  - 更新物流订单信息
  - 删除物流订单
  - 根据采购订单ID或销售订单ID查询物流订单

------

### **17. LogisticsAgreementDAO**

- **功能**: 负责`Logistics_Agreement`表的数据访问。
- **操作**:
  - 新增物流协议
  - 根据ID查询物流协议
  - 查询所有物流协议
  - 更新物流协议信息
  - 删除物流协议
  - 根据物流公司ID查询物流协议列表

------

### **18. MaterialDAO**

- **功能**: 负责`Material`表的数据访问。
- **操作**:
  - 新增原材料
  - 根据ID查询原材料
  - 查询所有原材料
  - 更新原材料信息
  - 删除原材料

------

### **19. ProductMaterialDAO**

- **功能**: 负责`Product_Material`表的数据访问。
- **操作**:
  - 新增产品原材料关系
  - 根据产品ID查询原材料列表
  - 根据原材料ID查询产品列表
  - 更新产品原材料关系
  - 删除产品原材料关系



## 五、功能分析

1. **用户权限管理**
   - 涉及表：`Role`, `User`, `Permission`, `Role_Permission`
   - 功能：角色分配、用户账号管理、权限配置。
2. **供应商管理**
   - 涉及表：`Supplier`
   - 功能：供应商信息维护、联系人管理。
3. **客户管理**
   - 涉及表：`Customer`
   - 功能：客户信息维护、联系人管理。
4. **产品管理**
   - 涉及表：`Product_Category`, `Product`
   - 功能：产品分类管理、产品信息维护。
5. **采购管理**
   - 涉及表：`Purchase_Order`, `Purchase_Contract`
   - 功能：采购订单处理、合同签订与跟踪。
6. **销售管理**
   - 涉及表：`Sales_Order`, `Sales_Contract`
   - 功能：销售订单处理、合同签订与跟踪。
7. **库存管理**
   - 涉及表：`Inventory`, `Inventory_Adjustment`
   - 功能：库存监控、库存调整记录。
8. **物流管理**
   - 涉及表：`Logistics_Company`, `Logistics_Order`, `Logistics_Agreement`
   - 功能：物流公司合作、物流订单跟踪、协议管理。
9. **物料管理**
   - 涉及表：`Material`, `Product_Material`
   - 功能：原材料信息维护、产品与物料关联管理。

**总计：9个大功能模块**





## 六、服务层的内容

### **1. 用户权限管理模块**

#### **Controller名称**

- `UserController`
- `RoleController`
- `PermissionController`

#### **职能与HTTP端点**

- **UserController**
  - **POST `/api/users/register`**
    - 调用方法：`UserService.registerUser()`
    - 职能：接收用户注册请求，校验数据后创建用户账号。
  - **POST `/api/users/login`**
    - 调用方法：`UserService.login()`
    - 职能：处理用户登录请求，返回Token或会话信息。
  - **PUT `/api/users/{userId}/roles`**
    - 调用方法：`UserService.assignRoleToUser()`
    - 职能：为用户分配角色（需权限校验）。
  - **GET `/api/roles/{roleId}/users`**
    - 调用方法：`UserService.listUsersByRole()`
    - 职能：查询某角色下的所有用户列表。
- **RoleController**
  - **POST `/api/roles`**
    - 调用方法：`RoleService.createRole()`
    - 职能：创建新角色（仅限管理员）。
  - **PUT `/api/roles/{roleId}`**
    - 调用方法：`RoleService.updateRole()`
    - 职能：更新角色信息（如名称、描述，仅限管理员）。
  - **DELETE `/api/roles/{roleId}`**
    - 调用方法：`RoleService.deleteRole()`
    - 职能：删除角色（自动解除用户和权限关联，仅限管理员）。
  - **POST `/api/roles/{roleId}/permissions`**
    - 调用方法：`RoleService.bindPermissionToRole()`
    - 职能：为角色绑定权限（仅限管理员）。
  - **DELETE `/api/roles/{roleId}/permissions/{permissionId}`**
    - 调用方法：`RoleService.unbindPermissionFromRole()`
    - 职能：解绑角色与权限的关联（仅限管理员）。
  - **GET `/api/roles/{roleId}/permissions`**
    - 调用方法：`RoleService.getPermissionsByRole()`
    - 职能：查询角色拥有的权限列表。
  - **GET `/api/roles`**
    - 调用方法：`RoleService.listRoles()`
    - 职能：分页查询角色列表（支持分页参数，仅限管理员）。
  - **GET `/api/roles/{roleId}/has-permission`**
    - 调用方法：`RoleService.checkPermissionByRole()`
    - 职能：验证角色是否拥有指定权限（通过`code`参数传递权限编码）。
- **PermissionController**
  - **POST `/api/permissions`**
    - 调用方法：`PermissionService.createPermission()`
    - 职能：新增权限项（需管理员权限）。
  - **GET `/api/permissions/{permissionId}`**
    - 调用方法：`PermissionService.getPermissionById()`
    - 职能：查询权限详情（包括关联角色数）。
  - **PUT `/api/permissions/{permissionId}`**
    - 调用方法：`PermissionService.updatePermission()`
    - 职能：更新权限信息（如名称、编码，需管理员权限）。
  - **DELETE `/api/permissions/{permissionId}`**
    - 调用方法：`PermissionService.deletePermission()`
    - 职能：删除权限（自动解除角色关联，需管理员权限）。
  - **GET `/api/permissions/{permissionId}/roles`**
    - 调用方法：`PermissionService.getRolesByPermission()`
    - 职能：查询拥有该权限的所有角色列表（仅限管理员）。

------

### **2. 供应商与客户管理模块**

#### **Controller名称**

- `SupplierController`
- `CustomerController`

#### **职能与HTTP端点**

- **SupplierController**
  - **POST `/api/suppliers`**
    - 调用方法：`SupplierService.addSupplier()`
    - 职能：新增供应商信息。
  - **PUT `/api/suppliers/{supplierId}`**
    - 调用方法：`SupplierService.updateSupplier()`
    - 职能：更新供应商信息。
  - **GET `/api/suppliers/{supplierId}/orders`**
    - 调用方法：`SupplierService.getSupplierOrders()`
    - 职能：查询供应商的历史采购订单。
- **CustomerController**
  - **POST `/api/customers`**
    - 调用方法：`CustomerService.addCustomer()`
    - 职能：新增客户信息。
  - **PUT `/api/customers/{customerId}/credit`**
    - 调用方法：`CustomerService.updateCustomerCreditLimit()`
    - 职能：更新客户信用额度。
  - **GET `/api/customers/{customerId}/credit-status`**
    - 调用方法：`CustomerService.checkCreditStatus()`
    - 职能：检查客户信用是否允许下单。

------

### **3. 产品管理模块**

#### **Controller名称**

- `ProductCategoryController`
- `ProductController`

#### **职能与HTTP端点**

- **ProductCategoryController**
  - **POST `/api/categories`**
    - 调用方法：`ProductCategoryService.createCategory()`
    - 职能：创建产品分类。
  - **PUT `/api/categories/{categoryId}/parent`**
    - 调用方法：`ProductCategoryService.moveCategory()`
    - 职能：调整分类的父级节点。
  - **GET `/api/categories/tree`**
    - 调用方法：`ProductCategoryService.getCategoryTree()`
    - 职能：获取完整的分类树结构。
- **ProductController**
  - **POST `/api/products`**
    - 调用方法：`ProductService.createProduct()`
    - 职能：创建新产品并关联分类。
  - **PUT `/api/products/{productId}/price`**
    - 调用方法：`ProductService.updateProductPrice()`
    - 职能：调整产品价格。
  - **GET `/api/products/{productId}/materials`**
    - 调用方法：`ProductService.getProductMaterials()`
    - 职能：查询产品的BOM（物料清单）。

------

### **4. 采购管理模块**

#### **Controller名称**

- `PurchaseOrderController`
- `PurchaseContractController`

#### **职能与HTTP端点**

- **PurchaseOrderController**
  - **POST `/api/purchase-orders`**
    - 调用方法：`PurchaseOrderService.createPurchaseOrder()`
    - 职能：创建采购订单，关联供应商和产品。
  - **PUT `/api/purchase-orders/{orderId}/status`**
    - 调用方法：`PurchaseOrderService.updateOrderStatus()`
    - 职能：更新订单状态（如“已发货”）。
  - **GET `/api/purchase-orders/{orderId}/total`**
    - 调用方法：`PurchaseOrderService.calculateTotalPrice()`
    - 职能：计算订单总价（用于前端展示）。
- **PurchaseContractController**
  - **POST `/api/purchase-contracts`**
    - 调用方法：`PurchaseContractService.signContract()`
    - 职能：签订采购合同。
  - **GET `/api/purchase-contracts/{contractId}/validate`**
    - 调用方法：`PurchaseContractService.validateContractForOrder()`
    - 职能：校验订单是否符合合同条款（如有效期）。

------

### **5. 销售管理模块**

#### **Controller名称**

- `SalesOrderController`
- `SalesContractController`

#### **职能与HTTP端点**

- **SalesOrderController**
  - **POST `/api/sales-orders`**
    - 调用方法：`SalesOrderService.createSalesOrder()`
    - 职能：创建销售订单，关联客户和产品。
  - **POST `/api/sales-orders/{orderId}/split`**
    - 调用方法：`SalesOrderService.splitSalesOrder()`
    - 职能：根据库存分布拆分订单。
  - **PUT `/api/sales-orders/{orderId}/delivered`**
    - 调用方法：`SalesOrderService.markOrderAsDelivered()`
    - 职能：标记订单为“已签收”。
- **SalesContractController**
  - **POST `/api/sales-contracts`**
    - 调用方法：`SalesContractService.signContract()`
    - 职能：签订销售合同。
  - **GET `/api/sales-contracts/{contractId}/orders`**
    - 调用方法：`SalesContractService.getOrdersUnderContract()`
    - 职能：查询合同关联的所有销售订单。

------

### **6. 库存管理模块**

#### **Controller名称**

- `InventoryController`
- `InventoryAdjustmentController`

#### **职能与HTTP端点**

- **InventoryController**
  - **GET `/api/inventory/{productId}`**
    - 调用方法：`InventoryService.getInventoryByProduct()`
    - 职能：查询指定产品的实时库存。
  - **PUT `/api/inventory/{productId}/stock`**
    - 调用方法：`InventoryService.updateStock()`
    - 职能：手动调整库存数量（需权限）。
- **InventoryAdjustmentController**
  - **POST `/api/inventory-adjustments`**
    - 调用方法：`InventoryAdjustmentService.recordAdjustment()`
    - 职能：记录库存调整原因（如报损）。
  - **GET `/api/inventory-adjustments/{productId}`**
    - 调用方法：`InventoryAdjustmentService.getAdjustmentHistory()`
    - 职能：查询产品的库存调整历史记录。

------

### **7. 物流管理模块**

#### **Controller名称**

- `LogisticsCompanyController`
- `LogisticsOrderController`
- `LogisticsAgreementController`

#### **职能与HTTP端点**

- **LogisticsCompanyController**
  - **POST `/api/logistics-companies`**
    - 调用方法：`LogisticsCompanyService.addCompany()`
    - 职能：新增合作物流公司。
- **LogisticsOrderController**
  - **POST `/api/logistics-orders`**
    - 调用方法：`LogisticsOrderService.createLogisticsOrder()`
    - 职能：创建物流订单，关联采购或销售订单。
  - **PUT `/api/logistics-orders/{orderId}/status`**
    - 调用方法：`LogisticsOrderService.updateShippingStatus()`
    - 职能：更新物流状态（如“运输中”）。
- **LogisticsAgreementController**
  - **POST `/api/logistics-agreements`**
    - 调用方法：`LogisticsAgreementService.signAgreement()`
    - 职能：签订物流合作协议。

------

### **8. 物料管理模块**

#### **Controller名称**

- `MaterialController`
- `ProductMaterialController`

#### **职能与HTTP端点**

- **MaterialController**
  - **POST `/api/materials`**
    - 调用方法：`MaterialService.addMaterial()`
    - 职能：新增原材料信息。
- **ProductMaterialController**
  - **POST `/api/products/{productId}/materials`**
    - 调用方法：`ProductMaterialService.bindMaterialToProduct()`
    - 职能：绑定产品与原材料的关系。

------

### **9. 数据分析与报表模块**

#### **Controller名称**

- `ReportController`
- `AnalyticsController`

#### **职能与HTTP端点**

- **ReportController**
  - **GET `/api/reports/sales/monthly`**
    - 调用方法：`ReportService.generateMonthlySalesReport()`
    - 职能：生成并下载月度销售报表（PDF/Excel）。
- **AnalyticsController**
  - **GET `/api/analytics/suppliers/{supplierId}/performance`**
    - 调用方法：`AnalyticsService.calculateSupplierPerformance()`
    - 职能：获取供应商履约率等分析数据。

------

### **10. 系统集成模块**

#### **Controller名称**

- `IntegrationController`
- `PaymentController`

#### **职能与HTTP端点**

- **IntegrationController**
  - **POST `/api/integration/erp-sync`**
    - 调用方法：`IntegrationService.syncDataToERP()`
    - 职能：手动触发ERP数据同步。
- **PaymentController**
  - **POST `/api/payments/process`**
    - 调用方法：`PaymentService.processPayment()`
    - 职能：处理用户支付请求（调用第三方支付接口）。
