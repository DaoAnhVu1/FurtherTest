package com.shop.controllers;

import java.util.ArrayList;
import java.util.Collections;

import com.shop.models.ShoppingCart;

public class ShoppingCartController {
    private static ShoppingCartController instance = null;
    private ArrayList<ShoppingCart> allShoppingCarts;

    private ShoppingCartController() {
        allShoppingCarts = new ArrayList<>();
    }

    public static ShoppingCartController getInstance() {
        if (instance == null) {
            instance = new ShoppingCartController();
        }
        return instance;
    }

    public ShoppingCart getCurrentCart() {
        if (allShoppingCarts.size() == 0) {
            allShoppingCarts.add(new ShoppingCart());
            return allShoppingCarts.get(0);
        }

        return allShoppingCarts.get(allShoppingCarts.size() - 1);
    }

    public void createNewCart() {
        allShoppingCarts.add(new ShoppingCart());
    }

    public ArrayList<ShoppingCart> getAllShoppingCarts() {
        return allShoppingCarts;
    }

    public void sort() {
        Collections.sort(allShoppingCarts);
    }
}
