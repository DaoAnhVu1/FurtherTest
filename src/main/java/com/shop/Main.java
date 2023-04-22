package com.shop;

import java.util.Scanner;

import com.shop.views.ProductView;
import com.shop.views.ShoppingCartView;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        ProductView productView = new ProductView(scanner);
        ShoppingCartView shoppingCartView = new ShoppingCartView(scanner);
        do {
            // Display menu
            System.out.println();
            System.out.println("1. View products");
            System.out.println("2. Add products");
            System.out.println("3. Edit products");
            System.out.println("4. Remove products");
            System.out.println("5. Add product to current cart");
            System.out.println("6. Remove product from current cart");
            System.out.println("7. Update messages in current cart");
            System.out.println("8. Apply coupons");
            System.out.println("9. Remove coupons");
            System.out.println("10. View a cart");
            System.out.println("11. Sort and display all carts");
            System.out.println("12. Print receipts and create new cart");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            // Read user choice
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            // Process user choice
            switch (choice) {
                case 1 -> productView.viewProducts();
                case 2 -> productView.addProduct();
                case 3 -> productView.editProduct();
                case 4 -> productView.removeProduct();
                case 5 -> shoppingCartView.addProductToCart();
                case 6 -> shoppingCartView.removeProductFromCart();
                case 7 -> shoppingCartView.updateMessages();
                case 8 -> shoppingCartView.applyCoupon();
                case 9 -> shoppingCartView.removeCoupon();
                case 10 -> shoppingCartView.viewACart();
                case 11 -> shoppingCartView.displayAllCarts();
                case 12 -> shoppingCartView.printReceipt();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);
    }
}