package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;

import java.sql.Date;

public class HeavyMeal extends Food {
    private double portionSize;
    private double carbs;
    private double protein;
    private double fat;
    private boolean isVegan;

    public HeavyMeal(double portionSize, double carbs, double protein, double fat, boolean isVegan, int foodID, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(foodID, calories, ingredients, FoodCategory.HEAVY_MEAL, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.portionSize = portionSize;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.isVegan = false;
    }
}
