package com.jason.supplymanagement.service.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsCompany;
import java.util.List;

public interface LogisticsCompanyService {
    LogisticsCompany saveLogisticsCompany(LogisticsCompany logisticsCompany);
    LogisticsCompany updateLogisticsCompany(LogisticsCompany logisticsCompany);
    void deleteLogisticsCompany(int logisticsCompanyId);
    LogisticsCompany getLogisticsCompanyById(int logisticsCompanyId);
    List<LogisticsCompany> getAllLogisticsCompanies();
}