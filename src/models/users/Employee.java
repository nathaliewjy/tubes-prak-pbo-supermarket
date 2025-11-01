package models.users;

import java.util.Date;

public abstract class Employee extends User {
    private int salary;
    private Date hireDate;
    private int workingHours;

    public Employee(int salary, Date hireDate, int workingHours, int userID, String name, Role role) {
        super(userID, name, role);
        this.salary = salary;
        this.hireDate = hireDate;
        this.workingHours = workingHours;
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
