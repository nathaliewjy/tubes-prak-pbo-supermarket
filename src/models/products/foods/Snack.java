package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;
import models.users.Supplier;

import java.sql.Date;

public class Snack extends Food {
    private String flavor;
    private double sugar;
    private boolean isGlutenFree;

    public Snack(String flavor, double sugar, boolean isGlutenFree, int weight, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(weight, calories, ingredients, FoodCategory.SNACK, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.flavor = flavor;
        this.sugar = sugar;
        this.isGlutenFree = isGlutenFree;
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

    public boolean isGlutenFree() {
        return this.isGlutenFree;
    }

    public void setGlutenFree(boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.flavor + " " + this.sugar + " " + this.isGlutenFree;
    }
}
