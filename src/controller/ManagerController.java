package controller;

import models.jobdesk.RequestRestock;
import models.products.Product;
import models.users.Employee;
import models.users.Role;
import models.users.employees.Cashier;
import models.users.employees.Manager;
import models.users.employees.Stocker;
import repository.IEmployeeRepository;
import repository.IPresensiRepository;
import repository.IProductRepository;
import repository.IRequestRestockRepository;

import java.util.ArrayList;
import java.util.UUID;

import Exception.InvalidInputException;

public class ManagerController {
    private IEmployeeRepository employeeRepository;
    private IPresensiRepository presensiRepository;
    private IProductRepository productRepository;
    private IRequestRestockRepository requestRestockRepository;

    public ManagerController(IEmployeeRepository employeeRepository, IPresensiRepository presensiRepository, IProductRepository productRepository, IRequestRestockRepository requestRestockRepository) {
        this.employeeRepository = employeeRepository;
        this.presensiRepository = presensiRepository;
        this.productRepository = productRepository;
        this.requestRestockRepository = requestRestockRepository;
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
        if (nik == null || nik.length() < 16) {
            throw new InvalidInputException("NIK kosong / kurang dari 16");
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
        if (nik == null || nik.length() != 16) {
            throw new InvalidInputException("NIK salah");
        }

        Employee e = employeeRepository.findByNik(nik);

        if (e == null) {
            throw new InvalidInputException("Employee not found");
        }

        employeeRepository.deleteEmployee(nik);
    }

    public void changeRole(UUID employeeID, Role newRole) throws InvalidInputException {
        if (employeeID == null) {
            throw new InvalidInputException("ID kosong");
        }
        if (newRole == null) {
            throw new InvalidInputException("New role kosong");
        }

        Employee e = employeeRepository.findById(employeeID);

        if (e == null) {
            throw new InvalidInputException("Employee not found");
        }

        Role oldRole = e.getRole();

        if (oldRole == newRole) {
            throw new InvalidInputException("Old role sama dengan new role");
        }

        employeeRepository.changeRole(employeeID, oldRole, newRole);
    }

    public double calculateSalary(UUID employeeID) throws InvalidInputException {
        if (employeeID == null) {
            throw new InvalidInputException("EmployeeID cant be null");
        }

        Employee e = employeeRepository.findById(employeeID);

        if (e == null) {
            throw new InvalidInputException("EMployee not found");
        }

        int totalPres = presensiRepository.countPresensi(employeeID);
        double salaryPerDay = e.getSalary() / 30.0;
        double salaryAkhir = salaryPerDay * totalPres;

        return salaryAkhir;
    }

    public void assignRestock(UUID managerID, UUID stockerID, UUID productID, int quantity) throws InvalidInputException {
        if (managerID == null) {
            throw new InvalidInputException("ManagerID kosong");
        }
        if (stockerID == null) {
            throw new InvalidInputException("StockerID kosong");
        }
        if (productID == null) {
            throw new InvalidInputException("ProductID kosong");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity harus > 0");
        }

        Employee manager = employeeRepository.findById(managerID);
        if (manager == null || manager.getRole() != Role.MANAGER) {
            throw new InvalidInputException("Manager not found");
        }

        Employee stocker = employeeRepository.findById(stockerID);
        if (stocker == null || stocker.getRole() != Role.STOCKER) {
            throw new InvalidInputException("Stocker not found");
        }

        Product product = productRepository.findProductById(productID);
        if (product == null) {
            throw new InvalidInputException("Product not found");
        }

        RequestRestock req = new RequestRestock(productID, quantity, managerID);
        req.setStockerID(stockerID);

        requestRestockRepository.createRequest(req);
    }

    public ArrayList<RequestRestock> monitorAllRestock() throws InvalidInputException {
        ArrayList<RequestRestock> reqList = requestRestockRepository.getAllRequests();

        if (reqList.isEmpty()) {
            throw new InvalidInputException("G ada request restock");
        }

        return reqList;
    }

    public double monitorTotalUang() throws InvalidInputException {
        ArrayList<Product> prods = productRepository.getAllProducts();

        if (prods.isEmpty()) {
            throw new InvalidInputException("Product kosong");
        }

        double totalUang = 0;

        for (Product p : prods) {
            int stock = p.getStockInShelf() + p.getStockInStorage();
            double uang = stock * p.getPrice();

            totalUang += uang;
        }

        return totalUang;
    }

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
}