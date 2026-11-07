# Employee Management System

A Java and JDBC employee management application backed by MySQL. It includes both the original menu-driven console app and an alternate Java Swing desktop interface.

## Features

- Add, list, search, update salary, and delete employee records
- Swing desktop directory with ID search, row selection, input validation, and operation feedback
- Console menu remains available through `org.example.Main`
- Prepared statements for database queries

## Requirements

- Java 25 or later
- Maven
- MySQL

## Database setup

Create the database and table before launching either interface:

```sql
CREATE DATABASE collegedb;

USE collegedb;

CREATE TABLE employee (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DOUBLE
);
```

The application defaults to `jdbc:mysql://localhost:3306/collegedb`, database user `root`, and an empty password. Configure credentials with environment variables rather than editing source code:

| Variable | Default | Description |
| --- | --- | --- |
| `EMS_DB_URL` | `jdbc:mysql://localhost:3306/collegedb` | JDBC connection URL |
| `EMS_DB_USER` | `root` | MySQL user |
| `EMS_DB_PASSWORD` | empty | MySQL password |

For example, in PowerShell:

```powershell
$env:EMS_DB_USER = "root"
$env:EMS_DB_PASSWORD = "your-password"
```

## Run the Swing desktop app

From the project directory, start the GUI with Maven:

```bash
mvn compile exec:java -Dexec.mainClass="org.example.EmployeeSwingApp"
```

In IntelliJ IDEA, import the project as a Maven project, configure the database environment variables in the run configuration, and run `EmployeeSwingApp`.

## Run the console app

Run `org.example.Main` from IntelliJ IDEA, or use Maven:

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```

## Project structure

```text
src/main/java/org/example/
├── Employee.java
├── EmployeeDAO.java
├── EmployeeSwingApp.java
└── Main.java
```
