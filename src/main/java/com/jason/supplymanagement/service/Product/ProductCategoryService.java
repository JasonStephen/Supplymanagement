package com.jason.supplymanagement.service.Product;

import com.jason.supplymanagement.entity.Product.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getAllProductCategories();
    ProductCategory getProductCategoryById(int id);
    ProductCategory createProductCategory(ProductCategory productCategory);
    ProductCategory updateProductCategory(int id, ProductCategory productCategory);
    void deleteProductCategory(int id);
    List<ProductCategory> getCategoriesByParentId(Integer parentId);
}