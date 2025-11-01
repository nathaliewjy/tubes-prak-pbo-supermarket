package models.users.employees;

import models.users.Employee;
import models.users.Role;

import java.util.Date;

public class Stocker extends Employee {

    public Stocker(int salary, Date hireDate, int workingHours, int userID, String name) {
        super(salary, hireDate, workingHours, userID, name, Role.STOCKER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
