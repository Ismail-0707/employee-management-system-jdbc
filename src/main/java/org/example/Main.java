package org.example;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        EmployeeDAO dao = new EmployeeDAO();

        while(true)
        {
            System.out.println("\n===== Employee Management =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch(choice)
            {
                case 1:

                    System.out.print("Enter Id: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Name: ");
                    String name = sc.next();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    Employee emp = new Employee(id, name, salary);
                    dao.addEmployee(emp);

                    break;

                case 2:

                    dao.viewEmployees();
                    break;

                case 3:

                    System.out.print("Enter Employee Id: ");
                    int updateId = sc.nextInt();

                    System.out.print("Enter New Salary: ");
                    double newSalary = sc.nextDouble();

                    dao.updateEmployee(updateId, newSalary);

                    break;

                case 4:

                    System.out.print("Enter Employee Id: ");

                    int deleteId = sc.nextInt();

                    dao.deleteEmployee(deleteId);

                    break;
                case 5:

                    System.out.print("Enter Employee Id: ");

                    int searchId = sc.nextInt();

                    dao.searchEmployee(searchId);

                    break;
                case 6:

                    System.out.println("Thank You");
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice");
            }
        }
    }
}
