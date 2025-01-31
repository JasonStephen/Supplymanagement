package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.ProductCategory;
import com.jason.supplymanagement.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @GetMapping("/{id}")
    public ProductCategory getProductCategoryById(@PathVariable int id) {
        return productCategoryService.getProductCategoryById(id);
    }

    @PostMapping
    public ProductCategory createProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.createProductCategory(productCategory);
    }

    @PutMapping("/{id}")
    public ProductCategory updateProductCategory(@PathVariable int id, @RequestBody ProductCategory productCategory) {
        return productCategoryService.updateProductCategory(id, productCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteProductCategory(@PathVariable int id) {
        productCategoryService.deleteProductCategory(id);
    }
}