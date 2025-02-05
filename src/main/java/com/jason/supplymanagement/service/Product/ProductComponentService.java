package com.jason.supplymanagement.service.Product;

import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId;

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