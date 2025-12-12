package repository;

import models.users.Employee;
import models.users.Role;

import java.util.ArrayList;
import java.util.UUID;

public interface IEmployeeRepository {

    Employee findByNik(String nik);

    void addEmployee(Employee e);
    void updateEmployee(Employee e);
    void changeRole(UUID employeeID, Role oldRole, Role newRole);
    void updateJobdesk(UUID employeeID, String jobdesk);
    Employee findById(UUID employeeID);

    void deleteEmployee(String nik);

    ArrayList<Employee> getAllEmployee();
}
