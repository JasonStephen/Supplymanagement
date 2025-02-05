package com.jason.supplymanagement.dao.Product;

import com.jason.supplymanagement.entity.Product.InventoryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAdjustmentDAO extends JpaRepository<InventoryAdjustment, Integer> {
    // 自定义方法：根据产品ID查询库存调整记录
    List<InventoryAdjustment> findByProductId(int productId);
}
