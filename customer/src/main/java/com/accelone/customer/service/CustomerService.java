package com.accelone.customer.service;

import com.accelone.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public List<Customer> getAllCustomers();
    public Optional<Customer> getById(Long id);
    public Optional<Customer> getCustomerByName(String name);
    public Optional<Customer> saveCustomer(Customer customer);
    public Optional<Customer> updateCustomer(Customer newCustomer,Customer dbCustomer, Long id);
}
