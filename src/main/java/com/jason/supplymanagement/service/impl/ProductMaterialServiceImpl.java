package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.ProductMaterialDAO;
import com.jason.supplymanagement.entity.ProductMaterial;
import com.jason.supplymanagement.entity.ProductMaterialId;
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
        if (productMaterial.getMaterial() == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        return productMaterialDAO.save(productMaterial);
    }

    @Override
    public ProductMaterial updateProductMaterial(ProductMaterial productMaterial) {
        if (productMaterial.getMaterial() == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        ProductMaterialId id = new ProductMaterialId(productMaterial.getProduct().getProductId(), productMaterial.getMaterial().getMaterialId());
        if (productMaterialDAO.existsById(id)) {
            return productMaterialDAO.save(productMaterial);
        }
        return null;
    }

    @Override
    public void deleteProductMaterial(ProductMaterial productMaterial) {
        productMaterialDAO.delete(productMaterial);
    }

    @Override
    public ProductMaterial getProductMaterialById(ProductMaterialId id) {
        return productMaterialDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteProductMaterialById(ProductMaterialId id) {
        productMaterialDAO.deleteById(id);
    }
}