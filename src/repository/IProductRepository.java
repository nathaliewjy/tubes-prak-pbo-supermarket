package repository;

import java.util.ArrayList;

import models.products.Product;
import models.products.ProductCategory;

public interface IProductRepository {
    void addProduct(Product product);
    void deleteProduct(java.util.UUID id);
    Product findProductById(java.util.UUID id);
    ArrayList<Product> getProductsByCategory(ProductCategory category);
    void updateProductStock(Product product);
}
