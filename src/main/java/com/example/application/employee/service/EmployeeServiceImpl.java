package com.example.application.employee.service;

import com.example.application.employee.dto.EmployeeRequest;
import com.example.application.employee.dto.EmployeeResponse;
import com.example.application.employee.entity.Employee;
import com.example.application.employee.exception.EmployeeNotFoundException;
import com.example.application.employee.mapper.EmployeeMapper;
import com.example.application.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        try {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            Employee employee = EmployeeMapper.toEntity(request);

            employee.setEmployeeId(generateEmployeeId());

            Employee savedEmployee = employeeRepository.save(employee);

            return EmployeeMapper.toResponse(savedEmployee);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating employee: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new EmployeeNotFoundException(
                                    "Employee not found with id: " + id));

            return EmployeeMapper.toResponse(employee);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching employee: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        try {
            return employeeRepository.findAll()
                    .stream()
                    .map(EmployeeMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all employees: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new EmployeeNotFoundException(
                                    "Employee not found with id: " + id));

            employee.setName(request.getName());
            employee.setEmail(request.getEmail());
            employee.setPhone(request.getPhone());
            employee.setDepartment(request.getDepartment());
            employee.setDesignation(request.getDesignation());
            employee.setSalary(request.getSalary());

            Employee updatedEmployee =
                    employeeRepository.save(employee);

            return EmployeeMapper.toResponse(updatedEmployee);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() ->
                            new EmployeeNotFoundException(
                                    "Employee not found with id: " + id));

            employeeRepository.delete(employee);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting employee: " + e.getMessage(), e);
        }
    }

    private String generateEmployeeId() {
        try {
            long count = employeeRepository.count() + 1;
            return String.format("EMP%04d", count);
        } catch (Exception e) {
            throw new RuntimeException("Error generating employee ID: " + e.getMessage(), e);
        }
    }
}