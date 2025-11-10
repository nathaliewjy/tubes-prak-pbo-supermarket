package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;
import java.util.UUID;

public class Stocker extends Employee {

    public Stocker(int nik, int salary, Date hireDate, int workingHours, UUID userID, String name) {
        super(nik, salary, hireDate, workingHours, userID, name, Role.STOCKER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
