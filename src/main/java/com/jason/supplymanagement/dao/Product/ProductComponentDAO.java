package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.ProductComponent;
import com.jason.supplymanagement.entity.Product.ProductComponentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductComponentDAO extends JpaRepository<ProductComponent, ProductComponentId> {
    List<ProductComponent> findByProduct_ProductId(int productId);
    List<ProductComponent> findByComponent_ProductId(int componentId);
}