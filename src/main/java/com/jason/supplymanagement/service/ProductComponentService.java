package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.ProductComponent;
import com.jason.supplymanagement.entity.ProductComponentId;

import java.util.List;

public interface ProductComponentService {
    List<ProductComponent> getAllProductComponents();
    List<ProductComponent> getProductComponentsByProductId(int productId);
    List<ProductComponent> getProductComponentsByComponentId(int componentId);
    ProductComponent createProductComponent(ProductComponent productComponent);
    ProductComponent updateProductComponent(ProductComponent productComponent);
    void deleteProductComponent(ProductComponent productComponent);
    ProductComponent getProductComponentById(ProductComponentId id);
    void deleteProductComponentById(ProductComponentId id);
}