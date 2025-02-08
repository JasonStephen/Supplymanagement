package com.jason.supplymanagement.controller.Product;

import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId; // Add this import
import com.jason.supplymanagement.service.Product.InventoryAdjustmentService;
import com.jason.supplymanagement.service.Product.InventoryService;
import com.jason.supplymanagement.service.Product.ProductService;
import com.jason.supplymanagement.service.Product.ProductComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
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

    @GetMapping("/products")
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