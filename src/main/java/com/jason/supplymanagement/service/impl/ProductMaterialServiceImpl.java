package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.ProductMaterialDAO;
import com.jason.supplymanagement.entity.ProductMaterial;
import com.jason.supplymanagement.service.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMaterialServiceImpl implements ProductMaterialService {

    @Autowired
    private ProductMaterialDAO productMaterialDAO;

    @Override
    public List<ProductMaterial> getAllProductMaterials() {
        return productMaterialDAO.findAll();
    }

    @Override
    public List<ProductMaterial> getProductMaterialsByProductId(int productId) {
        return productMaterialDAO.findByProduct_ProductId(productId);
    }

    @Override
    public List<ProductMaterial> getProductMaterialsByMaterialId(int materialId) {
        return productMaterialDAO.findByMaterial_MaterialId(materialId);
    }

    @Override
    public ProductMaterial createProductMaterial(ProductMaterial productMaterial) {
        return productMaterialDAO.save(productMaterial);
    }

    @Override
    public ProductMaterial updateProductMaterial(ProductMaterial productMaterial) {
        if (productMaterialDAO.existsById(productMaterial.getProduct().getProductId())) {
            return productMaterialDAO.save(productMaterial);
        }
        return null;
    }

    @Override
    public void deleteProductMaterial(ProductMaterial productMaterial) {
        productMaterialDAO.delete(productMaterial);
    }
}