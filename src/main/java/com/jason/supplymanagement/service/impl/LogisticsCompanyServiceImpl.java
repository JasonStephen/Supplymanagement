package com.jason.supplymanagement.service.impl;

import com.jason.supplymanagement.dao.LogisticsCompanyDAO;
import com.jason.supplymanagement.entity.LogisticsCompany;
import com.jason.supplymanagement.service.LogisticsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsCompanyServiceImpl implements LogisticsCompanyService {

    @Autowired
    private LogisticsCompanyDAO logisticsCompanyDAO;

    @Override
    public LogisticsCompany saveLogisticsCompany(LogisticsCompany logisticsCompany) {
        return logisticsCompanyDAO.save(logisticsCompany);
    }

    @Override
    public LogisticsCompany updateLogisticsCompany(LogisticsCompany logisticsCompany) {
        return logisticsCompanyDAO.save(logisticsCompany);
    }

    @Override
    public void deleteLogisticsCompany(int logisticsCompanyId) {
        logisticsCompanyDAO.deleteById(logisticsCompanyId);
    }

    @Override
    public LogisticsCompany getLogisticsCompanyById(int logisticsCompanyId) {
        return logisticsCompanyDAO.findById(logisticsCompanyId).orElse(null);
    }

    @Override
    public List<LogisticsCompany> getAllLogisticsCompanies() {
        return logisticsCompanyDAO.findAll();
    }
}