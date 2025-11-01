package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;
import models.users.Supplier;

import java.sql.Date;

public class Beverage extends Food {
    private double volume;
    private String flavor;
    private double sugar;
    private boolean isCarbonated;

    public Beverage(double volume, String flavor, double sugar, boolean isCarbonated, int weight, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(weight, calories, ingredients, FoodCategory.BEVERAGE, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.volume = volume;
        this.flavor = flavor;
        this.sugar = sugar;
        this.isCarbonated = isCarbonated;
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

    public boolean isCarbonated() {
        return this.isCarbonated;
    }

    public void setCarbonated(boolean isCarbonated) {
        this.isCarbonated = isCarbonated;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.volume + " " + this.flavor + " " + this.sugar + " " + this.isCarbonated;
    }
}
