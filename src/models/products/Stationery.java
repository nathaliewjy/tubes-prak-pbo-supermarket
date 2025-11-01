package models.products;

import java.sql.Date;

public class Stationery extends Product {
    private int statID;
    private String material;
    private String size;
    private String color;

    public Stationery(int statID, String material, String size, String color, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(prodID, brand, ProductCategory.STATIONERY, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.statID = statID;
        this.material = material;
        this.size = size;
        this.color = color;
    }
}
