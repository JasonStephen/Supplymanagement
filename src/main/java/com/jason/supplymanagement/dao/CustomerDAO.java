package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer> {
    // 自定义方法：根据客户名称查询客户
    Customer findByName(String name);
}
