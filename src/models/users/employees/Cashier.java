package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;
import java.util.UUID;

public class Cashier extends Employee {

    public Cashier(int salary, Date hireDate, int workingHours, int nik, UUID userID, String name) {
        super(salary, hireDate, workingHours, nik, userID, name, Role.CASHIER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
