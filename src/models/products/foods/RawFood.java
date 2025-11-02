package models.products.foods;

import models.products.Food;
import models.products.FoodCategory;
import models.products.ProductCategory;
import models.users.Supplier;

import java.sql.Date;

public class RawFood extends Food {
    private boolean isOrganic;

    public RawFood(boolean isOrganic, int weight, double calories, String ingredients, FoodCategory foodCat, Date expiryDate, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(weight, calories, ingredients, FoodCategory.SNACK, expiryDate, prodID, brand, ProductCategory.FOOD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.isOrganic = isOrganic;
    }

    public boolean isOrganic() {
        return this.isOrganic;
    }

    public void setOrganic(boolean isOrganic) {
        this.isOrganic = isOrganic;
    }

    @Override
    public String toString() {
        return super.toString() + "" + this.isOrganic;
    }
}
