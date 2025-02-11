package com.jason.supplymanagement;

import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement // 启用事务管理
public class SupplyManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupplyManagementApplication.class, args);
    }

}