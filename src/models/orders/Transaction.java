package models.orders;
import java.util.UUID;

public class Transaction {
    private UUID transID;
    private UUID orderID;
    private double amountToPay;
    private PaymentMethod payMet;

    public Transaction(UUID orderID, double amountToPay, PaymentMethod payMet) {
        this.transID = UUID.randomUUID();
        this.amountToPay = amountToPay;
        this.payMet = payMet;
    }

    public UUID getTransID() {
        return this.transID;
    }

    public double getAmountToPay()  {
        return this.amountToPay;
    }

    public PaymentMethod getPaymentMethod() {
        return this.payMet;
    }

    @Override
    public String toString() {
        return this.transID + " " + this.amountToPay + " "  + " " + this.payMet;
    }
}

