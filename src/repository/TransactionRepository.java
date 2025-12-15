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

    public void findByDate(String ddmmyy) {
        conn = Database.connect();
        PreparedStatement pstmt = null;

    }

    public void addTransaction(Transaction m) {
        conn = Database.connect();
        PreparedStatement pstmt = null;

        String sqlTransaction = "INSERT INTO transaction(TransID,OrderID,TransDate,TotalPrice,PaymentMethod) VALUES(?,?,?,?,?)";

        String transID = UUID.randomUUID().toString();
        String fkOrderID = m.getOrderID().toString();
        try {
            pstmt = conn.prepareStatement(sqlTransaction);
            pstmt.setString(1, transID);
            pstmt.setString(2, fkOrderID);
            pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setDouble(4, m.getAmountToPay());
            pstmt.setString(5, m.getPaymentMethod().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.getSQLState();
        }
    }

    public ArrayList<Transaction> getTransactionList() { // return list Transaction(OrderID, TotalPrice, PaymentMethod)
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


    @Override
    public double calculateTotalRevenue() {
        String sql = "SELECT SUM(TotalPrice) AS TotalPendapatan FROM `transaction`";

        double total = 0;
        try (Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("TotalPendapatan");
            }

        } catch (SQLException e) {
            System.out.println("Gagal hitung revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
}
