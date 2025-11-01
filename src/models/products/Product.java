package models.products;

import java.util.Date;

public abstract class Product {
    private int prodID;
    // supID masuk ga?
    private String brand;
    private ProductCategory category;
    private double price;
    private int stockInStorage;
    private int stockInShelf;
    private String unit;
    private Date manufactureDate;

    public Product(int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        this.prodID = prodID;
        this.brand = brand;
        this.category = category;
        this.price =  price;
        this.stockInStorage = stockInStorage;
        this.stockInShelf = stockInShelf;
        this.unit = unit;
        this.manufactureDate = manufactureDate;
    }
}
