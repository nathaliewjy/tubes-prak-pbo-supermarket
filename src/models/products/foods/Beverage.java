package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;

import java.sql.Date;

public class Beverage extends Food {
    private double volume;
    private String flavor;
    private double sugar;
    private boolean isCarbonated;

    public Beverage(double volume, String flavor, double sugar, boolean isCarbonated, int foodID, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(foodID, calories, ingredients, FoodCategory.BEVERAGE, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.volume = volume;
        this.flavor = flavor;
        this.sugar = sugar;
        this.isCarbonated = false;
    }
}
