package models.users;

import java.util.Date;

public abstract class Employee extends User {
    private int nik;
    private int salary;
    private Date hireDate;
    private int workingHours;

    public Employee(int nik, int salary, Date hireDate, int workingHours, int userID, String name, Role role) {
        super(userID, name, role);
        this.nik = nik;
        this.salary = salary;
        this.hireDate = hireDate;
        this.workingHours = workingHours;
    }

    public int getNik() {
        return this.nik;
    }

    public void setNik(int nik) {
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

    @Override
    public String toString() {
        return super.toString() + " " + this.salary + " " + this.hireDate + " " + this.workingHours;
    }
}
