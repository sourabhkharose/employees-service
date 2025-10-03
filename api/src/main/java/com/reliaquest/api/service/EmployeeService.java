package com.reliaquest.api.service;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeCreateInput;
import java.util.List;

/**
 * A service interface that outlines the core business logic for managing employee data.
 * It defines all necessary operations like fetching, searching, creating, and deleting employees.
 */

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByNameSearch(String searchString);
    Employee getEmployeeById(String id);
    Integer getHighestSalaryOfEmployees();
    List<String> getTopTenHighestEarningEmployeeNames();
    Employee createEmployee(EmployeeCreateInput employeeInput);
    String deleteEmployeeById(String id);
}