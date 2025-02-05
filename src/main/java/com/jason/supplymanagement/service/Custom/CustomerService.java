package com.jason.supplymanagement.service.Custom;

import com.jason.supplymanagement.entity.Custom.Customer;
import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(int customerId);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
}