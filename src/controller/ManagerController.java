package controller;

import exception.InvalidInputException;
import models.jobdesk.RequestRestock;
import models.products.Product;
import models.users.Employee;
import models.users.Role;
import models.users.employees.Cashier;
import models.users.employees.Manager;
import models.users.employees.Stocker;
import repository.*;

import java.util.ArrayList;
import java.util.UUID;

public class ManagerController {
    private IEmployeeRepository employeeRepository;
    private IProductRepository productRepository;
    private IRequestRestockRepository requestRestockRepository;
    private ITransactionRepository transactionRepository;

    public ManagerController(IEmployeeRepository employeeRepository, IProductRepository productRepository, IRequestRestockRepository requestRestockRepository, ITransactionRepository transactionRepository) {
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.requestRestockRepository = requestRestockRepository;
        this.transactionRepository = transactionRepository;
    }

    public ArrayList<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployee();
    }

    public void hireEmployee(String name, Role role, int salary, int workingHours, String nik) throws InvalidInputException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Name kosong");
        }
        if (salary <= 0) {
            throw new InvalidInputException("Salary kosong");
        }
        if (workingHours < 0 || workingHours > 24) {
            throw new InvalidInputException("Working hours  kosong");
        }
        if (nik == null || nik.length() < 6) {
            throw new InvalidInputException("NIK kosong / kurang dari 6");
        }
        if (role == null) {
            throw new InvalidInputException("Role ksong");
        }

        Employee ada = employeeRepository.findByNik(nik);
        if (ada != null) {
            throw new InvalidInputException("NIK " + nik + " udah terdaftar");
        }

        java.sql.Date hireDate = new java.sql.Date(System.currentTimeMillis());

        Employee newEmp = null;

        switch (role) {
            case CASHIER:
                newEmp = new Cashier(name, salary, hireDate, workingHours, nik);
                break;
            case STOCKER:
                newEmp = new Stocker(name, salary, hireDate, workingHours, nik);
                break;
            case MANAGER:
                newEmp = new Manager(name, salary, hireDate, workingHours, nik, "-");
                break;
            default:
                throw new InvalidInputException("Role tidak valid");
        }

        employeeRepository.addEmployee(newEmp);
    }

    public void fireEmployee(String nik) throws InvalidInputException {
        if (nik == null || nik.length() != 6) {
            throw new InvalidInputException("NIK salah");
        }

        Employee e = employeeRepository.findByNik(nik);

        if (e == null) {
            throw new InvalidInputException("Employee not found");
        }

        employeeRepository.deleteEmployee(nik);
    }

    public void changeRole(String nik, Role newRole) throws InvalidInputException {
        if (nik == null || nik.length() < 6) {
            throw new InvalidInputException("NIK kosong");
        }
        if (newRole == null) {
            throw new InvalidInputException("New role kosong");
        }

        Employee e = employeeRepository.findByNik(nik);

        if (e == null) {
            throw new InvalidInputException("Employee not found");
        }

        Role oldRole = e.getRole();

        if (oldRole == newRole) {
            throw new InvalidInputException("Old role sama dengan new role");
        }

        employeeRepository.changeRole(e.getUserID(), oldRole, newRole);
    }

    public void assignRestock(String managerNik, String stockerNik, String productSku, int quantity) throws InvalidInputException {
        if (managerNik == null || managerNik.length() < 6) {
            throw new InvalidInputException("NIK Manager kosong");
        }
        if (stockerNik == null || stockerNik.length() < 6) {
            throw new InvalidInputException("NIK Stocker kosong");
        }
        if (productSku == null) {
            throw new InvalidInputException("SKU kosong");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity harus > 0");
        }

        Employee manager = employeeRepository.findByNik(managerNik);
        if (manager == null || manager.getRole() != Role.MANAGER) {
            throw new InvalidInputException("Manager not found");
        }

        Employee stocker = employeeRepository.findByNik(stockerNik);
        if (stocker == null || stocker.getRole() != Role.STOCKER) {
            throw new InvalidInputException("Stocker not found");
        }

        Product product = productRepository.findProductBySKU(productSku);
        if (product == null) {
            throw new InvalidInputException("Product not found");
        }

        RequestRestock req = new RequestRestock(product.getProdID(), quantity, manager.getUserID());
        req.setStockerID(stocker.getUserID());

        requestRestockRepository.createRequest(req);
    }

    public ArrayList<RequestRestock> monitorAllRestock() throws InvalidInputException {
        ArrayList<RequestRestock> reqList = requestRestockRepository.getAllRequests();

        if (reqList.isEmpty()) {
            throw new InvalidInputException("G ada request restock");
        }

        return reqList;
    }

    // ini jadinya ambil dari total pendapatan per order
    public double monitorTotalPendapatan() throws InvalidInputException {
        return transactionRepository.calculateTotalRevenue();
    }

    // total barang terjual --> semua brand yg udah pernah dibeli cust (tabel OrderProducts)
    public int monitorTotalBarang() throws InvalidInputException {
        ArrayList<Product> prods = productRepository.getAllProducts();

        if (prods.isEmpty()) {
            throw new InvalidInputException("Product kosong");
        }

        int totalBarang = 0;

        for (Product p : prods) {
            totalBarang += p.getStockInShelf() + p.getStockInStorage();
        }

        return totalBarang;
    }

    public Employee loginManager(String nik) throws InvalidInputException {
        if (nik == null || nik.length() < 6) {
            throw new InvalidInputException("NIK salah");
        }

        Employee emp = employeeRepository.findByNik(nik);

        if (emp == null) {
            throw new InvalidInputException("Emp not found");
        }

        if (emp.getRole() != Role.MANAGER) {
            throw new InvalidInputException("Bukan manager");
        }

        return emp;
    }
}
