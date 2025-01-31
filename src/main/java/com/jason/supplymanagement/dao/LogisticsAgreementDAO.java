package com.jason.supplymanagement.dao;

import com.jason.supplymanagement.entity.LogisticsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsAgreementDAO extends JpaRepository<LogisticsAgreement, Integer> {
    // 自定义方法：根据物流公司ID查询物流协议列表
    List<LogisticsAgreement> findByLogisticsCompanyId(int logisticsCompanyId);
}
