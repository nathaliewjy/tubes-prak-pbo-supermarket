package strategies.payment;

public class QrisPaymentStrategy implements IPaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Generating QR... " );
        System.out.println("Wating for server response... " );
        System.out.println("QRIS Payment of amount " + amount + " processed successfully.");
        return  true;
    }
}
