package controller;

import java.util.ArrayList;

public interface IPosController {
public void initializeSession();
public void endSession();
public boolean userAuthentication(String NIK);
public void createOrder(java.util.UUID memberUuid);
public void createTransaction(java.util.UUID orderID, double amountToPay, models.orders.PaymentMethod payMet);
}
