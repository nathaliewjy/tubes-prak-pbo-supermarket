package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import models.orders.Order;
import models.products.Product;
import models.products.ProductCategory;
import util.Database;

public class OrderRepository implements IOrderRepository {
    Connection conn = Database.connect();

    public OrderRepository() {

    }

    @Override
    public void addOrder(Order order) {
        conn = Database.connect();
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        String sqlIntoOrders = "INSERT INTO orders(OrderID,MemberID,OrderDate,TotalAmount) VALUES(?,?,?,?)";
        String sqlIntoOrdersProduct = "INSERT INTO order_product(OrderID,ProductID,Quantity) VALUES(?,?,?)";

        try {
            pstmt = conn.prepareStatement(sqlIntoOrders);
            pstmt.setString(1, order.getOrderID().toString());
            if (order.getMemberID() != null) {
                pstmt.setString(2, order.getMemberID().toString());
            } else {
                pstmt.setNull(2, java.sql.Types.NULL);
            }
            pstmt.setObject(3, order.getOrderDate());
            pstmt.setDouble(4, order.getTotalPrice());
            pstmt.executeUpdate(); // insert ke table orders
            pstmt2 = conn.prepareStatement(sqlIntoOrdersProduct);
            for (Map.Entry<Product, Integer> entry : order.getListItems().entrySet()) {
                // buat dapetin quantity dari suatu produk
                // ngeloop tiap produk yang dipesan dalam order,
                // trs ngambil quantity tiap produk
                // quantity bwt isi tabel order_product.quantity
                Product p = entry.getKey();
                Integer quantity = entry.getValue();
                pstmt2.setString(1, order.getOrderID().toString());
                pstmt2.setString(2, p.getProdID().toString());
                pstmt2.setInt(3, quantity);
                pstmt2.executeUpdate();
            }
            pstmt.close();
            pstmt2.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Override
    public HashMap<Product, Integer> getOrderItems(UUID orderID) {
        String sql = "SELECT p.ProductID FROM order_product JOIN orders ON orders.OrderID = order_product.OrderID JOIN products p ON p.ProductID = order_product.ProductID WHERE orders.OrderID = ?";
        PreparedStatement pstmt = null;
        HashMap<Product, Integer> listItems = new HashMap<>();
        try {
            conn = Database.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(orderID));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("SKU"),
                        rs.getString("Brand"),
                        rs.getString("Category").equals("FOOD") ? ProductCategory.FOOD : ProductCategory.BEVERAGE,
                        rs.getDouble("PRICE"),
                        rs.getInt("StockInStorage"),
                        rs.getInt("StockInShelf"),
                        rs.getDate("ManufactureDate"),
                        rs.getDate("ExpiryDate"));
                int quantity = rs.getInt("Quantity");
                listItems.put(product, quantity);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return listItems;

    }

    @Override
    public ArrayList<Order> getOrderList() { // masukin semua order ke arrayList
        ArrayList<Order> orderList = new ArrayList<>();
        String sqlForOrders = "SELECT * FROM orders";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Database.connect();
            pstmt = conn.prepareStatement(sqlForOrders);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UUID orderID = UUID.fromString(rs.getString("OrderID")); // dri string ke uuid
                UUID memberID = UUID.fromString(rs.getString("MemberID"));

                // double totalPrice = rs.getDouble("TotalAmount");

                HashMap<Product, Integer> listItems = getOrderItems(orderID);
                Order order = new Order(memberID, listItems);
                orderList.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
}
