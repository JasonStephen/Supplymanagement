package com.jason.supplymanagement.controller.Product;

import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId;
import com.jason.supplymanagement.service.Product.ProductComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-components")
public class ProductComponentController {

    @Autowired
    private ProductComponentService productComponentService;

    @GetMapping
    public List<ProductComponent> getAllProductComponents() {
        return productComponentService.getAllProductComponents();
    }

    @GetMapping("/product/{productId}")
    public List<ProductComponent> getProductComponentsByProductId(@PathVariable int productId) {
        return productComponentService.getProductComponentsByProductId(productId);
    }

    @GetMapping("/component/{componentId}")
    public List<ProductComponent> getProductComponentsByComponentId(@PathVariable int componentId) {
        return productComponentService.getProductComponentsByComponentId(componentId);
    }

    @GetMapping("/product/{productId}/component/{componentId}")
    public ProductComponent getProductComponent(@PathVariable int productId, @PathVariable int componentId) {
        ProductComponentId id = new ProductComponentId(productId, componentId);
        return productComponentService.getProductComponentById(id);
    }

    @PostMapping
    public ProductComponent createProductComponent(@RequestBody ProductComponent productComponent) {
        return productComponentService.createProductComponent(productComponent);
    }

    @PutMapping("/product/{productId}/component/{componentId}")
    public ProductComponent updateProductComponent(@PathVariable int productId, @PathVariable int componentId, @RequestBody ProductComponent productComponent) {
        if (productComponent.getProduct().getProductId() != productId || productComponent.getComponent().getProductId() != componentId) {
            throw new IllegalArgumentException("Product ID or Component ID does not match the request path");
        }
        return productComponentService.updateProductComponent(productComponent);
    }

    @DeleteMapping("/product/{productId}/component/{componentId}")
    public void deleteProductComponent(@PathVariable int productId, @PathVariable int componentId) {
        ProductComponentId id = new ProductComponentId(productId, componentId);
        productComponentService.deleteProductComponentById(id);
    }
}