package models.products;

import java.sql.Date;

public class Food extends Product {
    private int foodID;
    // prodID masuk ga?
    private double calories;
    private String ingredients;
    private FoodCategory foodCat;
    private Date expiryDate;

    public Food(int foodID, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate) {
        super(prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate);
        this.foodID = foodID;
        this.calories = calories;
        this.ingredients = ingredients;
        this.foodCat = foodCat;
        this.expiryDate = expiryDate;
    }
}
