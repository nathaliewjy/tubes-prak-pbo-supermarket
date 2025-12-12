package strategies.payment;

public class DebitPaymentStrategy implements IPaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Debit Card Payment of amount " + amount + "...");
        System.out.println("Verifying with bank server...");
        System.out.println("Debit Payment of amount " + amount + " processed successfully.");
        return true;
    }
}
