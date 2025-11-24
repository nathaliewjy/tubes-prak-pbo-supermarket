package repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import models.orders.Order;
import models.products.Product;

public interface IOrderRepository {
    public String addOrder(Order m);
    public void deleteOrder();
    ArrayList<Order> getOrderList();
    HashMap<Product, Integer> getOrderItems(UUID orderID);
}
