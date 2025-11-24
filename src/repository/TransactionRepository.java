package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import models.orders.PaymentMethod;
import models.orders.Transaction;
import util.Database;

public class TransactionRepository implements ITransactionRepository {
    Connection conn = Database.connect();


    public void findByDate(String ddmmyy){
        conn = Database.connect();
        PreparedStatement pstmt = null;
        
    }
    public void addTransaction(Transaction m, String TransactionType, String orderID) {
        conn = Database.connect();
        PreparedStatement pstmt = null;

        String sqlTransaction = "INSERT INTO transaction(TransID,OrderID,TransDate,TotalPrice,PaymentMethod,TransactionType) VALUES(?,?,current_timestamp(),?,?,?)";

        String transID = UUID.randomUUID().toString();
        String fkOrderID = orderID;
        try {
            pstmt = conn.prepareStatement(sqlTransaction);
            pstmt.setString(1, transID);
            pstmt.setString(2, fkOrderID);
            pstmt.setDouble(3, m.getAmountToPay());
            pstmt.setString(4, m.getPaymentMethod().toString());
            pstmt.setString(5, TransactionType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.getSQLState();
        }
    }

    public ArrayList<Transaction> getTransactionList() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        String sqlTransaction = "SELECT * FROM transaction";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = Database.connect();
            pstmt = conn.prepareStatement(sqlTransaction);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // UUID transID = UUID.fromString(rs.getString("TransID"));
                UUID orderID = UUID.fromString(rs.getString("OrderID"));
                // java.sql.Date transDate = rs.getDate("TransDate");
                double totalPrice = rs.getDouble("TotalPrice");
                String paymentMethod = rs.getString("PaymentMethod");

                Transaction transaction = new Transaction(orderID, totalPrice, PaymentMethod.valueOf(paymentMethod));
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.getSQLState();
        }

        return transactionList;
    }

}
