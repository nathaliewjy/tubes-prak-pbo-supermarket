package models.products;

import java.sql.Date;

public class Household extends Product {
    private int hoID;
    // prodID masuk g?
    private String weight;
    private String material;

    public Household(int hoID, String weight, String material, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(prodID, brand, ProductCategory.HOUSEHOLD, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.hoID = hoID;
        this.weight = weight;
        this.material = material;
    }
}
