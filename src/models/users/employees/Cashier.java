package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;
import java.util.UUID;

public class Cashier extends Employee {

    public Cashier(int nik, int salary, Date hireDate, int workingHours, UUID userID, String name) {
        super(nik, salary, hireDate, workingHours, userID, name, Role.CASHIER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
