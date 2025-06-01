package com.example.EHotel.repositories.customer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.customer.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    Optional<Customer> findBySinCustomer(String sinCustomer);
    void deleteBySinCustomer(String sinCustomer);
}
