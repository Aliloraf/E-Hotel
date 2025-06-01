package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.repositories.employee.EmployeeRepository;

import jakarta.transaction.Transactional;

import java.util.List;


@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @SuppressWarnings("null")
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Employee getEmployee(String sinEmployee) {
        return employeeRepository.findBySinEmployee(sinEmployee).orElse(null);
    }

    public void deleteEmployee(String sinEmployee) {
        employeeRepository.deleteBySinEmployee(sinEmployee);
    }

    @SuppressWarnings("null")
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
}
