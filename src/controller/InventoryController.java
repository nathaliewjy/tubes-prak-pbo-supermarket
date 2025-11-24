package controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import models.products.Product;
import repository.OrderRepository;
import repository.ProductRepository;

public class InventoryController {
    ProductRepository productRepository;
    OrderRepository orderRepository;

    public InventoryController(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<Product> getProductsByCategory(String category) {
        // return productRepository.getProductsByCategory(category); nunggu update method di product repository
        return null;
    }

    public List<Product> getAllExpiredProducts() {
        return productRepository.getAllProductsByExpired(); 
    }

    public void updateStockInShelf(UUID orderID) {
        HashMap<Product, Integer> listItems = orderRepository.getOrderItems(orderID);
        for (Product p : listItems.keySet()) {
            int quantity = listItems.get(p);
            int stockInShelf = p.getStockInShelf() - quantity;
            p.setStockInShelf(stockInShelf);
            productRepository.updateProduct(p);
        }
    }

}
