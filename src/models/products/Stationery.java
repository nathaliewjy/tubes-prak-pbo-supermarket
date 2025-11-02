package models.products;

import models.users.Supplier;

import java.sql.Date;

public class Stationery extends Product {
    private int statID;
    private Material material;
    private String size;
    private boolean isRefillable;


    public Stationery(int statID, Material material, String size, boolean isRefillable, int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        super(prodID, brand, ProductCategory.STATIONERY, price, stockInStorage, stockInShelf, unit, manufactureDate, sup);
        this.statID = statID;
        this.material = material;
        this.size = size;
        this.isRefillable = isRefillable;
    }

    public int getStatID() {
        return this.statID;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material =  material;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String  size) {
        this.size = size;
    }

    public boolean isRefillable() {
        return this.isRefillable;
    }

    public void setRefillable(boolean isRefillable) {
        this.isRefillable = isRefillable;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.statID + " " + this.material + " " + this.size + " " + this.isRefillable;
    }
}
