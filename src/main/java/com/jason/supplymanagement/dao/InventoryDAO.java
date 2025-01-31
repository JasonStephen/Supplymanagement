package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDAO extends JpaRepository<Inventory, Integer> {
    // 自定义方法：根据产品ID查询库存
    Inventory findByProductId(int productId);
}
