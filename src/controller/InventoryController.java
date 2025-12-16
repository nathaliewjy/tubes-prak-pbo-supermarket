package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Exception.InvalidInputException;
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

    public boolean Login(String NIK) {
        Employee employeeToCheck = employeeRepository.findByNik(NIK);
        if (employeeToCheck != null && employeeToCheck.getRole() == Role.STOCKER) {
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
            int quantityOrdered = listItems.get(p);
            p.setStockInShelf(p.getStockInShelf() - quantityOrdered);
            p.setStockInStorage(p.getStockInStorage() - quantityOrdered);
            productRepository.updateProductStock(p);
        }
    }

    // update storage stock 
    public boolean updateStorageStock(String name, int newStorageStock) {
        Product product = productRepository.getProductByName(name);

        if (product != null) {
            productRepository.updateProductStorageStock(product, newStorageStock);
            return true;
        } else {
            return false;
        }
    }

    // Update stock buat RestockNeeded
    // jadi StockInshelf = stockShelf + qtyshelfToAdd
    // stockInStorage = StockInStorage - qtyshelfToAdd
    public boolean updateStockShelf(String name, int qtyShelfToAdd) throws InvalidInputException {
        Product product = productRepository.getProductByName(name);

        if (product == null) {
            throw new InvalidInputException("Product not found");
        }

        if (product.getStockInStorage() < qtyShelfToAdd) {
            throw new InvalidInputException("Stock in storage not enough! Available: " + product.getStockInStorage());
        }

        

        productRepository.updateProductShelfStock(product, qtyShelfToAdd); // update stock di shelf

        return true;
    }

    // Complete request restock
    public boolean completeRestockRequest(String requestID) throws InvalidInputException {
        UUID reqUUID;

        try {
            reqUUID = UUID.fromString(requestID);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("format id requestid salah");
        }

        RequestRestock request = requestRestockRepository.getRequestById(reqUUID);

        if (request == null) {
            throw new InvalidInputException("Request not found");
        }

        Product product = productRepository.findProductById(request.getProductID());

        if (product == null) {
            throw new InvalidInputException("Product not found");
        }

        if (product.getStockInStorage() < request.getQuantityToRestock()) { // ngecek stok storage cukup atau engga
            throw new InvalidInputException(
                    "Stock in storage not enough! Available: " +
                            product.getStockInStorage() +
                            ", Needed: " +
                            request.getQuantityToRestock());
        }

        product.setStockInShelf(product.getStockInShelf() + request.getQuantityToRestock());
        product.setStockInStorage(product.getStockInStorage() - request.getQuantityToRestock());
        productRepository.updateProductStock(product);

        // update status
        requestRestockRepository.updateStatus(
                request.getRequestID(),
                RequestStatus.COMPLETED);

        return true;
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

    public ArrayList<Product> checkEmptyStock() {
        return productRepository.checkEmptyStock();
    }

    public String getProductNameById(UUID productID) {
        Product product = productRepository.findProductById(productID);
        return product != null ? product.getBrand() : "Unknown";
    }
}