package models.orders;

import java.sql.Date;
import java.util.UUID;

public class Transaction {
    private UUID transID;
    private Order order;
    private Date transDate;
    private double totalPrice;
    private PaymentMethod payMet;
    private TransactionType transType;

    public Transaction(UUID transID, Order order, Date transDate, double totalPrice, PaymentMethod payMet, TransactionType transType) {
        this.transID = transID;
        this.order =  order;
        this.transDate = transDate;
        this.totalPrice = totalPrice;
        this.payMet = payMet;
        this.transType = transType;
    }

    public UUID getTransID() {
        return this.transID;
    }

    public void setTransID(UUID transID) {
        this.transID = transID;
    }

    public Order getOrder()  {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public TransactionType getTransType() {
        return this.transType;
    }

    public void setTransType(TransactionType transType) {
        this.transType = transType;
    }

    @Override
    public String toString() {
        return this.transID + "" + this.transDate + "" + this.totalPrice + " " + this.payMet;
    }
}

