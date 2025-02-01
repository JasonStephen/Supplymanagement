package com.jason.supplymanagement.controller;

import com.jason.supplymanagement.entity.ProductMaterial;
import com.jason.supplymanagement.entity.ProductMaterialId;
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

    @GetMapping("/product/{productId}/material/{materialId}")
    public ProductMaterial getProductMaterial(@PathVariable int productId, @PathVariable int materialId) {
        ProductMaterialId id = new ProductMaterialId(productId, materialId);
        return productMaterialService.getProductMaterialById(id);
    }

    @PostMapping
    public ProductMaterial createProductMaterial(@RequestBody ProductMaterial productMaterial) {
        return productMaterialService.createProductMaterial(productMaterial);
    }

    @PutMapping("/product/{productId}/material/{materialId}")
    public ProductMaterial updateProductMaterial(@PathVariable int productId, @PathVariable int materialId, @RequestBody ProductMaterial productMaterial) {
        if (productMaterial.getProduct().getProductId() != productId || productMaterial.getMaterial().getMaterialId() != materialId) {
            throw new IllegalArgumentException("Product ID or Material ID does not match the request path");
        }
        return productMaterialService.updateProductMaterial(productMaterial);
    }

    @DeleteMapping("/product/{productId}/material/{materialId}")
    public void deleteProductMaterial(@PathVariable int productId, @PathVariable int materialId) {
        ProductMaterialId id = new ProductMaterialId(productId, materialId);
        productMaterialService.deleteProductMaterialById(id);
    }
}