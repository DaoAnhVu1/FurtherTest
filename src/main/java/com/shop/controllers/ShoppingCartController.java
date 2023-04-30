package com.shop.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shop.models.ShoppingCart;

public class ShoppingCartController {
    private static ShoppingCartController instance = null;
    private ArrayList<ShoppingCart> allShoppingCarts;

    // Singleton Pattern
    private ShoppingCartController() {
        allShoppingCarts = new ArrayList<>();
    }

    public static ShoppingCartController getInstance() {
        if (instance == null) {
            instance = new ShoppingCartController();
        }
        return instance;
    }

    // Get the most recent cart in the system
    public ShoppingCart getCurrentCart() {
        if (allShoppingCarts.size() == 0) {
            allShoppingCarts.add(new ShoppingCart());
            return allShoppingCarts.get(0);
        }

        return allShoppingCarts.get(allShoppingCarts.size() - 1);
    }

    public void addACart(ShoppingCart cart) {
        allShoppingCarts.add(cart);
    }

    public void createNewCart() {
        allShoppingCarts.add(new ShoppingCart());
    }

    public ArrayList<ShoppingCart> getAllShoppingCarts() {
        return allShoppingCarts;
    }

    // Return the sorted cart based on weight without changing the original list
    public List<ShoppingCart> getSortedShoppingCarts() {
        List<ShoppingCart> sortedCarts = new ArrayList<>(allShoppingCarts);
        Collections.sort(sortedCarts, Collections.reverseOrder());
        return sortedCarts;
    }
}
