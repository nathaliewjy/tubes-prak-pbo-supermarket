package models.users;

import java.util.Date;

public class Employee extends User {
    private int salary;
    private Date hireDate;
    private int workingHours;

    public Employee(int salary, Date hireDate, int workingHours, String userID, String name, Role role) {
        super(userID, name, role);
        this.salary = salary;
        this.hireDate = hireDate;
        this.workingHours = workingHours;
    }
}
