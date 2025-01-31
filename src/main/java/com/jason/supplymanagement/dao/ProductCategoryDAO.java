package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryDAO extends JpaRepository<ProductCategory, Integer> {

    // 修改方法名
    List<ProductCategory> findByParentCategory_CategoryId(int parentCategoryId);
}