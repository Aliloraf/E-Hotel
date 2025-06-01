package com.example.EHotel.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.dtos.AddCustomerDTO;
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.services.CustomerService;



@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("")
    public String showEmployeeHome() {
        return "employee";
    }

    @GetMapping("/customers")
    public String showCustomers(Model model) {
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/customer/add")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer-form";
    }

    @PostMapping("/customer/add")
    public String addCustomer(@ModelAttribute("customer") AddCustomerDTO customerDTO) {
        Customer newCustomer = new Customer();
        newCustomer.setSinCustomer(customerDTO.getSinCustomer());
        newCustomer.setFirstname(customerDTO.getFirstname());
        newCustomer.setLastname(customerDTO.getLastname());
        newCustomer.setCheckInDate(customerDTO.getCheckInDate());
        newCustomer.setStreetNumber(customerDTO.getStreetNumber());
        newCustomer.setStreetName(customerDTO.getStreetName());
        newCustomer.setCity(customerDTO.getCity());
        newCustomer.setPostalCode(customerDTO.getPostalCode());
        newCustomer.setCountry(customerDTO.getCountry());
        

        customerService.addCustomer(newCustomer);
        return "redirect:/employee/customers";
    }


    @GetMapping("/customer/delete/{sinCustomer}")
    public String deleteCustomer(@PathVariable("sinCustomer") String sinCustomer) {
        customerService.deleteCustomer(sinCustomer);
        return "redirect:/employee/customers";
    }

    @GetMapping("/customer/update/{sinCustomer}")
    public String showUpdateCustomerForm(@PathVariable("sinCustomer") String sinCustomer, Model model) {
        Customer existingCustomer = customerService.getCustomer(sinCustomer);
        model.addAttribute("customer", existingCustomer);
        return "update-customer-form";
    }

    @PostMapping("/customer/update/{sinCustomer}")
    public String updateCustomer(@PathVariable("sinCustomer") String sinCustomer, @ModelAttribute("customer") AddCustomerDTO customerDTO) {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setSinCustomer(customerDTO.getSinCustomer());
        updatedCustomer.setFirstname(customerDTO.getFirstname());
        updatedCustomer.setLastname(customerDTO.getLastname());
        updatedCustomer.setCheckInDate(customerDTO.getCheckInDate());
        updatedCustomer.setStreetNumber(customerDTO.getStreetNumber());
        updatedCustomer.setStreetName(customerDTO.getStreetName());
        updatedCustomer.setCity(customerDTO.getCity());
        updatedCustomer.setPostalCode(customerDTO.getPostalCode());
        updatedCustomer.setCountry(customerDTO.getCountry());

        customerService.updateCustomer(updatedCustomer);
        return "redirect:/employee/customers";
    }

    
    
}
