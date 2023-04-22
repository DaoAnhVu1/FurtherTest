package com.shop.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.shop.controllers.ProductController;
import com.shop.controllers.ShoppingCartController;
import com.shop.models.GiftItem;
import com.shop.models.Product;
import com.shop.models.ProductItem;
import com.shop.models.ShoppingCart;

public class ShoppingCartView {
    private ShoppingCartController shoppingCartController;
    private ProductController productController;
    private Scanner scanner;

    public ShoppingCartView(Scanner scanner) {
        shoppingCartController = ShoppingCartController.getInstance();
        productController = ProductController.getInstance();
        this.scanner = scanner;
    }

    public void addProductToCart() {
        System.out.println();
        ArrayList<Product> productList = productController.getAllProductsList();
        if (productList.size() == 0) {
            System.out.println("There is no product in store, try adding some");
            return;
        }
        for (int i = 0; i < productList.size(); i++) {
            System.out.println((i + 1) + ": " + productList.get(i).getName());
            System.out.println("Available: " + productList.get(i).getQuantity());
        }

        System.out.println();
        System.out.print("Enter the name of the product you want to add: ");
        String productName = scanner.nextLine();
        Product chosenProduct = productController.getProduct(productName);
        if (chosenProduct == null) {
            System.out.println("There is no item with the name: " + productName);
            return;
        }

        System.out.print("Enter the quantity you want to add: ");
        int wantedQuantity = scanner.nextInt();
        scanner.nextLine();

        int availableQuantity = chosenProduct.getQuantity();

        if (wantedQuantity > availableQuantity) {
            System.out.println();
            System.out.println("There is not enough stock left");
            return;
        }

        System.out.println();
        if (chosenProduct.canBeGifted()) {
            List<String> listOfMessages = new ArrayList<>();
            System.out.println("How many product do you want to add messages ? (The number must be smaller or equal to "
                    + wantedQuantity + ")");

            int numberOfMessages = scanner.nextInt();
            scanner.nextLine();
            // Invalid input if the number of messages more than the number of added item
            if (numberOfMessages > wantedQuantity || numberOfMessages <= 0) {
                System.out.println("Invalid number of messages");
                return;
            }

            for (int i = 0; i < numberOfMessages; i++) {
                System.out.print("Enter message number " + (i + 1) + ": ");
                String message = scanner.nextLine();
                listOfMessages.add(message.trim());
            }

            shoppingCartController.getCurrentCart().addProduct(productName, wantedQuantity, listOfMessages);
        } else {
            shoppingCartController.getCurrentCart().addProduct(productName, wantedQuantity);
        }
        System.out.println();
        System.out.println("Successfully added " + wantedQuantity + " " + productName + " to current cart");
    }

    public void removeProductFromCart() {
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        int index = 1;
        for (ProductItem productItem : currentCart.getItems()) {
            Product currentProduct = productItem.getProduct();
            System.out.println("Item " + index);
            System.out.println("Name: " + currentProduct.getName());
            System.out.println("Description:" + currentProduct.getDescription());
            System.out.println("Price: $" + currentProduct.getPrice());
            System.out.println("Tax Type: " + currentProduct.getTaxType());
            if (productItem instanceof GiftItem) {
                System.out.println("Message: " + ((GiftItem) productItem).getMessage());
            }
            System.out.println();
            index += 1;
        }

        System.out.print("Enter the name of the product you want to remove: ");
        String productName = scanner.nextLine();

        System.out.println();

        System.out.print("Enter the quantity you want to remove: ");
        int removeQuantity = scanner.nextInt();
        scanner.nextLine();

        if (removeQuantity <= 0 || removeQuantity > currentCart.getItems().size()) {
            System.out.println("Invalid input");
            return;
        }
        currentCart.removeProduct(productName, removeQuantity);
    }

    public void applyCoupon() {
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        System.out.print("Enter coupon: ");
        String coupon = scanner.nextLine();
        boolean Success = currentCart.applyCoupon(coupon);
        if (Success) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid coupon");
        }
    }

    public void removeCoupon() {
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        boolean remove = currentCart.removeCoupon();
        if (remove) {
            System.out.println("Successfully remove coupon");
            return;
        }

        System.out.println("There is no coupon to remove");
    }

    public void updateMessages() {
        System.out.println();
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        HashSet<Integer> validInput = new HashSet<>();
        for (int i = 0; i < currentCart.getItems().size(); i++) {
            ProductItem currentItem = currentCart.getItems().get(i);
            if (currentItem instanceof GiftItem) {
                Product currentProduct = currentItem.getProduct();
                System.out.println("Item " + (i + 1));
                System.out.println("Name: " + currentProduct.getName());
                System.out.println("Description:" + currentProduct.getDescription());
                System.out.println("Price: $" + currentProduct.getPrice());
                System.out.println("Tax Type: " + currentProduct.getTaxType());
                System.out.println("Message: " + ((GiftItem) currentItem).getMessage());
                System.out.println();
                validInput.add(i);
            }
        }

        System.out.print("Enter the item number you want to update (1, 2, ...) : ");
        int userInput = scanner.nextInt();
        userInput = userInput - 1;
        scanner.nextLine();
        if (!validInput.contains(userInput)) {
            System.out.println("Invalid input, you must enter the provided item number");
            return;
        }
        System.out.println();
        System.out.print("Enter the new message: ");
        String newMessage = scanner.nextLine();
        ((GiftItem) currentCart.getItems().get(userInput)).setMessage(newMessage);
        System.out.println("Successfully update the message");
    }

    public void viewACart() {
        System.out.println();
        ArrayList<ShoppingCart> allShoppingCarts = shoppingCartController.getAllShoppingCarts();
        for (int i = 0; i < allShoppingCarts.size(); i++) {
            System.out.println("Shopping cart number " + (i + 1));
        }
        System.out.println();

        System.out.print("Select one (1, 2, ...): ");
        int chosenIndex = scanner.nextInt();
        chosenIndex -= 1;
        scanner.nextLine();
        System.out.println();

        if (chosenIndex < 0 || chosenIndex >= allShoppingCarts.size()) {
            System.out.println("Invalid input, try again");
            return;
        }
        ShoppingCart chosenCart = allShoppingCarts.get(chosenIndex);
        chosenCart.print(false);
        System.out.println();
    }

    public void displayAllCarts() {
        ShoppingCartController.getInstance().sort();
        for (ShoppingCart cart : ShoppingCartController.getInstance().getAllShoppingCarts()) {
            cart.print(false);
            System.out.println();
        }
    }

    public void printReceipt() {
        if (shoppingCartController.getCurrentCart().getItems().size() == 0) {
            System.out.println("There is 0 item in the current cart");
            return;
        }
        shoppingCartController.getCurrentCart().print(true);
        shoppingCartController.createNewCart();
    }
}
