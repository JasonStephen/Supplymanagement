package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.ProductMaterial;
import com.jason.supplymanagement.entity.ProductMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMaterialDAO extends JpaRepository<ProductMaterial, ProductMaterialId> {
    List<ProductMaterial> findByProduct_ProductId(int productId);
    List<ProductMaterial> findByMaterial_MaterialId(int materialId);
}