package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    // 修改方法名，使用 category_CategoryId 作为路径
    List<Product> findByCategory_CategoryId(int categoryId);
}