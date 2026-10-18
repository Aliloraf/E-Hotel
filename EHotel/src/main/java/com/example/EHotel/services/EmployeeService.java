package com.example.EHotel.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.AddEmployeeDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.repositories.employee.EmployeeRepository;
import com.example.EHotel.repositories.hotel.HotelRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    public static final String MANAGER_ROLE = "Manager";

    private final EmployeeRepository employeeRepository;
    private final HotelRepository hotelRepository;

    @Transactional(readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepository.findAll(Sort.by("lastname", "firstname"));
    }

    @Transactional(readOnly = true)
    public List<Employee> getManagers() {
        return employeeRepository.findByRoleOrderByLastnameAscFirstnameAsc(MANAGER_ROLE);
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(String sinEmployee) {
        return employeeRepository.findById(sinEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable : " + sinEmployee));
    }

    public Employee addEmployee(AddEmployeeDTO dto) {
        // save() on an existing id would silently overwrite that employee
        if (employeeRepository.existsById(dto.getSinEmployee())) {
            throw new BusinessRuleException("Un employé avec le NAS " + dto.getSinEmployee() + " existe déjà.");
        }
        Employee employee = new Employee();
        employee.setSinEmployee(dto.getSinEmployee());
        copyFields(dto, employee);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String sinEmployee, AddEmployeeDTO dto) {
        Employee employee = getEmployee(sinEmployee);
        copyFields(dto, employee);
        return employeeRepository.save(employee);
    }

    /** Deletes an employee. Hotels managed by this employee are left without a manager. */
    public void deleteEmployee(String sinEmployee) {
        Employee employee = getEmployee(sinEmployee);
        hotelRepository.findByManager(employee).forEach(hotel -> hotel.setManager(null));
        employeeRepository.delete(employee);
        employeeRepository.flush();
    }

    public AddEmployeeDTO toDto(Employee employee) {
        return new AddEmployeeDTO(employee.getSinEmployee(), employee.getFirstname(), employee.getLastname(),
                employee.getRole(), employee.getStreetNumber(), employee.getStreetName(), employee.getCity(),
                employee.getPostalCode(), employee.getCountry(),
                employee.getHotel() != null ? employee.getHotel().getIdHotel() : null);
    }

    private void copyFields(AddEmployeeDTO dto, Employee employee) {
        Hotel hotel = hotelRepository.findById(dto.getIdHotel())
                .orElseThrow(() -> new ResourceNotFoundException("Hôtel introuvable : " + dto.getIdHotel()));
        employee.setFirstname(dto.getFirstname());
        employee.setLastname(dto.getLastname());
        employee.setRole(dto.getRole());
        employee.setStreetNumber(dto.getStreetNumber());
        employee.setStreetName(dto.getStreetName());
        employee.setCity(dto.getCity());
        employee.setPostalCode(dto.getPostalCode());
        employee.setCountry(dto.getCountry());
        employee.setHotel(hotel);
    }
}
