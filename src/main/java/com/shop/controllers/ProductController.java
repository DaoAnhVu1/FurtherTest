package com.shop.controllers;

/**
 * @author Group 4
 */

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import com.shop.models.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductController {
    private static ProductController instance = null;
    private HashMap<String, Product> allProducts;

    private ProductController() {
        allProducts = new HashMap<>();
    }

    // Singleton Pattern
    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    public void addProduct(Product product) {
        if (allProducts.containsKey(product.getName())) {
            // There is already an item with the same name in the shop
            return;
        }
        allProducts.put(product.getName(), product);
    }

    public void removeProduct(String name) {
        allProducts.remove(name);
    }

    public Product getProduct(String name) {
        return allProducts.get(name);
    }

    public HashMap<String, Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Product> getAllProductsList() {
        return new ArrayList<>(allProducts.values());
    }

    // Update product quantity, parameter quantity can be negative to increase the quantity if needed
    public void updateProductQuantity(String product, int quantity) {
        int previousQuantity = allProducts.get(product).getQuantity();
        if (quantity > previousQuantity) {
            return;
        }
        allProducts.get(product).setQuantity(previousQuantity - quantity);
    }
}
