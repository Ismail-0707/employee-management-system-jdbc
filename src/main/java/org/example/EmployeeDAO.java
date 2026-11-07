package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/collegedb";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                setting("EMS_DB_URL", DEFAULT_URL),
                setting("EMS_DB_USER", "root"),
                setting("EMS_DB_PASSWORD", "")
        );
    }

    public boolean createEmployee(Employee employee) throws SQLException {
        String query = "INSERT INTO employee (id, name, salary) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employee.getId());
            statement.setString(2, employee.getName());
            statement.setDouble(3, employee.getSalary());
            return statement.executeUpdate() == 1;
        }
    }

    public List<Employee> getEmployees() throws SQLException {
        String query = "SELECT id, name, salary FROM employee ORDER BY id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery()) {
            List<Employee> employees = new ArrayList<>();
            while (results.next()) {
                employees.add(readEmployee(results));
            }
            return employees;
        }
    }

    public Optional<Employee> findEmployee(int id) throws SQLException {
        String query = "SELECT id, name, salary FROM employee WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? Optional.of(readEmployee(results)) : Optional.empty();
            }
        }
    }

    public boolean updateEmployeeSalary(int id, double salary) throws SQLException {
        String query = "UPDATE employee SET salary = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, salary);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean removeEmployee(int id) throws SQLException {
        String query = "DELETE FROM employee WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public void addEmployee(Employee employee) {
        try {
            System.out.println(createEmployee(employee)
                    ? "1 Employee Inserted Successfully" : "No Employee Inserted");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void viewEmployees() {
        try {
            for (Employee employee : getEmployees()) {
                System.out.println(employee.getId() + " " + employee.getName() + " " + employee.getSalary());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateEmployee(int id, double salary) {
        try {
            System.out.println(updateEmployeeSalary(id, salary)
                    ? "1 Employee Updated Successfully" : "0 Employee Updated Successfully");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {
        try {
            System.out.println(removeEmployee(id)
                    ? "1 Employee Deleted Successfully" : "0 Employee Deleted Successfully");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void searchEmployee(int id) {
        try {
            Optional<Employee> employee = findEmployee(id);
            if (employee.isPresent()) {
                Employee found = employee.get();
                System.out.println("\nEmployee Found");
                System.out.println("ID : " + found.getId());
                System.out.println("Name : " + found.getName());
                System.out.println("Salary : " + found.getSalary());
            } else {
                System.out.println("Employee Not Found");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static Employee readEmployee(ResultSet results) throws SQLException {
        return new Employee(results.getInt("id"), results.getString("name"), results.getDouble("salary"));
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}