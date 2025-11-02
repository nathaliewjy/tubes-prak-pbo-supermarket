package models.orders;

import models.products.Product;

import java.sql.Date;
import java.util.HashMap;

public class Invoice {
    private int invID;
    private Transaction trans;
    private PaymentStatus status;
    private int quantity;
    private double totalPrice;
    private HashMap<Product, Integer> listItems;
    private Date transDate;
    private PaymentMethod payMet;


    public Invoice(int invID, Transaction trans, PaymentStatus status, int quantity, double totalPrice, HashMap<Product, Integer> listItems, Date transDate) {
        this.invID = invID;
        this.trans = trans;
        this.status = status;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.listItems = listItems;
        this.transDate = transDate;
        this.payMet = payMet;
    }


    public int getInvID() {
        return this.invID;
    }

    public void setInvID(int invID) {
        this.invID = invID;
    }

    public Transaction getTrans() {
        return this.trans;
    }

    public void setTrans(Transaction trans) {
        this.trans = trans;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public HashMap<Product, Integer> getListItems() {
        return this.listItems;
    }

    public void setListItems(HashMap<Product, Integer> listItems) {
        this.listItems = listItems;
    }

    public Date getTransDate() {
        return this.transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public PaymentMethod getPayMet() {
        return this.payMet;
    }

    public void setPayMet(PaymentMethod payMet) {
        this.payMet = payMet;
    }

    @Override
    public String toString() {
        return this.invID + " " + this.trans + "" + this.status + " " + this.quantity + " " + this.totalPrice + "" + this.listItems.keySet() + " " + this.transDate + " " + this.payMet;
    }

}
