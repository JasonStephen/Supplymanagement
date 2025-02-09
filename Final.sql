-- 创建数据库并切换到该数据库
CREATE DATABASE IF NOT EXISTS SupplyChainManagement;
USE SupplyChainManagement;

-- 1. 角色表 (Role) - 无外键依赖，最先创建
CREATE TABLE IF NOT EXISTS Role (
                                    role_id INT AUTO_INCREMENT PRIMARY KEY,
                                    role_name VARCHAR(50) NOT NULL UNIQUE
    );

-- 2. 用户表 (User) - 依赖 Role 表
CREATE TABLE IF NOT EXISTS User (
                                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id INT,
    avatar VARCHAR(255),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
    );

-- 3. 权限表 (Permission) - 无外键依赖
CREATE TABLE IF NOT EXISTS Permission (
                                          permission_id INT AUTO_INCREMENT PRIMARY KEY,
                                          permission_name VARCHAR(50) NOT NULL UNIQUE,
    permission_code VARCHAR(50) NOT NULL UNIQUE
    );

-- 4. 角色权限表 (Role_Permission) - 依赖 Role 和 Permission 表
CREATE TABLE IF NOT EXISTS Role_Permission (
                                               role_id INT,
                                               permission_id INT,
                                               PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id),
    FOREIGN KEY (permission_id) REFERENCES Permission(permission_id)
    );

-- 5. 供应商表 (Supplier) - 无外键依赖
CREATE TABLE IF NOT EXISTS Supplier (
                                        supplier_id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL
    );

-- 6. 客户表 (Customer) - 无外键依赖
CREATE TABLE IF NOT EXISTS Customer (
                                        customer_id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL
    );

-- 7. 产品类别表 (Product_Category) - 自引用外键
CREATE TABLE IF NOT EXISTS Product_Category (
                                                category_id INT AUTO_INCREMENT PRIMARY KEY,
                                                category_name VARCHAR(100) NOT NULL,
    parent_category_id INT,
    FOREIGN KEY (parent_category_id) REFERENCES Product_Category(category_id)
    );

-- 8. 产品表 (Product) - 依赖 Product_Category 表
--     同时将 unit 字段直接添加到创建语句中
CREATE TABLE IF NOT EXISTS Product (
                                       product_id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category_id INT,
    price DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Product_Category(category_id)
    );

-- 9. 采购订单表 (Purchase_Order) - 依赖 Supplier、Product 及 Purchase_Contract 表
--    此处将 contract_id 字段添加到创建语句中
CREATE TABLE IF NOT EXISTS Purchase_Order (
                                              purchase_order_id INT AUTO_INCREMENT PRIMARY KEY,
                                              supplier_id INT,
                                              product_id INT,
                                              quantity INT NOT NULL,
                                              unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    contract_id INT,
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (contract_id) REFERENCES Purchase_Contract(contract_id)
    );

-- 10. 采购合同表 (Purchase_Contract) - 依赖 Supplier 表
CREATE TABLE IF NOT EXISTS Purchase_Contract (
                                                 contract_id INT AUTO_INCREMENT PRIMARY KEY,
                                                 supplier_id INT,
                                                 contract_content TEXT NOT NULL,
                                                 signing_date DATETIME,
                                                 expiry_date DATETIME,
                                                 FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
    );

-- 11. 销售订单表 (Sales_Order) - 依赖 Customer、Product 及 Sales_Contract 表
--     将 contract_id 字段直接添加到创建语句中
CREATE TABLE IF NOT EXISTS Sales_Order (
                                           sales_order_id INT AUTO_INCREMENT PRIMARY KEY,
                                           customer_id INT,
                                           product_id INT,
                                           quantity INT NOT NULL,
                                           unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    contract_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (contract_id) REFERENCES Sales_Contract(contract_id)
    );

