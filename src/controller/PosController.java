package controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import models.orders.Order;
import models.orders.PaymentMethod;
import models.orders.Transaction;
import models.products.Product;
import models.users.Employee;
import models.users.Members;
import repository.IEmployeeRepository;
import repository.IMembersRepository;
import repository.IOrderRepository;
import repository.IProductRepository;
import repository.ITransactionRepository;
import repository.MembersRepository;

public class PosController implements IPosController {
    private IOrderRepository orderRepository;
    private ITransactionRepository transactionRepository;
    private IEmployeeRepository employeeRepository;
    private IMembersRepository membersRepository;
    private IProductRepository productRepository;
    private HashMap<Product, Integer> currentCartItems = new HashMap<>();
    private UUID currentMemberID = null;
    private double startingCashAmount;
    private double currentCashAmount;

    public PosController(IOrderRepository orderRepository, ITransactionRepository transactionRepository,
            IEmployeeRepository employeeRepository) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean userAuthentication(String NIK) {
        Employee empToCheck = employeeRepository.findByNik(NIK);
        if (empToCheck == null) {
            return false;
        }
        if (empToCheck.getRole().toString().equals("CASHIER") || empToCheck.getRole().toString().equals("MANAGER")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void initializeSession(double startingCashAmount) {
        this.startingCashAmount = startingCashAmount;
        this.currentCashAmount = startingCashAmount;
    }

    @Override
    public void addMemberToSale(String phoneNumber) {
        Members m = membersRepository.findByPhone(phoneNumber);
        if (m != null) {
            this.currentMemberID = m.getUserID();
        }
    }

    @Override
    public void addItemToCart(String sku, int quantity) {
        Product p = productRepository.findProductBySKU(sku);

        if (p != null) {
            if (this.currentCartItems.containsKey(p)) {
                this.currentCartItems.put(p, this.currentCartItems.get(p) + quantity);
            } else {
                this.currentCartItems.put(p, quantity);
            }
        }
    }

    @Override
    public void addItemToCart(String sku) {
        addItemToCart(sku, 1);
    }

    @Override
    public void createOrder(UUID memberUuid) {
        Order currentOrder = new Order(memberUuid, this.currentCartItems);
        double totalAmount = 0;
        for (Product p : currentOrder.getListItems().keySet()) {
            totalAmount += p.getPrice() * currentOrder.getListItems().get(p); // dapetin harga * quantity
        }
        currentOrder.setTotalPrice(totalAmount);
        orderRepository.addOrder(currentOrder);
    }

    @Override
    public void createTransaction(UUID orderID, double amountToPay, PaymentMethod payMet) {
        Transaction m = new Transaction(orderID, amountToPay, payMet);
        transactionRepository.addTransaction(m);

    }

    @Override
    public void endSession() {
        
    }

    public double getStartingCashAmount() {
        return startingCashAmount;
    }

    public void setStartingCashAmount(double startingCashAmount) {
        this.startingCashAmount = startingCashAmount;
    }

    @Override
    public double getCurrentCashAmount() {
        return currentCashAmount;
    }

    public void setCurrentCashAmount(double currentCashAmount) {
        this.currentCashAmount = currentCashAmount;
    }

}
