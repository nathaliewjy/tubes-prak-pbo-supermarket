package models.orders;

public class Invoice {
    private int invID;
    private PaymentStatus status;

    public Invoice(int invID, PaymentStatus status) {
        this.invID = invID;
        this.status = status;
    }

}
