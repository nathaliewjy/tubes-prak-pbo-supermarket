package models.orders;

import models.products.Product;

import java.util.HashMap;

public class Cart {
    private HashMap<Product, Integer> listItems;

    public Cart(HashMap<Product, Integer> listItems) {
        this.listItems = listItems;
    }

    public HashMap<Product, Integer> getListItems() {
        return this.listItems;
    }

//    @Override
//    public String toString() {
//        return this.listItems.keySet(); // ininya meraahh
//    }
}
