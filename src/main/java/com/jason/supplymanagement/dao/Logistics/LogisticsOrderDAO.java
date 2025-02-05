package com.jason.supplymanagement.dao.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsOrderDAO extends JpaRepository<LogisticsOrder, Integer> {
    List<LogisticsOrder> findByPurchaseOrderId(int purchaseOrderId);
    List<LogisticsOrder> findBySalesOrderId(int salesOrderId);
}