package com.jason.supplymanagement.controller.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsAgreement;
import com.jason.supplymanagement.service.Logistics.LogisticsAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logistics-agreements")
public class LogisticsAgreementController {

    @Autowired
    private LogisticsAgreementService logisticsAgreementService;

    @GetMapping
    public List<LogisticsAgreement> getAllLogisticsAgreements() {
        return logisticsAgreementService.getAllLogisticsAgreements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogisticsAgreement> getLogisticsAgreementById(@PathVariable int id) {
        LogisticsAgreement logisticsAgreement = logisticsAgreementService.getLogisticsAgreementById(id);
        if (logisticsAgreement != null) {
            return ResponseEntity.ok(logisticsAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public LogisticsAgreement createLogisticsAgreement(@RequestBody LogisticsAgreement logisticsAgreement) {
        return logisticsAgreementService.createLogisticsAgreement(logisticsAgreement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogisticsAgreement> updateLogisticsAgreement(@PathVariable int id, @RequestBody LogisticsAgreement logisticsAgreement) {
        LogisticsAgreement updatedLogisticsAgreement = logisticsAgreementService.updateLogisticsAgreement(id, logisticsAgreement);
        if (updatedLogisticsAgreement != null) {
            return ResponseEntity.ok(updatedLogisticsAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogisticsAgreement(@PathVariable int id) {
        logisticsAgreementService.deleteLogisticsAgreement(id);
        return ResponseEntity.noContent().build();
    }
}