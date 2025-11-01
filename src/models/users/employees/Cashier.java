package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;

public class Cashier extends Employee {

    public Cashier(int salary, Date hireDate, int workingHours, String userID, String name, Role role) {
        super(salary, hireDate, workingHours, userID, name, Role.CASHIER);
    }
}
