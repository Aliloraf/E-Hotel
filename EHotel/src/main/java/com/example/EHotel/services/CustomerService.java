package com.example.EHotel.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.repositories.customer.CustomerRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @SuppressWarnings("null")
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomer(String sinCustomer) {
        return customerRepository.findBySinCustomer(sinCustomer).orElse(null);
    }

    public void deleteCustomer(String sinCustomer) {
        customerRepository.deleteBySinCustomer(sinCustomer);
    }

    @SuppressWarnings("null")
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
