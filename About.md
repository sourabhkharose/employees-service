# Employees Service

This is a Spring Boot REST API service for managing employee data. It was developed as part of a coding assessment and provides endpoints to perform standard CRUD (Create, Read, Update, Delete) operations on employee records.

---

## Features

- Create a new employee
- Get details of an employee by ID
- Get details of an employee by name
- List all employees
- Delete an employee
  
---

## Tech Stack

- Java 17
- Spring Boot
- Gradle (build tool)

---

## How to Run the Service

### Prerequisites

- Java 17+
- Gradle installed or use the Gradle wrapper (`./gradlew`)
- Git

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/sourabhkharose/employees-service.git
   cd employees-service
2. **Run the Application**
3. **Access the API**
   - Once running, the service will be available at: http://localhost:8111

### Endpoints

- GET /api/v1/employees — List all employees
- GET /api/v1/employees/{id} — Get employee by ID
- GET /api/v1/employees/search/{searchString} — Search employee by name
- GET /api/v1/employees/highestSalary — Get the highest salary among all employees
- GET /api/v1/employees/topTenHighestEarningEmployeeNames — Get the list of employees earning the top ten highest salaries
- POST /api/v1/employees — Create new employee
- DELETE /api/v1/employees/{id} — Delete employee


