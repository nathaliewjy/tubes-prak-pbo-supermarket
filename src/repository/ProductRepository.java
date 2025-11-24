package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import models.products.Product;
import models.products.ProductCategory;
import models.users.Employee;
import util.Database;

public class ProductRepository implements IProductRepository {

    private Product resultSetProduct(ResultSet rs) throws SQLException {
        Product product = new Product(
                rs.getString("Brand"),
                ProductCategory.valueOf(rs.getString("Category")),
                rs.getDouble("PRICE"),
                rs.getInt("StockInStorage"),
                rs.getInt("StockInShelf"),
                rs.getDate("ManufactureDate"),
                rs.getDate("ExpiryDate"));
        // Set the ID from the DB, since the constructor creates a random one
        product.setProdID(UUID.fromString(rs.getString("ProdID")));
        return product;
    }

    @Override
    public Product findProductById(UUID id) {
        String sql = "SELECT * FROM product WHERE ProdID = ? AND deletedAt IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO product (ProdID, Brand, Category, PRICE, StockInStorage, StockInShelf, ManufactureDate, ExpiryDate) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProdID().toString());
            pstmt.setString(2, product.getBrand());
            pstmt.setString(3, product.getCategory().name());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getStockInStorage());
            pstmt.setInt(6, product.getStockInShelf());
            pstmt.setDate(7, new java.sql.Date(product.getManufactureDate().getTime()));
            pstmt.setDate(8, new java.sql.Date(product.getExpiryDate().getTime()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(UUID id) {
        String sql = "UPDATE product SET deletedAt = CURRENT_TIMESTAMP WHERE ProdID = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getProductsByCategory(ProductCategory category) {
        ArrayList<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE Category = ? AND deletedAt IS NULL";

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productList.add(resultSetProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public void updateProductStock(Product product) {
        String sql = "UPDATE product SET StockInStorage = ?, StockInShelf = ? WHERE ProdID = ?";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product.getStockInStorage());
            pstmt.setInt(2, product.getStockInShelf());
            pstmt.setString(3, product.getProdID().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
