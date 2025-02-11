package com.jason.supplymanagement.controller.Product;

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

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
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

    @PostMapping("/{productId}/produce")
    public void produceProduct(@PathVariable int productId, @RequestBody ProductionRequest request) {
        List<ProductComponent> components = productComponentService.getProductComponentsByProductId(productId);
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
        inventoryService.adjustInventory(productId, request.getQuantity());

        InventoryAdjustment productionAdjustment = new InventoryAdjustment();
        productionAdjustment.setProductId(productId);
        productionAdjustment.setQuantity(request.getQuantity());
        productionAdjustment.setReason("生产产出");
        productionAdjustment.setUserId(request.getUserId());
        inventoryAdjustmentService.createAdjustment(productionAdjustment);
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