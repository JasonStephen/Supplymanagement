package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.ProductMaterial;
import com.jason.supplymanagement.service.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-materials")
public class ProductMaterialController {

    @Autowired
    private ProductMaterialService productMaterialService;

    @GetMapping
    public List<ProductMaterial> getAllProductMaterials() {
        return productMaterialService.getAllProductMaterials();
    }

    @GetMapping("/product/{productId}")
    public List<ProductMaterial> getProductMaterialsByProductId(@PathVariable int productId) {
        return productMaterialService.getProductMaterialsByProductId(productId);
    }

    @GetMapping("/material/{materialId}")
    public List<ProductMaterial> getProductMaterialsByMaterialId(@PathVariable int materialId) {
        return productMaterialService.getProductMaterialsByMaterialId(materialId);
    }

    @PostMapping
    public ProductMaterial createProductMaterial(@RequestBody ProductMaterial productMaterial) {
        return productMaterialService.createProductMaterial(productMaterial);
    }

    @PutMapping
    public ProductMaterial updateProductMaterial(@RequestBody ProductMaterial productMaterial) {
        return productMaterialService.updateProductMaterial(productMaterial);
    }

    @DeleteMapping
    public void deleteProductMaterial(@RequestBody ProductMaterial productMaterial) {
        productMaterialService.deleteProductMaterial(productMaterial);
    }
}