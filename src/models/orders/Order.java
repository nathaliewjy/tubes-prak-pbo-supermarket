package models.orders;

import models.products.Product;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class Order {
    private UUID orderID;
    private UUID memberID;
    private LocalDateTime orderDate;
    private HashMap<Product, Integer> listItems;
    private double totalPrice;
    

    public Order(UUID memberID, HashMap<Product, Integer> listItems) {
        this.orderDate = LocalDateTime.now();
        this.orderID = UUID.randomUUID();
        this.memberID = memberID;
        this.listItems = listItems;
    }

    public Order(UUID orderID, UUID memberID, LocalDateTime date, HashMap<Product, Integer> listItems, double totalPrice) {
        this.orderID = orderID;
        this.memberID = memberID;
        this.orderDate = date;
        this.listItems = listItems;
        this.totalPrice = totalPrice;
    }

    public UUID getOrderID() {
        return this.orderID;
    }

    public UUID getMemberID() {
        return this.memberID;
    }

    public HashMap<Product, Integer> getListItems() {
        return listItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return this.orderID + " " + this.memberID + " " + this.orderDate + " " + this.totalPrice;
    }
}
