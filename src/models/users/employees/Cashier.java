package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.sql.Date;
import java.util.UUID;

public class Cashier extends Employee {

    // create new
    public Cashier(String name, int salary, Date hireDate, int workingHours, String nik) {
        super(name, Role.CASHIER, salary, hireDate, workingHours, nik);
    }

    // ambil dari db
    public Cashier(UUID userID, String name, Date deletedAt, int salary, Date hireDate, int workingHours, String nik) {
        super(userID, name, Role.CASHIER, deletedAt, salary, hireDate, workingHours, nik);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}