package com.example.EHotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.EHotel.dtos.AddCustomerDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.services.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/** Employee area: customer management. */
@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final CustomerService customerService;

    @GetMapping("")
    public String showEmployeeHome() {
        return "employee";
    }

    @GetMapping("/customers")
    public String showCustomers(Model model) {
        model.addAttribute("customers", customerService.getCustomers());
        return "customers";
    }

    @GetMapping("/customer/add")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new AddCustomerDTO());
        return "add-customer-form";
    }

    @PostMapping("/customer/add")
    public String addCustomer(@Valid @ModelAttribute("customer") AddCustomerDTO customerDTO, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "add-customer-form";
        }
        try {
            customerService.addCustomer(customerDTO);
        } catch (BusinessRuleException e) {
            result.rejectValue("sinCustomer", "duplicate", e.getMessage());
            return "add-customer-form";
        }
        redirect.addFlashAttribute("success", "Client ajouté.");
        return "redirect:/employee/customers";
    }

    @PostMapping("/customer/delete/{sinCustomer}")
    public String deleteCustomer(@PathVariable("sinCustomer") String sinCustomer, RedirectAttributes redirect) {
        customerService.deleteCustomer(sinCustomer);
        redirect.addFlashAttribute("success", "Client supprimé.");
        return "redirect:/employee/customers";
    }

    @GetMapping("/customer/update/{sinCustomer}")
    public String showUpdateCustomerForm(@PathVariable("sinCustomer") String sinCustomer, Model model) {
        model.addAttribute("customer", customerService.toDto(customerService.getCustomer(sinCustomer)));
        return "update-customer-form";
    }

    @PostMapping("/customer/update/{sinCustomer}")
    public String updateCustomer(@PathVariable("sinCustomer") String sinCustomer,
            @Valid @ModelAttribute("customer") AddCustomerDTO customerDTO, BindingResult result,
            RedirectAttributes redirect) {
        customerDTO.setSinCustomer(sinCustomer); // the SIN is the primary key and cannot be changed
        if (result.hasErrors()) {
            return "update-customer-form";
        }
        customerService.updateCustomer(sinCustomer, customerDTO);
        redirect.addFlashAttribute("success", "Client modifié.");
        return "redirect:/employee/customers";
    }
}
