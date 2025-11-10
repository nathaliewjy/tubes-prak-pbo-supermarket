package models.products;

import java.sql.Date;
import java.util.UUID;

public class Food extends Product {
    private int weight;
    private double calories;
    private String ingredients;

    public Food(int weight, double calories, String ingredients, UUID prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, Date manufactureDate, Date expiryDate) {
        super(prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, manufactureDate, expiryDate);
        this.weight = weight;
        this.calories = calories;
        this.ingredients = ingredients;
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

    @Override
    public String toString() {
        return super.toString() +  " " + this.weight + " " + this.calories + "  " + this.ingredients;
    }
}
