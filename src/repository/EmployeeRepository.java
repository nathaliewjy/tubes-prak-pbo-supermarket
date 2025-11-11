package repository;

import java.util.ArrayList;

import models.users.Employee;

public class EmployeeRepository {
    private ArrayList<Employee> employeeList = new ArrayList<>();

    public Employee findByNIK(){
        
    }

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }
}
