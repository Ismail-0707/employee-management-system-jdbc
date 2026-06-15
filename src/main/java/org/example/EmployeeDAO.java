package org.example;
import java.sql.*;

public class EmployeeDAO
{
    private final String url = "jdbc:mysql://localhost:3306/collegedb";
    private final String username = "root";
    private final String password = "Ismail@2006"; // Change this

    public Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(url, username, password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    // CREATE
    public void addEmployee(Employee emp)
    {
        String query = "INSERT INTO employee VALUES(?,?,?)";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query))
        {
            ps.setInt(1, emp.getId());
            ps.setString(2, emp.getName());
            ps.setDouble(3, emp.getSalary());

            int rows = ps.executeUpdate();

            System.out.println(rows +
                    " Employee Inserted Successfully");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // READ
    public void viewEmployees()
    {
        String query =
                "SELECT * FROM employee";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query))
        {
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                System.out.println(
                        rs.getInt("id") + " "
                                + rs.getString("name") + " "
                                + rs.getDouble("salary")
                );
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateEmployee(int id, double salary)
    {
        String query = "UPDATE employee SET salary=? WHERE id=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query))
        {
            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            System.out.println(rows + " Employee Updated Successfully");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteEmployee(int id)
    {
        String query = "DELETE FROM employee WHERE id=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query))
        {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            System.out.println(rows +
                    " Employee Deleted Successfully");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // SEARCH
    public void searchEmployee(int id)
    {
        String query = "SELECT * FROM employee WHERE id=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query))
        {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                System.out.println("\nEmployee Found");

                System.out.println("ID : " + rs.getInt("id"));

                System.out.println("Name : " + rs.getString("name"));

                System.out.println("Salary : " + rs.getDouble("salary"));
            }
            else
            {
                System.out.println("Employee Not Found");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}