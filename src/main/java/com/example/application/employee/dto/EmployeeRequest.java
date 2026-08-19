package com.example.application.employee.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    private String designation;

    @Positive(message = "Salary must be positive")
    private Double salary;
}
