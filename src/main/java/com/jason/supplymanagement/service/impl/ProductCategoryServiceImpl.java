package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.ProductCategoryDAO;
import com.jason.supplymanagement.entity.ProductCategory;
import com.jason.supplymanagement.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDAO productCategoryDAO;

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryDAO.findAll();
    }

    @Override
    public ProductCategory getProductCategoryById(int id) {
        return productCategoryDAO.findById(id).orElse(null);
    }

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        return productCategoryDAO.save(productCategory);
    }

    @Override
    public ProductCategory updateProductCategory(int id, ProductCategory productCategory) {
        if (productCategoryDAO.existsById(id)) {
            productCategory.setCategoryId(id);
            return productCategoryDAO.save(productCategory);
        }
        return null;
    }

    @Override
    public void deleteProductCategory(int id) {
        productCategoryDAO.deleteById(id);
    }
}