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
        ((view.PosView) view).updateMemberInfo(phoneNumber, getMemberPoints());
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
    public void finalizeSale(double amountPaid, PaymentMethod payMet, boolean usePoints) {

        double totalAmount = 0;
        // ngitung total dari product" yang ada di cart
        for (Product p : this.currentCartItems.keySet()) {
            totalAmount += p.getPrice() * this.currentCartItems.get(p);
        }
        // pemakaian point
        int pointsUsed = 0;
        if (usePoints && this.currentMember != null) {
            int memberPoints = this.currentMember.getPoint();

            // 1 Point = Rp 1 Discount
            if (memberPoints >= totalAmount) {
                pointsUsed = (int) totalAmount;
            } else {
                pointsUsed = memberPoints;
            }

            totalAmount = totalAmount - pointsUsed;
            System.out.println("Points used: " + pointsUsed + ". New Total: " + totalAmount);
            ((view.PosView) view).updateTotalAmount(totalAmount);
        }

        IPaymentStrategy strategy;

        switch (payMet) {
            case CASH:
                strategy = new CashPaymentStrategy(amountPaid);
                break;
            case QRIS:
                strategy = new QrisPaymentStrategy();
                break;
            case DEBIT:
                strategy = new DebitPaymentStrategy();
                break;
            default:
                System.out.println("Invalid Payment Method");
                return;
        }

        boolean isSuccess = strategy.processPayment(totalAmount);

        if (!isSuccess) {
            System.out.println("Transaction Aborted.");
            return;
        }

        // Create Order
        UUID memberUUID = (this.currentMember != null) ? this.currentMember.getUserID() : null;
        Order newOrder = new Order(memberUUID, this.currentCartItems);
        newOrder.setTotalPrice(totalAmount);
        orderRepository.addOrder(newOrder);

        // Create Transaction
        createTransaction(newOrder.getOrderID(), totalAmount, payMet);

        // Update Inventory
        for (Map.Entry<Product, Integer> entry : this.currentCartItems.entrySet()) {
            Product p = entry.getKey();
            p.setStockInShelf(p.getStockInShelf() - entry.getValue());
            productRepository.updateProductStock(p);
        }
        // validasi akhir
        if (payMet == PaymentMethod.CASH) {
            this.currentCashAmount += totalAmount;
        }

        // Point Handling (penambahan dan pengurungan point)
        if (this.currentMember != null) {

            if (pointsUsed > 0) {
                membersRepository.updatePoints(this.currentMember.getUserID(), -pointsUsed);
            }

            int pointsEarned = (int) (totalAmount / 100); // 1 point setiap Rp 100
            membersRepository.updatePoints(this.currentMember.getUserID(), pointsEarned);

            System.out.println("Points update: Used " + pointsUsed + ", Earned " + pointsEarned);
        }

        // Reset
        this.currentCartItems.clear();
        this.currentMember = null;
        // ((view.PosView) view).resetSaleView();
        System.out.println("Sale Finalized!");
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
