package models.orders;

import models.products.Product;

import java.util.HashMap;

public abstract class Order {
    private int orderID;
    private int quantity;
    private int price;
    private HashMap<Product, Integer> listItems;

    public Order(int orderID, int quantity, int price, HashMap<Product, Integer> listItems) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.listItems = listItems;
    }

}
