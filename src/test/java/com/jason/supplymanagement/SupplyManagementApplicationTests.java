package com.jason.supplymanagement;

import com.jason.supplymanagement.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SupplyManagementApplicationTests {

    @Autowired
    private UserService userService;
}