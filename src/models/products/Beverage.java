package models.products;

import models.users.Supplier;

import java.sql.Date;

public class Beverage extends Product {
    private double volume;
    private String flavor;
    private double sugar;

    public Beverage(double volume, String flavor, double sugar, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, Date manufactureDate, Date expiryDate, Supplier sup) {
        super(prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, manufactureDate, expiryDate, sup);
        this.volume = volume;
        this.flavor = flavor;
        this.sugar = sugar;
    }

    public double getVolume() {
        return this.volume;
    }

    public void setVolume(double volume) {
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
