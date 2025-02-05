package com.jason.supplymanagement.dao.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsCompanyDAO extends JpaRepository<LogisticsCompany, Integer> {
    // 自定义方法：根据物流公司名称查询物流公司
    LogisticsCompany findByName(String name);
}
