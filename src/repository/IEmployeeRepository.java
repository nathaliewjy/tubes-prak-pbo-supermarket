package repository;

import models.users.Employee;

import java.util.ArrayList;

public interface IEmployeeRepository {

    Employee findByNik(String nik);

    void addEmployee(Employee e, String department);

    void deleteEmployee(String nik);

    ArrayList<Employee> getAllEmployee();
}
