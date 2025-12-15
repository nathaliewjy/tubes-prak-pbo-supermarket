package repository;

import java.util.ArrayList;
import java.util.UUID;

import models.products.Product;
import models.products.ProductCategory;

public interface IProductRepository {
    void addProduct(Product product);
    void deleteProduct(java.util.UUID id);
    Product findProductById(java.util.UUID id);
    Product findProductBySKU(String sku);
    void updateProductStock(Product product);
    // ini ditambah sama nathalie
    void updateProductPrice(UUID prodID, double newPrice);
    ArrayList<Product> getExpiredProducts(int days);
}
