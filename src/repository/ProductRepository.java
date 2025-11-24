package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import models.products.Product;
import models.products.ProductCategory;
import models.users.Employee;
import util.Database;

public class ProductRepository {
    Connection conn = Database.connect();

    public Product findByNIK(String nik) {
        Product ProductFound = null;

        return ProductFound;
    }

    public void addProduct(Product e) {
        // work here
    }

    public void deleteProduct() {
        // work here
    }

    public void updateProduct(Product e) {
        // work here
    }

    public ArrayList<Product> getAllProductsByExpired(){
        ArrayList<Product> listProductExpired = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE expired_date < NOW()";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = Database.connect();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()){
                Product product = new Product(
                        rs.getString("Brand"),
                        rs.getString("Category").equals("FOOD") ? ProductCategory.FOOD : ProductCategory.BEVERAGE, // Prdouct Category enum,
                        rs.getDouble("PRICE"),
                        rs.getInt("StockInStorage"),
                        rs.getInt("StockInShelf"),
                        rs.getDate("ManufactureDate"),
                        rs.getDate("ExpiryDate")
                        );
                listProductExpired.add(product);
            }
        }
        catch(SQLException e){
            e.getMessage();

        }
        return listProductExpired;
    }

    public ArrayList<Product> getProductsByCategory() {
        ArrayList<Product> productList = new ArrayList<>();
        return productList;
    }
}
