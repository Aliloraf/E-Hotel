package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.dtos.AddEmployeeDTO;
import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelService;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HotelService hotelService;

    @RequestMapping("")
    public String showManagerHome() {
        return "manager";
    }

    @GetMapping("/employees")
    public String showsEmployees(Model model) {
        List<Employee> employees = employeeService.getEmployees();
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/employee/add")
    public String showAddEmployeeForm(Model model) {
        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("employee", new AddEmployeeDTO());
        model.addAttribute("hotels", hotels);
        return "add-employee-form";
    }

    @PostMapping("/employee/add")
    public String addEmployee(@ModelAttribute("employee") AddEmployeeDTO employeeDTO) {
        Employee newEmployee = new Employee();
        Hotel hotel = hotelService.getHotel(employeeDTO.getIdHotel());

        newEmployee.setSinEmployee(employeeDTO.getSinEmployee());
        newEmployee.setFirstname(employeeDTO.getFirstname());
        newEmployee.setLastname(employeeDTO.getLastname());
        newEmployee.setHotel(hotel);
        newEmployee.setRole(employeeDTO.getRole());
        newEmployee.setStreetNumber(employeeDTO.getStreetNumber());
        newEmployee.setStreetName(employeeDTO.getStreetName());
        newEmployee.setCity(employeeDTO.getCity());
        newEmployee.setPostalCode(employeeDTO.getPostalCode());
        newEmployee.setCountry(employeeDTO.getCountry());

        employeeService.addEmployee(newEmployee);

        return "redirect:/manager/employees";
    }

    @GetMapping("/employee/delete/{sinEmployee}")
    public String deleteEmployee(@PathVariable("sinEmployee") String sinEmployee) {
        employeeService.deleteEmployee(sinEmployee);
        return "redirect:/manager/employees";
    }

    @GetMapping("/employee/update/{sinEmployee}")
    public String showUpdateEmployeeForm(@PathVariable("sinEmployee") String sinEmployee, Model model) {
        Employee existingEmployee = employeeService.getEmployee(sinEmployee);
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();

        employeeDTO.setSinEmployee(existingEmployee.getSinEmployee());
        employeeDTO.setFirstname(existingEmployee.getFirstname());
        employeeDTO.setLastname(existingEmployee.getLastname());
        employeeDTO.setIdHotel(existingEmployee.getHotel().getIdHotel());
        employeeDTO.setRole(existingEmployee.getRole());
        employeeDTO.setStreetNumber(existingEmployee.getStreetNumber());
        employeeDTO.setStreetName(existingEmployee.getStreetName());
        employeeDTO.setCity(existingEmployee.getCity());
        employeeDTO.setPostalCode(existingEmployee.getPostalCode());
        employeeDTO.setCountry(existingEmployee.getCountry());


        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("hotels", hotels);
        return "update-employee-form";
    }

    @PostMapping("/employee/update/{sinEmployee}")
    public String updateEmployee(@PathVariable("sinEmployee") String sinEmployee, @ModelAttribute("employee") AddEmployeeDTO employeeDTO) {
        Employee updatedEmployee = new Employee();
        Hotel hotel = hotelService.getHotel(employeeDTO.getIdHotel());

        updatedEmployee.setSinEmployee(employeeDTO.getSinEmployee());
        updatedEmployee.setFirstname(employeeDTO.getFirstname());
        updatedEmployee.setLastname(employeeDTO.getLastname());
        updatedEmployee.setHotel(hotel);
        updatedEmployee.setRole(employeeDTO.getRole());
        updatedEmployee.setStreetNumber(employeeDTO.getStreetNumber());
        updatedEmployee.setStreetName(employeeDTO.getStreetName());
        updatedEmployee.setCity(employeeDTO.getCity());
        updatedEmployee.setPostalCode(employeeDTO.getPostalCode());
        updatedEmployee.setCountry(employeeDTO.getCountry());

        employeeService.updateEmployee(updatedEmployee);

        return "redirect:/manager/employees";
    }
    
}
