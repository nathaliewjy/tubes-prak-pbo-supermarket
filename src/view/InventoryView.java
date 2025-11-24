package view;

import controller.InventoryController;

public class InventoryView {
    InventoryController inventoryController;

    public InventoryView(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    public void render(){
        System.out.println("Inventory View");
        System.out.println("1. View All Products");
        System.out.println("2. View Expired Products");
        System.out.println("3. View Products By Category");
        
    }
}
