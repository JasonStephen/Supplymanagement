package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    // 根据类别 ID 查询产品列表
    List<Product> findByCategory_CategoryId(int categoryId);

    // 根据名称模糊查询产品列表
    List<Product> findByNameContainingIgnoreCase(String name);

    // 根据名称模糊查询并分页
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 根据类别 ID 查询并分页
    Page<Product> findByCategory_CategoryId(int categoryId, Pageable pageable);

    // 根据名称模糊查询和类别 ID 查询并分页
    Page<Product> findByNameContainingIgnoreCaseAndCategory_CategoryId(String name, int categoryId, Pageable pageable);

    // 查询所有产品并分页
    Page<Product> findAll(Pageable pageable);

    // 新增方法：模糊查询（支持去除空格和标点符号）
    @Query("SELECT p FROM Product p WHERE LOWER(REPLACE(p.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(?1, ' ', ''), '%'))")
    List<Product> findByFuzzyName(String name);

    // 新增方法：模糊查询并分页（支持去除空格和标点符号）
    @Query("SELECT p FROM Product p WHERE LOWER(REPLACE(p.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(?1, ' ', ''), '%'))")
    Page<Product> findByFuzzyName(String name, Pageable pageable);

    // 新增方法：模糊查询 + 类别 ID 查询（支持去除空格和标点符号）
    @Query("SELECT p FROM Product p WHERE LOWER(REPLACE(p.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(?1, ' ', ''), '%')) AND p.category.categoryId = ?2")
    Page<Product> findByFuzzyNameAndCategoryId(String name, int categoryId, Pageable pageable);
}
