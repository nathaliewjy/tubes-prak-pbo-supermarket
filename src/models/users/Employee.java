package models.users;

import java.util.Date;
import java.util.UUID;

public abstract class Employee extends User {
    private int salary;
    private Date hireDate;
    private int workingHours;
    private String nik;

    public Employee(int salary, Date hireDate, int workingHours, String nik, UUID userID, String name, Role role) {
        super(name, role);
        this.salary = salary;
        this.hireDate = hireDate;
        this.workingHours = workingHours;
        this.nik = nik;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return this.hireDate;
    }

    public int getWorkingHours() {
        return this.workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public String getNik() {
        return this.nik;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.salary + " " + this.hireDate + " " + this.workingHours + " " + this.nik;
    }
}
