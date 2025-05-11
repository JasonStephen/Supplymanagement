package com.jason.supplymanagement.controller.Product;

import com.jason.supplymanagement.dao.Users.UserDAO;
import com.jason.supplymanagement.dto.ProductDTO;
import com.jason.supplymanagement.dto.ProductDetailsDTO;
import com.jason.supplymanagement.entity.Product.*;
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Product.ProductComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductComponentService productComponentService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryAdjustmentService inventoryAdjustmentService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

//    @PostMapping
//    public Product createProduct(@RequestBody Product product) {
//        return productService.createProduct(product);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int categoryId,
            @RequestParam BigDecimal price,
            @RequestParam String unit,
            @RequestParam(required = false) MultipartFile photo) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(new ProductCategory(categoryId)); // 使用带参数的构造函数
        product.setPrice(price);
        product.setUnit(unit);

        if (photo != null && !photo.isEmpty()) {
            try {
                String uploadDir = "E:/Project/SupplyManagement/uploads/products/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = "product_" + System.currentTimeMillis() + "." + photo.getOriginalFilename().split("\\.")[1];
                Path path = Paths.get(uploadDir + fileName);
                Files.write(path, photo.getBytes());

                product.setPhoto("/products/uploads/products/" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

//    @PutMapping("/{id}")
//    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
//        return productService.updateProduct(id, product);
//    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam BigDecimal price,
            @RequestParam String unit,
            @RequestParam(required = false) MultipartFile photo) {

        // 获取现有产品
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // 创建更新后的产品对象
        Product updatedProduct = new Product();
        updatedProduct.setProductId(id);
        updatedProduct.setName(name);
        updatedProduct.setDescription(description);
        updatedProduct.setPrice(price); // 使用前端传来的新价格
        updatedProduct.setUnit(unit);

        // 处理类别
        if (categoryId != null) {
            updatedProduct.setCategory(new ProductCategory(categoryId));
        } else {
            updatedProduct.setCategory(null);
        }

        // 处理图片上传
        if (photo != null && !photo.isEmpty()) {
            try {
                String uploadDir = "E:/Project/SupplyManagement/uploads/products/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = "product_" + id + "." + photo.getOriginalFilename().split("\\.")[1];
                Path path = Paths.get(uploadDir + fileName);
                Files.write(path, photo.getBytes());

                updatedProduct.setPhoto("/products/uploads/products/" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            // 保留原有图片
            updatedProduct.setPhoto(existingProduct.getPhoto());
        }

        // 调用服务层更新产品
        Product result = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{productId}/components")
    public List<ProductComponent> getProductComponents(@PathVariable int productId) {
        return productComponentService.getProductComponentsByProductId(productId);
    }

    @PostMapping("/{productId}/components")
    public ProductComponent addProductComponent(@PathVariable int productId, @RequestBody ProductComponent productComponent) {
        productComponent.setProduct(productService.getProductById(productId));
        return productComponentService.createProductComponent(productComponent);
    }

    @PutMapping("/{productId}/components/{componentId}")
    public ProductComponent updateProductComponent(@PathVariable int productId, @PathVariable int componentId, @RequestBody ProductComponent productComponent) {
        productComponent.setProduct(productService.getProductById(productId));
        productComponent.setComponent(productService.getProductById(componentId));
        return productComponentService.updateProductComponent(productComponent);
    }

    @DeleteMapping("/{productId}/components/{componentId}")
    public void deleteProductComponent(@PathVariable int productId, @PathVariable int componentId) {
        ProductComponentId id = new ProductComponentId(productId, componentId);
        productComponentService.deleteProductComponentById(id);
    }

    @GetMapping("/search")
    public List<Product> getProductsByName(@RequestParam(required = false) String name) {
        if (name != null) {
            return productService.getProductsByName(name);
        }
        return productService.getAllProducts();
    }

    @Transactional
    @PostMapping("/{productId}/produce")
    public ResponseEntity<?> produceProduct(@PathVariable int productId, @RequestBody ProductionRequest request) {
        try {
            // 检查用户 ID 是否存在
            if (!userDAO.existsById(request.getUserId())) {
                return ResponseEntity.badRequest().body("{\"message\": \"用户 ID 不存在\"}");
            }

            // 检查材料库存是否充足
            List<ProductComponent> components = productComponentService.getProductComponentsByProductId(productId);
            for (ProductComponent component : components) {
                int requiredQuantity = component.getQuantity() * request.getQuantity();
                Inventory componentInventory = inventoryService.getInventoryByProductId(component.getComponent().getProductId());
                if (componentInventory == null || componentInventory.getQuantity() < requiredQuantity) {
                    return ResponseEntity.badRequest().body("{\"message\": \"材料库存不足，无法生产\"}");
                }
            }

            // 减少材料库存
            for (ProductComponent component : components) {
                int requiredQuantity = component.getQuantity() * request.getQuantity();
                inventoryService.adjustInventory(component.getComponent().getProductId(), -requiredQuantity);

                InventoryAdjustment consumptionAdjustment = new InventoryAdjustment();
                consumptionAdjustment.setProductId(component.getComponent().getProductId());
                consumptionAdjustment.setQuantity(-requiredQuantity);
                consumptionAdjustment.setReason("生产消耗");
                consumptionAdjustment.setUserId(request.getUserId());
                inventoryAdjustmentService.createAdjustment(consumptionAdjustment);
            }

            // 增加产品库存
            inventoryService.adjustInventory(productId, request.getQuantity());

            InventoryAdjustment productionAdjustment = new InventoryAdjustment();
            productionAdjustment.setProductId(productId);
            productionAdjustment.setQuantity(request.getQuantity());
            productionAdjustment.setReason("生产产出");
            productionAdjustment.setUserId(request.getUserId());
            inventoryAdjustmentService.createAdjustment(productionAdjustment);

            return ResponseEntity.ok("{\"message\": \"生产成功\"}"); // 返回 JSON 格式
        } catch (Exception e) {
            // 如果发生异常，事务会回滚
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"生产失败：" + e.getMessage() + "\"}");
        }
    }



    @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer category,
            @RequestParam(defaultValue = "productId_desc") String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSortDirection(sort));
        Page<Product> products = productService.getProducts(search, category, pageable);

        Page<ProductDTO> productDTOs = products.map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(product.getProductId());
            dto.setName(product.getName());
            dto.setCategoryName(product.getCategory().getCategoryName());
            dto.setPrice(product.getPrice());
            dto.setPhoto(product.getPhoto());
            return dto;
        });

        return ResponseEntity.ok(productDTOs);
    }



    @PostMapping("/uploadPhoto")
    public ResponseEntity<Void> uploadPhoto(@RequestParam("photo") MultipartFile file, @RequestParam int productId) {
        try {
            // 保存文件到绝对路径
            String uploadDir = "E:/Project/SupplyManagement/uploads/products/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "product_" + productId + "." + file.getOriginalFilename().split("\\.")[1];
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            // 更新产品图片路径
            Product product = productService.getProductById(productId);
            product.setPhoto("/api/v1/product/uploads/products/" + fileName);
            productService.updateProduct(productId, product);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 访问产品图片
    @GetMapping("/uploads/products/{fileName}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String fileName) {
        try {
            String uploadDir = "E:/Project/SupplyManagement/uploads/products/";
            Path path = Paths.get(uploadDir + fileName);
            byte[] imageBytes = Files.readAllBytes(path);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Sort getSortDirection(String sort) {
        String[] parts = sort.split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("无效的排序字段: " + sort);
        }

        String field = parts[0]; // 字段名
        String direction = parts[1].toLowerCase(); // 排序方向

        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new IllegalArgumentException("无效的排序方向: " + direction);
        }

        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(sortDirection, field);
    }

    @GetMapping("/details/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductDetails(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // 获取库存信息
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        if (inventory == null) {
            inventory = new Inventory(); // 如果库存不存在，返回默认值
        }

        // 构造 ProductDetailsDTO
        ProductDetailsDTO details = new ProductDetailsDTO();
        details.setProductId(product.getProductId());
        details.setName(product.getName());
        details.setCategory(product.getCategory().getCategoryName());
        details.setPrice(product.getPrice());
        details.setUnit(product.getUnit());
        details.setDescription(product.getDescription());
        details.setPhoto(product.getPhoto());
        details.setQuantity(inventory.getQuantity());
        details.setAlertThreshold(inventory.getAlertThreshold());
        details.setInventoryStatus(inventory.getQuantity() < inventory.getAlertThreshold() ? "告警" : "正常");

        return ResponseEntity.ok(details);
    }

    @PostMapping("/{productId}/bind-category/{categoryId}")
    public ResponseEntity<Void> bindCategoryToProduct(
            @PathVariable int productId,
            @PathVariable int categoryId) {
        boolean success = productService.bindCategoryToProduct(productId, categoryId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/recent-price-changes")
    public ResponseEntity<List<PriceChange>> getRecentPriceChanges() {
        List<PriceChange> recentChanges = productService.getRecentPriceChanges(3);
        return ResponseEntity.ok(recentChanges);
    }

    public static class ProductionRequest {
        private int quantity;
        private int userId;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}