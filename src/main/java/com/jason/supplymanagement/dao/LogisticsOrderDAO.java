package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.LogisticsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsOrderDAO extends JpaRepository<LogisticsOrder, Integer> {
    // 自定义方法：根据采购订单ID查询物流订单
    List<LogisticsOrder> findByPurchaseOrderId(int purchaseOrderId);

    // 自定义方法：根据销售订单ID查询物流订单
    List<LogisticsOrder> findBySalesOrderId(int salesOrderId);
}
