package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDAO extends JpaRepository<PurchaseOrder, Integer> {
    // 自定义方法：根据供应商ID查询采购订单列表
    List<PurchaseOrder> findBySupplier_SupplierId(int supplierId);
}
