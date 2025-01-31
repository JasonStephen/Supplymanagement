package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDAO extends JpaRepository<SalesOrder, Integer> {
    // 修改方法名，使用 customer_CustomerId 作为路径
    List<SalesOrder> findByCustomer_CustomerId(int customerId);
}
