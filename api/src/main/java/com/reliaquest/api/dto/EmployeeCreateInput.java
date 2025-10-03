package com.reliaquest.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeCreateInput {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Salary cannot be null")
    @Min(value = 1, message = "Salary must be greater than zero")
    private Integer salary;

    @NotNull(message = "Age cannot be null")
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 75, message = "Age cannot be more than 75")
    private Integer age;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @jakarta.validation.constraints.Email(message = "Email should be valid")
    private String email;
}