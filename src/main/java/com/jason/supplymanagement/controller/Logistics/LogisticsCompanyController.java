package com.jason.supplymanagement.controller.Logistics;

import com.jason.supplymanagement.entity.Logistics.LogisticsCompany;
import com.jason.supplymanagement.service.Logistics.LogisticsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logistics-companies")
public class LogisticsCompanyController {

    @Autowired
    private LogisticsCompanyService logisticsCompanyService;

    @PostMapping
    public LogisticsCompany createLogisticsCompany(@RequestBody LogisticsCompany logisticsCompany) {
        return logisticsCompanyService.saveLogisticsCompany(logisticsCompany);
    }

    @PutMapping("/{id}")
    public LogisticsCompany updateLogisticsCompany(@PathVariable int id, @RequestBody LogisticsCompany logisticsCompany) {
        logisticsCompany.setLogisticsCompanyId(id);
        return logisticsCompanyService.updateLogisticsCompany(logisticsCompany);
    }

    @DeleteMapping("/{id}")
    public void deleteLogisticsCompany(@PathVariable int id) {
        logisticsCompanyService.deleteLogisticsCompany(id);
    }

    @GetMapping("/{id}")
    public LogisticsCompany getLogisticsCompanyById(@PathVariable int id) {
        return logisticsCompanyService.getLogisticsCompanyById(id);
    }

    @GetMapping("/list")
    public List<LogisticsCompany> getAllLogisticsCompanies() {
        return logisticsCompanyService.getAllLogisticsCompanies();
    }
}