package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDAO extends JpaRepository<Inventory, Integer> {
    // 自定义方法：根据产品ID查询库存
    Inventory findByProductId(int productId);
    void deleteByProductId(int productId);
}
