package controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import models.jobdesk.RequestRestock;
import models.jobdesk.RequestStatus;
import models.products.Product;
import models.products.ProductCategory;
import models.users.Employee;
import models.users.Role;
import repository.IEmployeeRepository;
import repository.IProductRepository;
import repository.IRequestRestockRepository;

public class InventoryController {

    private IEmployeeRepository employeeRepository;
    private IProductRepository productRepository;
    private IRequestRestockRepository requestRestockRepository;
    private Employee emp;
    public InventoryController(IProductRepository productRepository,
            IRequestRestockRepository requestRestockRepository, IEmployeeRepository employeeRepository) {
        this.productRepository = productRepository;
        this.requestRestockRepository = requestRestockRepository;
        this.employeeRepository = employeeRepository;
    }

    public boolean Login(String NIK){
        Employee employeeToCheck = employeeRepository.findByNik(NIK);
        if(employeeToCheck != null && employeeToCheck.getRole() == Role.STOCKER){
            this.emp = employeeToCheck;
            return true;
        }
        return false;

    }
    public Employee getCurrentEmployee() {
        return this.emp;
    }

    public ArrayList<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.getProductsByCategory(category);
    }

    public ArrayList<Product> getAllExpiredProducts(int days) {
        return productRepository.getExpiredProducts(days);
    }

    public void updateStockAfterOrder(HashMap<Product, Integer> listItems) {
        for (Product p : listItems.keySet()) {
            int quantityOrdered = listItems.get(p); // dapetin quantity yang dipesan
            p.setStockInShelf(p.getStockInShelf() - quantityOrdered); /// pengurangan
            p.setStockInStorage(p.getStockInStorage() - quantityOrdered);
            productRepository.updateProductStock(p); // update di repository
        }
    }

    public boolean updateStockByName(String name, int newStockInShelf, int newStockInStorage) {
        Product product = productRepository.getProductByName(name);

        if (product != null) {
            product.setStockInShelf(newStockInShelf);
            product.setStockInStorage(newStockInStorage);
            productRepository.updateProductStock(product);
            requestRestockRepository.updateStatus(product.getProdID(), RequestStatus.COMPLETED);
            // bikin request restock jadi Successfull
            return true;
        } else {
            return false;
        }
    }

   
    public ArrayList<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    public ArrayList<RequestRestock> checkMyRequest(UUID stockerID) {
        return requestRestockRepository.getPendingRequest(stockerID);
    }

    public ArrayList<RequestRestock> getAllRequests() {
        return requestRestockRepository.getAllRequests();
    }

   

}
