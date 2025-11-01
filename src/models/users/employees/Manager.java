package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;

public class Manager extends Employee {
    private String department;

    public Manager(String department, int salary, Date hireDate, int workingHours, String userID, String name, Role role) {
        super(salary, hireDate, workingHours, userID, name, Role.MANAGER);
        this.department = department;
    }
}
