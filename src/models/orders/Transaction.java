package models.orders;

import models.users.Customer;
import models.users.employees.Cashier;

import java.sql.Date;

public class Transaction {
    private int transID;
    private Customer cust;
    private Cashier cash;
    private Date transDate;
    private double totalPrice;
    private PaymentMethod payMet;

    public Transaction(int transID, Customer cust, Cashier cash, Date transDate, double totalPrice, PaymentMethod payMet) {
        this.transID = transID;
        this.cust = cust;
        this.cash = cash;
        this.transDate = transDate;
        this.totalPrice = totalPrice;
        this.payMet = payMet;
    }

    public int getTransID() {
        return this.transID;
    }

    public void setTransID(int transID) {
        this.transID = transID;
    }

    public Customer getCustomer() {
        return this.cust;
    }

    public void setCustomer(Customer cust) {
        this.cust = cust;
    }

    public Cashier getCashier() {
        return this.cash;
    }

    public void setCashier(Cashier cash) {
        this.cash = cash;
    }

    public Date getTransDate() {
        return this.transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public double getTotalPrice()  {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return this.payMet;
    }

    public void setPaymentMethod(PaymentMethod payMet) {
        this.payMet = payMet;
    }

    @Override
    public String toString() {
        return this.transID + " " +  this.cust.getName() + " " + this.cash.getName() + "" + this.transDate + "" + this.totalPrice + " " + this.payMet;
    }
}

