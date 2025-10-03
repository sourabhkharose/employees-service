package com.reliaquest.api.service;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeCreateInput;
import com.reliaquest.api.dto.ExternalApiResponse;
import com.reliaquest.api.exception.ExternalApiException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String externalApiBaseUrl;

    public EmployeeServiceImpl(RestTemplate restTemplate, @Value("${external.api.baseurl}") String externalApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.externalApiBaseUrl = externalApiBaseUrl;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees from external API");
        return fetchAllEmployeesFromApi();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        logger.info("Searching for employees with name containing: {}", searchString);
        List<Employee> allEmployees = fetchAllEmployeesFromApi();
        return allEmployees.stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        logger.info("Fetching employee with ID: {}", id);
        String url = externalApiBaseUrl + "/" + id;
        try {
            ResponseEntity<ExternalApiResponse<Employee>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Employee not found with ID: {}", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        } catch (RestClientException e) {
            logger.error("Error calling external API to get employee by ID: {}", e.getMessage());
            throw new ExternalApiException("Error fetching employee data from external service.", e);
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        logger.info("Calculating highest salary of all employees");
        List<Employee> allEmployees = fetchAllEmployeesFromApi();
        return allEmployees.stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names");
        List<Employee> allEmployees = fetchAllEmployeesFromApi();
        return allEmployees.stream()
                .sorted(Comparator.comparing(Employee::getEmployeeSalary).reversed())
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
    }

    @Override
    public Employee createEmployee(EmployeeCreateInput employeeInput) {
        logger.info("Creating a new employee with name: {}", employeeInput.getName());
        try {
            ResponseEntity<ExternalApiResponse<Employee>> response = restTemplate.exchange(
                    externalApiBaseUrl,
                    HttpMethod.POST,
                    new org.springframework.http.HttpEntity<>(employeeInput),
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (RestClientException e) {
            logger.error("Error calling external API to create employee: {}", e.getMessage());
            throw new ExternalApiException("Error creating employee via external service.", e);
        }
    }

    @Override
    public String deleteEmployeeById(String id) {
        logger.info("Attempting to delete employee with ID: {}", id);
        // Note: The external API deletes by name, but our contract requires deleting by ID.
        // This is the intentional mismatch. We solve it by first fetching the employee by ID to get their name.
        Employee employeeToDelete = getEmployeeById(id); // This will throw ResourceNotFoundException if ID is invalid
        String employeeName = employeeToDelete.getEmployeeName();
        String url = externalApiBaseUrl + "/" + employeeName;

        try {
            restTemplate.delete(url);
            logger.info("Successfully deleted employee '{}' with ID: {}", employeeName, id);
            return employeeName;
        } catch (RestClientException e) {
            logger.error("Error calling external API to delete employee: {}", e.getMessage());
            throw new ExternalApiException("Error deleting employee via external service.", e);
        }
    }

    /**
     * A private helper method to fetch all employees, providing a single point
     * of contact with the external API for scalability and maintenance.
     */
    private List<Employee> fetchAllEmployeesFromApi() {
        try {
            ResponseEntity<ExternalApiResponse<List<Employee>>> response = restTemplate.exchange(
                    externalApiBaseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (RestClientException e) {
            logger.error("Failed to fetch all employees from external API: {}", e.getMessage());
            throw new ExternalApiException("Could not fetch employee data from the external service.", e);
        }
    }
}