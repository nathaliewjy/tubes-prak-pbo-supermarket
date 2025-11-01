package models.products;

import models.users.Supplier;

import java.util.Date;

public abstract class Product {
    private int prodID;
    private String brand;
    private ProductCategory category;
    private double price;
    private int stockInStorage;
    private int stockInShelf;
    private String unit;
    private Date manufactureDate;
    private Supplier sup; // jd tiap prod punya 1 sup

    public Product(int prodID, String brand, ProductCategory category, double price, int stockInStorage, int stockInShelf, String unit, Date manufactureDate, Supplier sup) {
        this.prodID = prodID;
        this.brand = brand;
        this.category = category;
        this.price =  price;
        this.stockInStorage = stockInStorage;
        this.stockInShelf = stockInShelf;
        this.unit = unit;
        this.manufactureDate = manufactureDate;
        this.sup = sup;
    }

    public int getProdID() {
        return this.prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockInStorage() {
        return this.stockInStorage;
    }

    public void setStockInStorage(int stockInStorage) {
        this.stockInStorage = stockInStorage;
    }

    public int getStockInShelf() {
        return this.stockInShelf;
    }

    public void setStockInShelf(int stockInShelf) {
        this.stockInShelf = stockInShelf;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getManufactureDate() {
        return this.manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;

    }

    public Supplier getSup() {
        return this.sup;
    }

    public void setSup(Supplier sup) {
        this.sup = sup;
    }

    @Override
    public String toString() {
        return this.prodID + " " + this.brand + " " + this.category + " " + this.price + " " + this.stockInStorage + " " + this.stockInShelf + " " + this.unit + " " + this.manufactureDate + " " + this.sup;
    }
}
