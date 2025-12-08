package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.sql.Date;
import java.util.UUID;

public class Manager extends Employee {
    private String department;

    public Manager(String department, int salary, Date hireDate, int workingHours, String nik, UUID userID, String name,
            Date deletedAt) {
        super(salary, hireDate, workingHours, nik, userID, name, Role.MANAGER, deletedAt);
        this.department = department;
    }

    public Manager(UUID userID, String name, Date deletedAt, int salary, Date hireDate, int workingHours, String nik,
            String department) {
        super(userID, name, Role.MANAGER, deletedAt, salary, hireDate, workingHours, nik);
        this.department = department;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.department;
    }
}
