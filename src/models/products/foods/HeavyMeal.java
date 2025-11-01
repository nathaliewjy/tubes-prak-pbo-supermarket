package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;
import models.users.Supplier;

import java.sql.Date;

public class HeavyMeal extends Food {
    private double portionSize;
    private double carbs;
    private double protein;
    private double fat;
    private boolean isVegan;

    public HeavyMeal(double portionSize, double carbs, double protein, double fat, boolean isVegan,  int weight, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(weight, calories, ingredients, FoodCategory.HEAVY_MEAL, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.portionSize = portionSize;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.isVegan = isVegan;
    }

    public double getPortionSize() {
        return this.portionSize;
    }

    public void setPortionSize(double portionSize) {
        this.portionSize = portionSize;
    }

    public double getCarbs() {
        return this.carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getProtein() {
        return this.protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return this.fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public boolean isVegan() {
        return this.isVegan;
    }

    public void setVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.portionSize + " " + this.carbs + " " + this.protein + " " + this.fat + " " + this.isVegan;
    }
}
