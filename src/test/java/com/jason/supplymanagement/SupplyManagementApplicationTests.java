package com.jason.supplymanagement;

import com.jason.supplymanagement.entity.User;
import com.jason.supplymanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SupplyManagementApplicationTests {

    @Autowired
    private UserService userService;
}