package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.SalesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesContractDAO extends JpaRepository<SalesContract, Integer> {

    // 修改方法名
    List<SalesContract> findByCustomer_CustomerId(int customerId);
}
