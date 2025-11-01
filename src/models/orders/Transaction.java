package models.orders;

import java.sql.Date;

public class Transaction {
    private int transID;
    private Date transDate;
    private int totalPrice;
    private PaymentMethod paymentMethod;

    public Transaction(int transID, Date transDate, int totalPrice, PaymentMethod paymentMethod) {
        this.transID = transID;
        this.transDate = transDate;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }
}
