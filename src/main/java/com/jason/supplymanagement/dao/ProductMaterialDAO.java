package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMaterialDAO extends JpaRepository<ProductMaterial, Integer> {
    // 修改方法名，使用 product_ProductId 作为路径
    List<ProductMaterial> findByProduct_ProductId(int productId);

    // 修改方法名，使用 material_MaterialId 作为路径
    List<ProductMaterial> findByMaterial_MaterialId(int materialId);
}
