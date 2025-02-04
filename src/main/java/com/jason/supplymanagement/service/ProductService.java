package com.jason.supplymanagement.service;

import com.jason.supplymanagement.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByName(String name);
    Product getProductById(int id);
    Product createProduct(Product product);
    Product updateProduct(int id, Product product);
    void deleteProduct(int id);
}