-- 12. 销售合同表 (Sales_Contract) - 依赖 Customer 表
CREATE TABLE IF NOT EXISTS Sales_Contract (
                                              contract_id INT AUTO_INCREMENT PRIMARY KEY,
                                              customer_id INT,
                                              contract_content TEXT NOT NULL,
                                              signing_date DATETIME,
                                              expiry_date DATETIME,
                                              FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
    );

-- 13. 库存表 (Inventory) - 依赖 Product 表
CREATE TABLE IF NOT EXISTS Inventory (
                                         product_id INT PRIMARY KEY,
                                         quantity INT NOT NULL,
                                         alert_threshold INT NOT NULL,
                                         FOREIGN KEY (product_id) REFERENCES Product(product_id)
    );

-- 14. 库存调整表 (Inventory_Adjustment) - 依赖 Product 和 User 表
CREATE TABLE IF NOT EXISTS Inventory_Adjustment (
                                                    adjustment_id INT AUTO_INCREMENT PRIMARY KEY,
                                                    product_id INT,
                                                    quantity INT NOT NULL,
                                                    reason TEXT NOT NULL,
                                                    user_id INT,
                                                    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
    );

-- 15. 物流公司表 (Logistics_Company) - 无外键依赖
CREATE TABLE IF NOT EXISTS Logistics_Company (
                                                 logistics_company_id INT AUTO_INCREMENT PRIMARY KEY,
                                                 name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL
    );

-- 16. 物流协议表 (Logistics_Agreement) - 依赖 Logistics_Company 表
CREATE TABLE IF NOT EXISTS Logistics_Agreement (
                                                   agreement_id INT AUTO_INCREMENT PRIMARY KEY,
                                                   logistics_company_id INT,
                                                   agreement_content TEXT NOT NULL,
                                                   signing_date DATETIME,
                                                   expiry_date DATETIME,
                                                   FOREIGN KEY (logistics_company_id) REFERENCES Logistics_Company(logistics_company_id)
    );

-- 17. 物流订单表 (Logistics_Order) - 依赖 Purchase_Order、Sales_Order、Logistics_Company 及 Logistics_Agreement 表
--     此处不再创建 shipping_date、estimated_arrival_date、actual_arrival_date 字段，
--     而是直接包含 status 和 agreement_id 字段。
CREATE TABLE IF NOT EXISTS Logistics_Order (
                                               logistics_order_id INT AUTO_INCREMENT PRIMARY KEY,
                                               purchase_order_id INT,
                                               sales_order_id INT,
                                               logistics_company_id INT,
                                               status VARCHAR(20) NOT NULL,
    agreement_id INT,
    FOREIGN KEY (purchase_order_id) REFERENCES Purchase_Order(purchase_order_id),
    FOREIGN KEY (sales_order_id) REFERENCES Sales_Order(sales_order_id),
    FOREIGN KEY (logistics_company_id) REFERENCES Logistics_Company(logistics_company_id),
    FOREIGN KEY (agreement_id) REFERENCES Logistics_Agreement(agreement_id)
    );

-- 18. 原材料表 (Material) - 无外键依赖
CREATE TABLE IF NOT EXISTS Material (
                                        material_id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    unit VARCHAR(20) NOT NULL
    );

-- 19. 产品原材料表 (Product_Material) - 依赖 Product 和 Material 表
CREATE TABLE IF NOT EXISTS Product_Material (
                                                product_id INT,
                                                material_id INT,
                                                quantity INT NOT NULL,
                                                unit VARCHAR(20) NOT NULL,
    PRIMARY KEY (product_id, material_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (material_id) REFERENCES Material(material_id)
    );

-- 20. 产品组件表 (Product_Component) - 自关联，依赖 Product 表
CREATE TABLE IF NOT EXISTS Product_Component (
                                                 product_id INT,
                                                 component_id INT,
                                                 quantity INT NOT NULL,
                                                 unit VARCHAR(20) NOT NULL,
    PRIMARY KEY (product_id, component_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (component_id) REFERENCES Product(product_id)
    );