package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.sql.Date;
import java.util.UUID;

public class Manager extends Employee {
    private String department;

    // cerate new
    public Manager(String name, int salary, Date hireDate, int workingHours, String nik, String department) {
        super(name, Role.MANAGER, salary, hireDate, workingHours, nik);
        this.department = department;
    }

    // amvil dari db
    public Manager(UUID userID, String name, Date deletedAt, int salary, Date hireDate, int workingHours, String nik, String department) {
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
