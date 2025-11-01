package models.products;

import models.users.Supplier;

import java.sql.Date;

public class Food extends Product {
    private int weight;
    private double calories;
    private String ingredients;
    private FoodCategory foodCat;
    private Date expiryDate;

    public Food(int weight, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.weight = weight;
        this.calories = calories;
        this.ingredients = ingredients;
        this.foodCat = foodCat;
        this.expiryDate = expiryDate;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getCalories() {
        return this.calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String getIngredients() {
        return  this.ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public FoodCategory getFoodCat() {
        return this.foodCat;
    }

    public void setFoodCat(FoodCategory foodCat) {
        this.foodCat = foodCat;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date  expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return this.weight + " " + this.calories + "  " + this.ingredients + " " + this.foodCat + " " + this.expiryDate;
    }
}
