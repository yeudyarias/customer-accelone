package com.accelone.customer.service.impl;

import com.accelone.customer.entity.Customer;
import com.accelone.customer.repository.CustomerRepository;
import com.accelone.customer.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> getById(Long id){
        return customerRepository.findById(id);
    }
    @Override
    public Optional<Customer> getCustomerByName(String name){
        return customerRepository.findCustomerByName(name);
    }
    @Override
    public Optional<Customer> saveCustomer(Customer customer) {
        return Optional.of(customerRepository.save(customer));
    }
    @Override
    public Optional<Customer> updateCustomer(Customer newCustomer,Customer dbCustomer, Long id) {
        mapper.map(newCustomer,dbCustomer);
        return Optional.of(customerRepository.save(dbCustomer));
    }
}
