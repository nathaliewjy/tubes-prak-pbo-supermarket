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
        String sql = "SELECT u.UserID, u.Name, u.Role, u.deletedAt, e.Salary, e.HireDate, e.WorkingHours, e.NIK, m.Department FROM employee e INNER JOIN users u ON e.EmployeeID = u.UserID LEFT JOIN manager m ON e.EmployeeID = m.ManagerID WHERE e.NIK = ? AND u.deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nik);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetEmployee(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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

        try (Connection conn = Database.connect();
             PreparedStatement stmtUsers = conn.prepareStatement(sqlUsers);
             PreparedStatement stmtEmployee = conn.prepareStatement(sqlEmployee);
             PreparedStatement stmtRole = conn.prepareStatement(sqlEmpRole)) {

            stmtUsers.setString(1, e.getUserID().toString());
            stmtUsers.setString(2, e.getName());
            stmtUsers.setString(3, e.getRole().name());
            stmtUsers.executeUpdate();

            stmtEmployee.setString(1, e.getUserID().toString());
            stmtEmployee.setDouble(2, e.getSalary());
            stmtEmployee.setDate(3, e.getHireDate());
            stmtEmployee.setInt(4, e.getWorkingHours());
            stmtEmployee.setString(5, e.getNik());
            stmtEmployee.executeUpdate();

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

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nik);
            stmt.executeUpdate();

        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    @Override
    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> employeeList = new ArrayList<>();

        String sql = "SELECT u.UserID, u.Name, u.Role, u.deletedAt, e.Salary, e.HireDate, e.WorkingHours, e.NIK, m.Department FROM employee e INNER JOIN users u ON e.EmployeeID = u.UserID LEFT JOIN manager m ON e.EmployeeID = m.ManagerID WHERE u.deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee e = resultSetEmployee(rs);
                employeeList.add(e);
            }

        } catch (SQLException e4) {
            e4.printStackTrace();
        }

        return employeeList;
    }

    @Override
    public Employee findById(UUID employeeID) {
        String sql = "SELECT u.UserID, u.Name, u.Role, u.deletedAt, e.Salary, e.HireDate, e.WorkingHours, e.NIK, m.Department FROM employee e INNER JOIN users u ON e.EmployeeID = u.UserID LEFT JOIN manager m ON e.EmployeeID = m.ManagerID WHERE u.UserID = ? AND u.deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeID.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetEmployee(rs);
                }
            }

        } catch (SQLException e5) {
            e5.printStackTrace();
        }

        return null;
    }
    private Employee resultSetEmployee(ResultSet rs) throws SQLException {
        UUID userID = UUID.fromString(rs.getString("UserID"));
        String name = rs.getString("Name");
        Role role = Role.valueOf(rs.getString("Role"));
        Date deletedAt = rs.getDate("deletedAt");
        int salary = rs.getInt("Salary");
        Date hireDate = rs.getDate("HireDate");
        int workingHours = rs.getInt("WorkingHours");
        String nik = rs.getString("NIK");

        Employee e = null;

        if (role == Role.CASHIER) {
            e = new Cashier(userID, name, deletedAt, salary, hireDate, workingHours, nik);
        } else if (role == Role.MANAGER) {
            String department = rs.getString("Department");
            e = new Manager(userID, name, deletedAt, salary, hireDate, workingHours, nik, department);
        } else if (role == Role.STOCKER) {
            e = new Stocker(userID, name, deletedAt, salary, hireDate, workingHours, nik);
        }

        return e;
    }

    @Override
    public void updateJobdesk(UUID employeeID, String jobdesk) {
        // di db nya belum ada jobdesk jadi nanti
    }

    @Override
    public void updateEmployee(Employee e) {
        String sqlUsers = "UPDATE users SET Name = ?, Role = ? WHERE UserID = ?";
        String sqlEmp = "UPDATE employee SET Salary = ?, WorkingHours = ? WHERE EmployeeID = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmtUsers = conn.prepareStatement(sqlUsers);
             PreparedStatement stmtEmp = conn.prepareStatement(sqlEmp)) {

            stmtUsers.setString(1, e.getName());
            stmtUsers.setString(2, e.getRole().name());
            stmtUsers.setString(3, e.getUserID().toString());
            stmtUsers.executeUpdate();

            stmtEmp.setInt(1, e.getSalary());
            stmtEmp.setInt(2, e.getWorkingHours());
            stmtEmp.setString(3, e.getUserID().toString());
            stmtEmp.executeUpdate();

        } catch (SQLException e6) {
            e6.printStackTrace();
        }
    }

    @Override
    public void changeRole(UUID employeeID, Role oldRole, Role newRole) {
        String sql = "UPDATE users SET Role = ? WHERE UserID = ?";

        String sqlOldRole = "";
        if (oldRole == Role.CASHIER) {
            sqlOldRole = "DELETE FROM cashier WHERE CashierID = ?";
        } else if (oldRole == Role.STOCKER) {
            sqlOldRole = "DELETE FROM stocker WHERE StockerID = ?";
        } else if (oldRole == Role.MANAGER) {
            sqlOldRole = "DELETE FROM manager WHERE ManagerID = ?";
        }

        String sqlNewRole = "";
        if (newRole == Role.CASHIER) {
            sqlNewRole = "INSERT INTO cashier (CashierID) VALUES (?)";
        } else if (newRole == Role.STOCKER) {
            sqlNewRole = "INSERT INTO stocker (StockerID) VALUES (?)";
        } else if (newRole == Role.MANAGER) {
            sqlNewRole = "INSERT INTO manager (ManagerID, Department) VALUES (?,?)";
        }

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole.name());
            stmt.setString(2, employeeID.toString());
            stmt.executeUpdate();

            if (!sqlOldRole.isEmpty()) {
                try (PreparedStatement stmtOldRole = conn.prepareStatement(sqlOldRole)) {
                    stmtOldRole.setString(1, employeeID.toString());
                    stmtOldRole.executeUpdate();
                }
            }

            if (!sqlNewRole.isEmpty()) {
                try (PreparedStatement stmtNewRole = conn.prepareStatement(sqlNewRole)) {
                    stmtNewRole.setString(1, employeeID.toString());

                    if (newRole == Role.MANAGER) {
                        stmtNewRole.setString(2, "-");
                    }
                    stmtNewRole.executeUpdate();
                }
            }

        } catch (SQLException e6) {
            e6.printStackTrace();
        }
    }
}