package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.ProductMaterial;

import java.util.List;

public interface ProductMaterialService {
    List<ProductMaterial> getAllProductMaterials();
    List<ProductMaterial> getProductMaterialsByProductId(int productId);
    List<ProductMaterial> getProductMaterialsByMaterialId(int materialId);
    ProductMaterial createProductMaterial(ProductMaterial productMaterial);
    ProductMaterial updateProductMaterial(ProductMaterial productMaterial);
    void deleteProductMaterial(ProductMaterial productMaterial);
}