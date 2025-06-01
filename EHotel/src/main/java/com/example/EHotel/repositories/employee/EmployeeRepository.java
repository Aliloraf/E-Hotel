package com.example.EHotel.repositories.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.example.EHotel.model.employee.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
    Optional<Employee> findBySinEmployee(String sinEmployee);
    void deleteBySinEmployee(String sinEmployee);
}