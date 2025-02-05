package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    List<Product> findByCategory_CategoryId(int categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
}