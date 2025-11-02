package models.orders;

import models.products.Product;

import java.util.HashMap;

public abstract class Order {
    private int orderID;
    private int quantity;
    private double price;
    private HashMap<Product, Integer> listItems;

    public Order(int orderID, int quantity, double price, HashMap<Product, Integer> listItems) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.listItems = listItems;
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

    @Override
    public String toString() {
        return this.orderID + " " + this.quantity + " " + this.price + " " + this.listItems.keySet(); // pake keySet buat ngambil productnya aja
    }
}
