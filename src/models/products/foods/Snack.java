package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;

import java.sql.Date;

public class Snack extends Food {
    private double weight;
    private String flavor;
    private double sugar;
    private boolean isGlutenFree;

    public Snack(double weight, String flavor, double sugar, boolean isGlutenFree, int foodID, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(foodID, calories, ingredients, FoodCategory.SNACK, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.weight = weight;
        this.flavor = flavor;
        this.sugar = sugar;
        this.isGlutenFree = false;
    }
}
