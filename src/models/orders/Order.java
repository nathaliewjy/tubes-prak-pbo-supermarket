package models.orders;

import models.products.Product;
import models.users.Customer;
import models.users.employees.Cashier;

import java.util.HashMap;

public abstract class Order {
    private int orderID;
    private int quantity;
    private double price;
    private HashMap<Product, Integer> listItems;
    private Customer cust;
    private Cashier cash;

    public Order(int orderID, int quantity, double price, HashMap<Product, Integer> listItems, Customer cust, Cashier cash) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.listItems = listItems;
        this.cust = cust;
        this.cash = cash;
    }

    public int getOrderID() {
        return this.orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity  = quantity;
    }

    public double getPrice()  {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public HashMap<Product, Integer> getListItems() {
        return this.listItems;
    }

    public void setListItems(HashMap<Product, Integer> listItems) {
        this.listItems = listItems;
    }

    public Customer getCust() {
        return this.cust;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public Cashier getCash() {
        return this.cash;
    }

    public void setCash(Cashier cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return this.orderID + " " + this.quantity + " " + this.price + " " + this.listItems.keySet() + " " + this.cust.getName(); // pake keySet buat ngambil productnya aja
    }
}
