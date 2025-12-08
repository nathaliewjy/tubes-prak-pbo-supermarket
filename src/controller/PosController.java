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
import repository.IEmployeeRepository;
import repository.IOrderRepository;
import repository.ITransactionRepository;

public class PosController implements IPosController {
    private IOrderRepository orderRepository;
    private ITransactionRepository transactionRepository;
    private IEmployeeRepository employeeRepository;
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
    public void initializeSession() {
        
    }

    @Override
    public void createOrder(UUID memberUuid) {
        HashMap<Product, Integer> listItems = new HashMap<>();

        Order m = new Order(memberUuid, listItems);
        double totalAmount = 0;
        for (Product p : m.getListItems().keySet()) {
            totalAmount += p.getPrice() * m.getListItems().get(p); // dapetin harga * quantity
        }
        m.setTotalPrice(totalAmount);
        orderRepository.addOrder(m);

    }

    @Override
    public void createTransaction(UUID orderID, double amountToPay, PaymentMethod payMet) {

        Transaction m = new Transaction(orderID, amountToPay, payMet);
        transactionRepository.addTransaction(m);
    }

    @Override
    public void endSession() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endSession'");
    }

    public double getStartingCashAmount() {
        return startingCashAmount;
    }

    public void setStartingCashAmount(double startingCashAmount) {
        this.startingCashAmount = startingCashAmount;
    }

    public double getCurrentCashAmount() {
        return currentCashAmount;
    }

    public void setCurrentCashAmount(double currentCashAmount) {
        this.currentCashAmount = currentCashAmount;
    }

}
