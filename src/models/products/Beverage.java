package models.products;

import java.sql.Date;
import java.util.UUID;

public class Beverage extends Product {
    private int volume;
    private String flavor;
    private double sugar;

    public Beverage(int volume, String flavor, double sugar, UUID prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, Date manufactureDate, Date expiryDate) {
        super(prodID, brand, ProductCategory.BEVERAGE, price, stockInStorage, stockInShelf, manufactureDate, expiryDate);
        this.volume = volume;
        this.flavor = flavor;
        this.sugar = sugar;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getFlavor() {
        return this.flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public double getSugar() {
        return this.sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.volume + " " + this.flavor + " " + this.sugar;
    }
}
