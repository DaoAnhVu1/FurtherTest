package com.shop.views;

/**
 * @author Group 4
 */

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import java.util.*;

import com.shop.controllers.ProductController;
import com.shop.controllers.ShoppingCartController;
import com.shop.models.GiftItem;
import com.shop.models.Product;
import com.shop.models.ProductItem;
import com.shop.models.ShoppingCart;

public class ShoppingCartView {
    private ShoppingCartController shoppingCartController;
    private ProductController productController;
    private final Scanner scanner;

    public ShoppingCartView(Scanner scanner) {
        shoppingCartController = ShoppingCartController.getInstance();
        productController = ProductController.getInstance();
        this.scanner = scanner;
    }

    public void addProductToCart() throws InputMismatchException {
        System.out.println();
        ArrayList<Product> productList = productController.getAllProductsList();
        if (productList.size() == 0) {
            System.out.println("There is no product in store, try adding some");
            return;
        }

        ProductView productView = new ProductView(scanner);
        productView.viewProducts();

        try {
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

            if (wantedQuantity <= 0) {
                System.out.println("You cannot add 0 or less item");
                return;
            }

            int availableQuantity = chosenProduct.getQuantity();

            if (wantedQuantity > availableQuantity) {
                System.out.println();
                System.out.println("There is not enough stock left");
                return;
            }

            System.out.println();
            if (chosenProduct.canBeGifted()) {
                List<String> listOfMessages = new ArrayList<>();
                System.out.println(
                        "How many product do you want to add messages ? (The number must be smaller or equal to "
                                + wantedQuantity + ")");

                int numberOfMessages = scanner.nextInt();
                scanner.nextLine();
                // Invalid input if the number of messages more than the number of added item
                if (numberOfMessages > wantedQuantity || numberOfMessages < 0) {
                    System.out.println("Invalid number of messages");
                    return;
                } else if (numberOfMessages == 0) {
                    System.out.println("No message added");
                } else {
                    for (int i = 0; i < numberOfMessages; i++) {
                        System.out.print("Enter message number " + (i + 1) + ": ");
                        String message = scanner.nextLine();
                        listOfMessages.add(message.trim());
                    }
                }

                shoppingCartController.getCurrentCart().addProduct(productName, wantedQuantity, listOfMessages);
            } else {
                shoppingCartController.getCurrentCart().addProduct(productName, wantedQuantity);
            }
            System.out.println();
            System.out.println("Successfully added " + wantedQuantity + " " + productName + " to current cart");
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void removeProductFromCart() throws InputMismatchException {
        System.out.println();
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        int index = 1;
        for (ProductItem productItem : currentCart.getAllItems()) {
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
        try {
            System.out.print("Enter the name of the product you want to remove: ");
            String productName = scanner.nextLine();

            System.out.println();

            System.out.print("Enter the quantity you want to remove: ");
            int removeQuantity = scanner.nextInt();
            scanner.nextLine();

            if (removeQuantity <= 0 || removeQuantity > currentCart.getAllItems().size()) {
                System.out.println("Invalid quantity input");
                return;
            }
            boolean isRemoved = false;
            boolean currentProductIsAGift = productController.getProduct(productName).canBeGifted();
            if (currentProductIsAGift) {
                System.out.println();
                HashSet<Integer> validInput = new HashSet<>();
                List<Integer> removeIndexList = new ArrayList<>();
                for (int i = 0; i < currentCart.getGiftItems().size(); i++) {
                    GiftItem currentItem = currentCart.getGiftItems().get(i);
                    if (currentItem.getProduct().getName().equals(productName)) {
                        System.out.println("Item " + (i + 1));
                        System.out.println("Name: " + (currentItem.getProduct().getName()));
                        System.out.println("Message: " + currentItem.getMessage());
                        System.out.println();
                        validInput.add(i);
                    }
                }
                System.out.println("This is a gift item, please specify the item number you want to remove");
                for (int i = 0; i < removeQuantity; i++) {
                    System.out.print((i + 1) + ". Remove item number: ");
                    int itemNumberToBeRemoved = scanner.nextInt();
                    scanner.nextLine();
                    itemNumberToBeRemoved -= 1;
                    if (!validInput.contains(itemNumberToBeRemoved)) {
                        System.out.println("Invalid item number, try again");
                        return;
                    }
                    removeIndexList.add(itemNumberToBeRemoved);
                    System.out.println();
                }
                isRemoved = currentCart.removeGiftItem(productName, removeQuantity, removeIndexList);
            } else {
                isRemoved = currentCart.removeNormalItem(productName, removeQuantity);
            }

            System.out.println();
            if (isRemoved) {
                System.out.println("Successfully removed " + removeQuantity + " " + productName);
            } else {
                System.out.println("Remove unsuccessfully, please check your input again");
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void viewCurrentCart() {
        System.out.println();
        ShoppingCart currentCart = shoppingCartController.getCurrentCart();
        currentCart.print(false);
    }

    public void applyCoupon() {
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        System.out.print("Enter coupon: ");
        String coupon = scanner.nextLine();
        boolean success = currentCart.applyCoupon(coupon);
        System.out.println();
        if (success) {
            System.out.println("Success");
        } else {
            System.out.println("Fail to apply coupon, please check again");
        }
    }

    public void removeCoupon() {
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        boolean remove = currentCart.removeCoupon();
        System.out.println();
        if (remove) {
            System.out.println("Successfully remove coupon");
            return;
        }

        System.out.println("There is no coupon to remove");
    }

    public void updateMessages() throws InputMismatchException {
        System.out.println();
        ShoppingCart currentCart = ShoppingCartController.getInstance().getCurrentCart();
        if (currentCart.getGiftItems().size() == 0) {
            System.out.println("There no gift-able item in your cart");
            return;
        }

        for (int i = 0; i < currentCart.getGiftItems().size(); i++) {
            GiftItem currentItem = currentCart.getGiftItems().get(i);
            if (currentItem != null) {
                Product currentProduct = currentItem.getProduct();
                System.out.println("Item " + (i + 1));
                System.out.println("Name: " + currentProduct.getName());
                System.out.println("Description:" + currentProduct.getDescription());
                System.out.println("Price: $" + currentProduct.getPrice());
                System.out.println("Tax Type: " + currentProduct.getTaxType());
                System.out.println("Message: " + currentItem.getMessage());
                System.out.println();
            }
        }

        try {
            System.out.print("Enter the item number you want to update (1, 2, ...) : ");
            int userInput = scanner.nextInt();
            userInput = userInput - 1;
            scanner.nextLine();

            if (userInput < 0 || userInput >= currentCart.getGiftItems().size()) {
                System.out.println("Invalid input");
                return;
            }

            System.out.println();
            System.out.print("Enter the new message: ");
            String newMessage = scanner.nextLine();
            currentCart.getGiftItems().get(userInput).setMessage(newMessage);
            System.out.println();
            System.out.println("Successfully update the message");
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void viewACart() throws InputMismatchException {
        System.out.println();
        ArrayList<ShoppingCart> allShoppingCarts = shoppingCartController.getAllShoppingCarts();
        if (allShoppingCarts.size() == 0) {
            System.out.println("There is no cart in the system");
            return;
        }
        for (int i = 0; i < allShoppingCarts.size() - 1; i++) {
            System.out.println("Shopping cart number " + (i + 1));
            System.out.println();
        }
        System.out.println();
        try {
            System.out.print("Select one (1, 2, ...): ");
            int chosenIndex = scanner.nextInt();
            chosenIndex -= 1;
            scanner.nextLine();
            System.out.println();

            if (chosenIndex < 0 || chosenIndex >= allShoppingCarts.size() - 1) {
                System.out.println("Invalid input, try again");
                return;
            }
            ShoppingCart chosenCart = allShoppingCarts.get(chosenIndex);
            chosenCart.print(false);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void displayAllCarts() {
        List<ShoppingCart> sortedShoppingCarts = shoppingCartController.getSortedShoppingCarts();
        System.out.println();
        int index = 1;
        boolean found = false;
        for (ShoppingCart cart : sortedShoppingCarts) {
            if (cart.getTotalWeight() == 0) {
                continue;
            }
            System.out.println("#Cart " + index);
            System.out.println();
            index += 1;
            cart.print(false);
            found = true;
            System.out.println("Total weight: " + cart.getTotalWeight());
            for (int i = 0; i < 20; i++) {
                System.out.print("-");
            }
            System.out.println();
        }

        if (!found) {
            System.out.println("There is no cart in the system");
        }
    }

    public void printReceipt() {
        System.out.println();
        if (shoppingCartController.getCurrentCart().getAllItems().size() == 0) {
            System.out.println("There is 0 item in the current cart");
            return;
        }
        shoppingCartController.getCurrentCart().print(true);
        shoppingCartController.createNewCart();
    }
}
