package models.orders;

import models.products.Product;
import models.users.Member;
import models.users.employees.Cashier;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class Order {
    private UUID orderID;
    private Member mem;
    private Date orderDate;
    private double totalAmount;

    public Order(UUID orderID, Member mem, Date orderDate, double totalAmount) {
        this.orderID = orderID;
        this.mem = mem;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public UUID getOrderID() {
        return this.orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public Member getMem() {
        return this.mem;
    }

    public void setMem(Member mem) {
        this.mem = mem;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return this.orderID + " " + this.mem.getName() + " " + this.orderDate + " " + this.totalAmount;
    }
}
