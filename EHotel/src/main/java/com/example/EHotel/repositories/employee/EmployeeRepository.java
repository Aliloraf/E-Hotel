package com.example.EHotel.repositories.employee;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.employee.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{

    List<Employee> findByRoleOrderByLastnameAscFirstnameAsc(String role);
}
