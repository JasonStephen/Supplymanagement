package com.jason.supplymanagement.service.Product;

import com.jason.supplymanagement.dto.ProductDetailsDTO;
import com.jason.supplymanagement.entity.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByName(String name);
    Product getProductById(int id);
    Product createProduct(Product product);
    Product updateProduct(int id, Product product);
    void deleteProduct(int id);
    Page<Product> getProducts(String search, Integer category, Pageable pageable);

    ResponseEntity<ProductDetailsDTO> getProductDetails(int productId);

    boolean bindCategoryToProduct(int productId, int categoryId);
}
