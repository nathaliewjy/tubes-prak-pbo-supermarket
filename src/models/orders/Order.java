package models.orders;

import models.products.Product;
import models.users.Member;
import models.users.employees.Cashier;

import java.util.HashMap;
import java.util.UUID;

public abstract class Order {
    private UUID orderID;
    private int quantity;
    private double price;
    private HashMap<Product, Integer> listItems;
    private Member mem;
    private Cashier cash;

    public Order(UUID orderID, int quantity, double price, HashMap<Product, Integer> listItems, Member mem, Cashier cash) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.listItems = listItems;
        this.mem = mem;
        this.cash = cash;
    }

    public UUID getOrderID() {
        return this.orderID;
    }

    public void setOrderID(UUID orderID) {
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

    public Member getMem() {
        return this.mem;
    }

    public void setMem(Member mem) {
        this.mem = mem;
    }

    public Cashier getCash() {
        return this.cash;
    }

    public void setCash(Cashier cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return this.orderID + " " + this.quantity + " " + this.price + " " + this.listItems.keySet() + " " + this.mem.getName(); // pake keySet buat ngambil productnya aja
    }
}
