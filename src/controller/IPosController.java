package controller;

import java.util.ArrayList;

public interface IPosController {
    public boolean userAuthentication(String NIK);
public void initializeSession(double startingCashAmount);
public void addItemToCart(String sku);
public void addItemToCart(String sku, int quantity);
public void addMemberToSale(String phoneNumber);
public void createOrder(java.util.UUID memberUuid);
public void createTransaction(java.util.UUID orderID, double amountToPay, models.orders.PaymentMethod payMet);
public double getCurrentCashAmount();
public void endSession();
}
