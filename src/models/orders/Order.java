package models.orders;

import models.products.Product;
import models.users.Members;
import models.users.employees.Cashier;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public class Order {
    private UUID orderID;
    private UUID memberID;
    private Date orderDate;
    private double totalPrice;
    private HashMap<Product, Integer> listItems;


    public Order(UUID memberID, Date orderDate, HashMap<Product, Integer> listItems) {
        this.orderID = UUID.randomUUID();
        this.memberID = memberID;
        this.listItems = listItems;
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

    @Override
    public String toString() {
        return this.orderID + " " + this.memberID + " " + this.orderDate + " " + this.totalPrice;
    }
}
