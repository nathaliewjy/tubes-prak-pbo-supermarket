package controller;

import java.util.ArrayList;

import models.orders.PaymentMethod;

public interface IPosController {
    public boolean userAuthentication(String NIK);

    public void initializeSession(double startingCashAmount);

    public void addItemToCart(String sku);

    public void addItemToCart(String sku, int quantity);

    public void addMemberToSale(String phoneNumber);

    public void finalizeSale(double amountPaid, boolean usePoints);

    public void finalizeSale(PaymentMethod payMet, boolean usePoints);

    public double getCurrentCashAmount();

    public boolean endSession(double actualEndingCash);

    public int getMemberPoints();
}
