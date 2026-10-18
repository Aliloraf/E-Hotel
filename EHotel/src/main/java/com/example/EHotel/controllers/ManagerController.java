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

import com.example.EHotel.dtos.AddEmployeeDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/** Manager area: employee management. */
@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final EmployeeService employeeService;
    private final HotelService hotelService;

    @GetMapping("")
    public String showManagerHome() {
        return "manager";
    }

    @GetMapping("/employees")
    public String showEmployees(Model model) {
        model.addAttribute("employees", employeeService.getEmployees());
        return "employees";
    }

    @GetMapping("/employee/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new AddEmployeeDTO());
        return employeeForm("add-employee-form", model);
    }

    @PostMapping("/employee/add")
    public String addEmployee(@Valid @ModelAttribute("employee") AddEmployeeDTO employeeDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return employeeForm("add-employee-form", model);
        }
        try {
            employeeService.addEmployee(employeeDTO);
        } catch (BusinessRuleException e) {
            result.rejectValue("sinEmployee", "duplicate", e.getMessage());
            return employeeForm("add-employee-form", model);
        }
        redirect.addFlashAttribute("success", "Employé ajouté.");
        return "redirect:/manager/employees";
    }

    @PostMapping("/employee/delete/{sinEmployee}")
    public String deleteEmployee(@PathVariable("sinEmployee") String sinEmployee, RedirectAttributes redirect) {
        employeeService.deleteEmployee(sinEmployee);
        redirect.addFlashAttribute("success", "Employé supprimé.");
        return "redirect:/manager/employees";
    }

    @GetMapping("/employee/update/{sinEmployee}")
    public String showUpdateEmployeeForm(@PathVariable("sinEmployee") String sinEmployee, Model model) {
        model.addAttribute("employee", employeeService.toDto(employeeService.getEmployee(sinEmployee)));
        return employeeForm("update-employee-form", model);
    }

    @PostMapping("/employee/update/{sinEmployee}")
    public String updateEmployee(@PathVariable("sinEmployee") String sinEmployee,
            @Valid @ModelAttribute("employee") AddEmployeeDTO employeeDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        employeeDTO.setSinEmployee(sinEmployee); // the SIN is the primary key and cannot be changed
        if (result.hasErrors()) {
            return employeeForm("update-employee-form", model);
        }
        employeeService.updateEmployee(sinEmployee, employeeDTO);
        redirect.addFlashAttribute("success", "Employé modifié.");
        return "redirect:/manager/employees";
    }

    private String employeeForm(String view, Model model) {
        model.addAttribute("hotels", hotelService.getHotels());
        return view;
    }
}
