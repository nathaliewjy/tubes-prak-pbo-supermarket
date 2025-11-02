package models.products;

import models.users.Supplier;

import java.util.Date;

public class Household extends Product {
    private int hoID;
    private int weight;
    private Material material;

    public Household(int hoID, int weight, Material material, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(prodID, brand, ProductCategory.HOUSEHOLD, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.hoID = hoID;
        this.weight = weight;
        this.material = material;
    }

    public int getHoID() {
        return this.hoID;
    }

    public void setHoID(int hoID) {
        this.hoID = hoID;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.hoID + " " + this.weight + " " + this.material;
    }
}
