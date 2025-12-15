package controller;

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
import strategies.payment.CashPaymentStrategy;
import strategies.payment.DebitPaymentStrategy;
import strategies.payment.IPaymentStrategy;
import strategies.payment.QrisPaymentStrategy;
import view.PosView;

public class PosController implements IPosController {
    private PosView view;
    private IOrderRepository orderRepository;
    private ITransactionRepository transactionRepository;
    private IEmployeeRepository employeeRepository;
    private IMembersRepository membersRepository;
    private IProductRepository productRepository;
    private HashMap<Product, Integer> currentCartItems = new HashMap<>();
    private Members currentMember = null;
    private double startingCashAmount;
    private double currentCashAmount;

    public PosController(PosView view, IOrderRepository orderRepo,
            ITransactionRepository transRepo, IEmployeeRepository empRepo, IMembersRepository memRepo,
            IProductRepository prodRepo) {
        this.view = view;
        this.membersRepository = memRepo;
        this.orderRepository = orderRepo;
        this.transactionRepository = transRepo;
        this.employeeRepository = empRepo;
        this.productRepository = prodRepo;
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
            this.currentMember = m;
            System.out.println("Member added: " + m.getName() + " (Points: " + m.getPoint() + ")");
            ((view.PosView) view).updateMemberInfo(m.getName(), m.getPoint());
        } else {
            System.out.println("Member not found.");
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
        ((view.PosView) view).updateCartTable(this.currentCartItems);
    }

    @Override
    public void addItemToCart(String sku) {
        addItemToCart(sku, 1);
    }

    private void createTransaction(UUID orderID, double amountToPay, PaymentMethod payMet) {
        Transaction m = new Transaction(orderID, amountToPay, payMet);
        transactionRepository.addTransaction(m);

    }

    @Override
    public void finalizeSale(double cashGiven, boolean usePoints) {
       
        double totalAmount = calculateCartTotal();

        
        double finalPrice = applyPoints(totalAmount, usePoints);

        
        CashPaymentStrategy strategy = new CashPaymentStrategy(cashGiven);

        if (!strategy.processPayment(finalPrice)) {
            view.showPaymentFailure("Insufficient Cash!");
            return;
        }

        
        processSuccessAndSave(finalPrice, PaymentMethod.CASH, strategy, usePoints);
    }

    //NON-CASH (QRIS / DEBIT) 
    @Override
    public void finalizeSale(PaymentMethod method, boolean usePoints) {
        
        double totalAmount = calculateCartTotal();

        
        double finalPrice = applyPoints(totalAmount, usePoints);

        IPaymentStrategy strategy;
        if (method == PaymentMethod.QRIS)
            strategy = new QrisPaymentStrategy();
        else if (method == PaymentMethod.DEBIT)
            strategy = new DebitPaymentStrategy();
        else {
            view.showPaymentFailure("Invalid Non-Cash Method");
            return;
        }

        if (!strategy.processPayment(finalPrice)) {
            view.showPaymentFailure("Payment Declined");
            return;
        }

       
        processSuccessAndSave(finalPrice, method, strategy, usePoints);
    }

   
    private void processSuccessAndSave(double amountPaid, PaymentMethod method, IPaymentStrategy strategy,
            boolean usePoints) {
       
        UUID memberUUID = (this.currentMember != null) ? this.currentMember.getUserID() : null;
        Order newOrder = new Order(memberUUID, this.currentCartItems);
        newOrder.setTotalPrice(amountPaid);
        orderRepository.addOrder(newOrder);

        
        createTransaction(newOrder.getOrderID(), amountPaid, method);

        
        for (Map.Entry<Product, Integer> entry : this.currentCartItems.entrySet()) {
            Product p = entry.getKey();
            p.setStockInShelf(p.getStockInShelf() - entry.getValue());
            productRepository.updateProductStock(p);
        }

        
        if (method == PaymentMethod.CASH) {
            this.currentCashAmount += amountPaid;
        }

        // E. Update Member Points
        if (this.currentMember != null) {
            int pointsEarned = (int) amountPaid / 100; // 1 point per 100 currency units
            if (usePoints) {
                int pointsToUse = (this.currentMember.getPoint() >= amountPaid) ? (int) amountPaid
                        : this.currentMember.getPoint();
                this.currentMember.setPoint(this.currentMember.getPoint() - pointsToUse + pointsEarned);
            } else {
                this.currentMember.setPoint(this.currentMember.getPoint() + pointsEarned);
            }
            membersRepository.updatePoints(memberUUID, currentMember.getPoint());
        }

        // F. Show Success View
        if (strategy instanceof CashPaymentStrategy) {
            view.showPaymentSuccess(((CashPaymentStrategy) strategy).getChange());
        } else {
            view.showPaymentSuccess();
        }

        // G. Reset
        this.currentCartItems.clear();
        this.currentMember = null;
    }

    // Helper to avoid duplication
    private double calculateCartTotal() {
        double total = 0;
        for (Product p : this.currentCartItems.keySet()) {
            total += p.getPrice() * this.currentCartItems.get(p);
        }
        return total;
    }

    // Helper for points
    private double applyPoints(double total, boolean usePoints) {
        if (usePoints && this.currentMember != null) {
            int points = this.currentMember.getPoint();
            int discount = (points >= total) ? (int) total : points;
            return total - discount;
        }
        return total;
    }

    @Override
    public boolean endSession(double actualEndingCash) {
        double difference = actualEndingCash - this.currentCashAmount;

        System.out.println("Session Ended.");
        System.out.println("System expects: " + this.currentCashAmount);
        System.out.println("Actual drawer: " + actualEndingCash);

        if (difference != 0) {
            System.out.println("DIFFERENCE DETECTED: " + difference);
            return false;
        }
        this.currentCashAmount = 0;
        this.startingCashAmount = 0;
        return true;
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

    @Override
    public int getMemberPoints() {
        if (currentMember != null) {
            return currentMember.getPoint();
        }
        return 0;
    }

}
