package com.reliaquest.api.controller;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeCreateInput;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees") // Using a versioned and pluralized endpoint
public class EmployeeController implements IEmployeeController<Employee, EmployeeCreateInput> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    @RequestMapping()
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid EmployeeCreateInput employeeInput) {
        Employee createdEmployee = employeeService.createEmployee(employeeInput);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        String deletedEmployeeName = employeeService.deleteEmployeeById(id);
        String message = "Successfully deleted employee: " + deletedEmployeeName;
        return ResponseEntity.ok(message);
    }
}