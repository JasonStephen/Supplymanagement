package com.jason.supplymanagement.service.impl.Custom;

import com.jason.supplymanagement.dao.Custom.CustomerDAO;
import com.jason.supplymanagement.entity.Custom.Customer;
import com.jason.supplymanagement.service.Custom.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerDAO.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerDAO.save(customer);
    }

    @Override
    public void deleteCustomer(int customerId) {
        customerDAO.deleteById(customerId);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerDAO.findById(customerId).orElse(null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }
}