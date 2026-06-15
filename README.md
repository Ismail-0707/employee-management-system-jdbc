# Employee Management System

A console-based Employee Management System built using **Java**, **JDBC**, **MySQL**, and **Maven**. The application allows users to perform CRUD (Create, Read, Update, Delete) operations on employee records stored in a MySQL database.

## Features

- Add a new employee
- View all employees
- Search employee by ID
- Update employee salary
- Delete employee
- Menu-driven console interface
- MySQL database integration using JDBC
- Secure SQL execution using PreparedStatement

## Technologies Used

- Java
- JDBC (Java Database Connectivity)
- MySQL
- Maven
- Git & GitHub

## Project Structure

```text
Employee-Management-System
│
├── Employee.java
├── EmployeeDAO.java
├── Main.java
├── pom.xml
└── README.md
```

## Database Schema

```sql
CREATE DATABASE collegedb;

USE collegedb;

CREATE TABLE employee(
    id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DOUBLE
);
```

## Functionalities

### Add Employee
Insert a new employee record into the database.

### View Employees
Display all employee records stored in the database.

### Search Employee
Retrieve employee details using the employee ID.

### Update Employee
Modify the salary of an existing employee.

### Delete Employee
Remove an employee record from the database.

## Concepts Implemented

- Object-Oriented Programming (OOP)
- Encapsulation
- Constructors
- Getters and Setters
- JDBC Connectivity
- DriverManager
- Connection
- PreparedStatement
- ResultSet
- CRUD Operations
- DAO (Data Access Object) Pattern
- Exception Handling

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/Ismail-0707/employee-management-system-jdbc.git
```

2. Open the project in IntelliJ IDEA.

3. Create the database and table using the SQL script provided above.

4. Update the database credentials in `EmployeeDAO.java`.

5. Run `Main.java`.

## Sample Menu

```text
===== Employee Management System =====

1. Add Employee
2. View Employees
3. Update Employee
4. Delete Employee
5. Search Employee
6. Exit
```

## Learning Outcomes

Through this project, I gained hands-on experience with:

- Connecting Java applications to MySQL databases
- Performing CRUD operations using JDBC
- Using PreparedStatement for parameterized queries
- Processing query results with ResultSet
- Applying OOP principles in Java
- Managing source code using Git and GitHub

## Author

**Ismail**

GitHub: https://github.com/Ismail-0707
