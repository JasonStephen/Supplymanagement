package com.jason.supplymanagement.service.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsAgreement;

import java.util.List;

public interface LogisticsAgreementService {
    List<LogisticsAgreement> getAllLogisticsAgreements();
    LogisticsAgreement getLogisticsAgreementById(int id);
    LogisticsAgreement createLogisticsAgreement(LogisticsAgreement logisticsAgreement);
    LogisticsAgreement updateLogisticsAgreement(int id, LogisticsAgreement logisticsAgreement);
    void deleteLogisticsAgreement(int id);
}