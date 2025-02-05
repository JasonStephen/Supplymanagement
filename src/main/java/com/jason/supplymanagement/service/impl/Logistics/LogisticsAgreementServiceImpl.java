package com.jason.supplymanagement.service.impl.Logistics;

import com.jason.supplymanagement.dao.Logistics.LogisticsAgreementDAO;
import com.jason.supplymanagement.entity.Logistics.LogisticsAgreement;
import com.jason.supplymanagement.service.Logistics.LogisticsAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogisticsAgreementServiceImpl implements LogisticsAgreementService {

    @Autowired
    private LogisticsAgreementDAO logisticsAgreementDAO;

    @Override
    public List<LogisticsAgreement> getAllLogisticsAgreements() {
        return logisticsAgreementDAO.findAll();
    }

    @Override
    public LogisticsAgreement getLogisticsAgreementById(int id) {
        return logisticsAgreementDAO.findById(id).orElse(null);
    }

    @Override
    public LogisticsAgreement createLogisticsAgreement(LogisticsAgreement logisticsAgreement) {
        return logisticsAgreementDAO.save(logisticsAgreement);
    }

    @Override
    public LogisticsAgreement updateLogisticsAgreement(int id, LogisticsAgreement logisticsAgreement) {
        if (logisticsAgreementDAO.existsById(id)) {
            logisticsAgreement.setAgreementId(id);
            return logisticsAgreementDAO.save(logisticsAgreement);
        }
        return null;
    }

    @Override
    public void deleteLogisticsAgreement(int id) {
        logisticsAgreementDAO.deleteById(id);
    }
}