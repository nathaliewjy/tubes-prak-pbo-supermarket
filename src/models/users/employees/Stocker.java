package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.sql.Date;
import java.util.UUID;

public class Stocker extends Employee {

    // create new
    public Stocker(String name, int salary, Date hireDate, int workingHours, String nik) {
        super(name, Role.STOCKER, salary, hireDate, workingHours, nik);
    }

    // ambil dari db
    public Stocker(UUID userID, String name, Date deletedAt, int salary, Date hireDate,
                   int workingHours, String nik) {
        super(userID, name, Role.STOCKER, deletedAt, salary, hireDate, workingHours, nik);
    }



    @Override
    public String toString() {
        return super.toString();
    }
}
