package controller;

import java.util.HashMap;
import java.util.Map;

import models.orders.Order;
import models.orders.Transaction;
import models.products.Product;
import repository.OrderRepository;
import repository.TransactionRepository;

public class PosController {
    private OrderRepository orderRepository;
    private TransactionRepository transactionRepository;

    public PosController(OrderRepository orderRepository, TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }


    public void createOrder(Order order, String transactionType, Transaction transaction) { 
        orderRepository.addOrder(order);
        transactionRepository.addTransaction(transaction, transactionType, order.getOrderID().toString());
    }

    public void addOrder(Order m) {
        double totalAmount = 0;
        for (Product p : m.getListItems().keySet()) {
            totalAmount += p.getPrice() * m.getListItems().get(p); // dapetin harga * quantity
        }

        m.setTotalPrice(totalAmount);
        orderRepository.addOrder(m);
    }

    public void addTransaction(Transaction m , String TransactionType, String orderID) {
        transactionRepository.addTransaction(m,TransactionType, orderID);
    }

}
