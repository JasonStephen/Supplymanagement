package com.jason.supplymanagement;

import com.jason.supplymanagement.service.Logistics.LogisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class SupplyManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupplyManagementApplication.class, args);
    }

}