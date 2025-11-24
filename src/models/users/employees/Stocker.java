package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.sql.Date;
import java.util.UUID;

public class Stocker extends Employee {

    public Stocker(int salary, Date hireDate, int workingHours, String nik, UUID userID, String name, Date deletedAt) {
        super(salary, hireDate, workingHours, nik, userID, name, Role.STOCKER, deletedAt);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
