package com.jason.supplymanagement.controller.Product;

import com.jason.supplymanagement.entity.Product.Product;
import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId; // Add this import
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
}