package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;

public class Manager extends Employee {
    private String department;

    public Manager(String department, int salary, Date hireDate, int workingHours, int userID, String name) {
        super(salary, hireDate, workingHours, userID, name, Role.MANAGER);
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
