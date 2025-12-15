package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import models.products.Product;
import models.products.ProductCategory;
import util.Database;

public class ProductRepository implements IProductRepository {

    private Product resultSetProduct(ResultSet rs) throws SQLException {
        Product product = new Product(
                UUID.fromString(rs.getString("ProdID")),
                rs.getString("SKU"),
                rs.getString("Brand"),
                ProductCategory.valueOf(rs.getString("Category")),
                rs.getDouble("PRICE"),
                rs.getInt("StockInStorage"),
                rs.getInt("StockInShelf"),
                rs.getDate("ManufactureDate"),
                rs.getDate("ExpiryDate"),
                rs.getDate("deletedAt"));
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

    @Override
    public Product findProductBySKU(String sku) {
        String sql = "SELECT * FROM product WHERE SKU = ? AND deletedAt IS NULL";
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sku);
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

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO product (ProdID, SKU, Brand, Category, PRICE, StockInStorage, StockInShelf, ManufactureDate, ExpiryDate) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProdID().toString());
            pstmt.setString(2, product.getSku());
            pstmt.setString(3, product.getBrand());
            pstmt.setString(4, product.getCategory().name());
            pstmt.setDouble(5, product.getPrice());
            pstmt.setInt(6, product.getStockInStorage());
            pstmt.setInt(7, product.getStockInShelf());
            pstmt.setDate(8, new java.sql.Date(product.getManufactureDate().getTime()));
            pstmt.setDate(9, new java.sql.Date(product.getExpiryDate().getTime()));
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

    public ArrayList<Product> getAllProductsByExpired() {
        ArrayList<Product> listProductExpired = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE ExpiryDate < NOW()";
        ResultSet rs = null;
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            rs = pstmt.executeQuery();

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
                listProductExpired.add(product);
            }
        } catch (SQLException e) {
            e.getMessage();

        }
        return listProductExpired;
    }

    @Override
    public void updateProductPrice(UUID prodID, double newPrice) {
        String sql = "UPDATE product SET price = ? WHERE ProdID = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setString(2, prodID.toString());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<Product> getExpiredProducts(int days) {
        ArrayList<Product> expiredProds = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE ExpiryDate <= DATE_ADD(CURRENT_DATE(), INTERVAL ? DAY) AND deletedAt IS NULL ORDER BY ExpiryDate ASC";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, days);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expiredProds.add(resultSetProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expiredProds;
    }


    @Override
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> prods = new ArrayList<>();

        String sql = "SELECT * FROM product WHERE deletedAt IS NULL";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                prods.add(resultSetProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prods;
    }

    
}
