package com.jason.supplymanagement.dao.Custom;

import com.jason.supplymanagement.entity.Custom.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDAO extends JpaRepository<SalesOrder, Integer> {
    List<SalesOrder> findByCustomer_CustomerId(int customerId);
}