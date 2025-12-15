package strategies.payment;

public class CashPaymentStrategy implements IPaymentStrategy {
    private double cashGiven;
    private double change;

    public CashPaymentStrategy(double cashGiven) {
        this.cashGiven = cashGiven;
    }

    @Override
    public boolean processPayment(double totalAmount) {
        if (cashGiven >= totalAmount) {
            this.change = cashGiven - totalAmount;
            System.out.println("Paid with CASH. Change: " + change);
            return true;
        } else {
            System.out.println("Payment Failed: Insufficient Cash");
            return false;
        }
    }

    public double getChange() {
        return change;
    }
}