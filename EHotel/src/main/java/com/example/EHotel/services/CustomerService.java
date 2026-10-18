package com.example.EHotel.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.AddCustomerDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.repositories.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Customer> getCustomers() {
        return customerRepository.findAll(Sort.by("lastname", "firstname"));
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findCustomer(String sinCustomer) {
        return customerRepository.findById(sinCustomer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(String sinCustomer) {
        return findCustomer(sinCustomer)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable : " + sinCustomer));
    }

    public Customer addCustomer(AddCustomerDTO dto) {
        // save() on an existing id would silently overwrite that customer
        if (customerRepository.existsById(dto.getSinCustomer())) {
            throw new BusinessRuleException("Un client avec le NAS " + dto.getSinCustomer() + " existe déjà.");
        }
        Customer customer = new Customer(dto);
        customer.setCheckInDate(LocalDate.now());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(String sinCustomer, AddCustomerDTO dto) {
        Customer customer = getCustomer(sinCustomer);
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setStreetNumber(dto.getStreetNumber());
        customer.setStreetName(dto.getStreetName());
        customer.setCity(dto.getCity());
        customer.setPostalCode(dto.getPostalCode());
        customer.setCountry(dto.getCountry());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String sinCustomer) {
        customerRepository.delete(getCustomer(sinCustomer));
        customerRepository.flush();
    }

    public AddCustomerDTO toDto(Customer customer) {
        return new AddCustomerDTO(customer.getSinCustomer(), customer.getFirstname(), customer.getLastname(),
                customer.getStreetNumber(), customer.getStreetName(), customer.getCity(),
                customer.getPostalCode(), customer.getCountry());
    }
}
