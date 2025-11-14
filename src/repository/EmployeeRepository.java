package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import models.users.Employee;
import models.users.Role;
import models.users.employees.Cashier;
import models.users.employees.Manager;
import models.users.employees.Stocker;
import util.Database;

public class EmployeeRepository implements IEmployeeRepository {

    @Override
    public Employee findByNik(String nik) {
        Employee employeeFound = null;
        String sql = "SELECT u.UserID, u.Name, u.Role, e.Salary, e.HireDate, e.WorkingHours, e.NIK, m.Department FROM employee e INNER JOIN users u ON e.EmployeeID = u.UserID LEFT JOIN manager m ON e.EmployeeID = m.ManagerID WHERE e.NIK = ? AND u.deletedAt IS NULL";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nik);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employeeFound = resultSetEmployee(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeFound;
    }

    @Override
    public void addEmployee(Employee e){
        String sqlUsers = "INSERT INTO users (UserID, Name, Role) VALUES (?, ?, ?)";
        String sqlEmployee = "INSERT INTO employee (EmployeeID, Salary, HireDate, WorkingHours, NIK) VALUES (?, ?, ?, ?, ?)";
        String sqlEmpRole = "";

        if (e.getRole() == Role.CASHIER) {
            sqlEmpRole = "INSERT INTO cashier (CashierID) VALUES (?)";
        } else if (e.getRole() == Role.MANAGER) {
            sqlEmpRole = "INSERT INTO manager (ManagerID, Department) VALUES (?, ?)";
        } else if (e.getRole() == Role.STOCKER) {
            sqlEmpRole = "INSERT INTO stocker (StockerID) VALUES (?)";
        }

        try {
            Connection conn = Database.connect();

            PreparedStatement stmtUsers = conn.prepareStatement(sqlUsers);
            stmtUsers.setString(1, e.getUserID().toString());
            stmtUsers.setString(2, e.getName());
            stmtUsers.setString(3, e.getRole().name());
            stmtUsers.executeUpdate();

            PreparedStatement stmtEmployee = conn.prepareStatement(sqlEmployee);
            stmtEmployee.setString(1, e.getUserID().toString());
            stmtEmployee.setDouble(2, e.getSalary());
            stmtEmployee.setDate(3, e.getHireDate());
            stmtEmployee.setInt(4, e.getWorkingHours());
            stmtEmployee.setString(5, e.getNik());
            stmtEmployee.executeUpdate();

            PreparedStatement stmtRole = conn.prepareStatement(sqlEmpRole);
            stmtRole.setString(1, e.getUserID().toString());
            if (e instanceof Manager) {
                stmtRole.setString(2, ((Manager) e).getDepartment());
            }
            stmtRole.executeUpdate();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(String nik) {
        String sql = "UPDATE users u INNER JOIN employee e ON u.UserID = e.EmployeeID SET u.deletedAt = NOW(), e.deletedAt = NOW() WHERE e.NIK = ?";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nik);
            stmt.executeUpdate();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    @Override
    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> employeeList = new ArrayList<>();

        String sql = "SELECT u.UserID, u.Name, u.Role, e.Salary, e.HireDate, e.WorkingHours, e.NIK, m.Department FROM employee e INNER JOIN users u ON e.EmployeeID = u.UserID LEFT JOIN manager m ON e.EmployeeID = m.ManagerID WHERE u.deletedAt IS NULL";

        try {
            Connection conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee e = resultSetEmployee(rs);
                employeeList.add(e);
            }
        } catch (SQLException e4) {
            e4.printStackTrace();
        }
        return employeeList;
    }

    private Employee resultSetEmployee(ResultSet rs) throws SQLException {
        UUID userID = UUID.fromString(rs.getString("UserID"));
        String name = rs.getString("Name");
        Role role = Role.valueOf(rs.getString("Role"));
        int salary = rs.getInt("Salary");
        Date hireDate = rs.getDate("HireDate");
        int workingHours = rs.getInt("WorkingHours");
        String nik = rs.getString("NIK");

        Employee e = null;

        if (role == Role.CASHIER) {
            e = new Cashier(salary, hireDate, workingHours, nik, null, name);
        } else if (role == Role.MANAGER) {
            String department = rs.getString("Department");
            e = new Manager(department, salary, hireDate, workingHours, nik, null, name);
        } else if (role == Role.STOCKER) {
            e = new Stocker(salary, hireDate, workingHours, nik, null, name);
        }

        if (e != null) {
            e.setUserID(userID);
        }

        return e;
    }
}
