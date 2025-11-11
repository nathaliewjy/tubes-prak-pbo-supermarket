package models.users;

import java.util.Date;
import java.util.UUID;

public abstract class Employee extends User {
    private int salary;
    private Date hireDate;
    private int workingHours;
    private int nik;

    public Employee(int salary, Date hireDate, int workingHours, int nik, UUID userID, String name, Role role) {
        super(userID, name, role);
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

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public int getWorkingHours() {
        return this.workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public int getNik() {
        return this.nik;
    }

    public void setNik(int nik) {
        this.nik = nik;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.salary + " " + this.hireDate + " " + this.workingHours + " " + this.nik;
    }
}
