package repository;

import java.util.ArrayList;

import models.orders.Transaction;

public interface ITransactionRepository {
    public void addTransaction(Transaction m, String TransactionType, String orderID);
    public ArrayList<Transaction> getTransactionList();
    public void findByDate(String ddmmyy);
    // ini ditambah nathalie
    double calculateTotalRevenue();
}
