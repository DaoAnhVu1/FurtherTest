package com.shop.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.shop.controllers.ProductController;
import com.shop.models.Coupon;
import com.shop.models.CouponType;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.TaxType;

public class ProductView {
    private ProductController productController;
    private Scanner scanner;

    public ProductView(Scanner scanner) {
        productController = ProductController.getInstance();
        this.scanner = scanner;
    }

    public void viewProducts() {
        System.out.println();
        HashMap<String, Product> allProducts = productController.getAllProducts();
        List<String> sortedProductNames = new ArrayList<>(allProducts.keySet());
        // Display product in sorted order
        Collections.sort(sortedProductNames, (name1, name2) -> {
            char firstChar1 = name1.toLowerCase().charAt(0);
            char firstChar2 = name2.toLowerCase().charAt(0);
            return Character.compare(firstChar1, firstChar2);
        });
        if (allProducts.isEmpty()) {
            System.out.println("There is no product in store, try adding some");
            return;
        }
        int index = 1;
        for (String currentProduct : sortedProductNames) {
            Product product = allProducts.get(currentProduct);
            System.out.println(index + ": " + product.toString());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("Price: $" + product.getPrice());
            if (product instanceof PhysicalProduct) {
                System.out.println("Weight: " + ((PhysicalProduct) product).getWeight());
            }
            index += 1;
            System.out.println();
        }
    }

    public void addProduct() throws InputMismatchException {
        System.out.println();
        String name;
        String description;
        int quantity;
        double price;
        String taxType;
        boolean gift;
        double weight;

        try {
            System.out.print("Enter the product name: ");
            name = scanner.nextLine();

            if (productController.getProduct(name) != null) {
                System.out.println("There is already an item named: " + name);
                return;
            }

            System.out.print("Enter the product description: ");
            description = scanner.nextLine();

            System.out.print("Enter the product quantity: ");
            quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter the product price: ");
            price = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter the tax type of the product (TAX_FREE, NORMAL_TAX, LUXURY_TAX): ");
            taxType = scanner.nextLine();

            if (!taxType.equals("TAX_FREE") && !taxType.equals("NORMAL_TAX") && !taxType.equals("LUXURY_TAX")) {
                System.out.println("Invalid tax type");
                return;
            }

            System.out.print("Do you want to make this product a gift (Y/N): ");
            gift = scanner.nextLine().equalsIgnoreCase("Y");

            Product finalProduct;
            System.out.print("Is the product physical or digital (P/D): ");
            String isPhysicalInput = scanner.nextLine();
            if (isPhysicalInput.equalsIgnoreCase("p")) {
                System.out.print("Enter the product weight: ");
                weight = scanner.nextDouble();
                scanner.nextLine();
                finalProduct = new PhysicalProduct(name, description, quantity, price, TaxType.valueOf(taxType), gift,
                        weight);
            } else if (isPhysicalInput.equalsIgnoreCase("d")) {
                finalProduct = new DigitalProduct(name, description, quantity, price, TaxType.valueOf(taxType), gift);
            } else {
                System.out.println("Invalid input");
                return;
            }
            System.out.println();
            System.out.println("Successfully created " + finalProduct.toString());
            productController.addProduct(finalProduct);
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void editProduct() throws InputMismatchException {
        viewProducts();
        try {
            System.out.print("Enter the name of the product you want to edit: ");
            String name = scanner.nextLine();
            Product chosenProduct = ProductController.getInstance().getProduct(name);

            if (chosenProduct == null) {
                System.out.println("Product not found.");
                return;
            }

            boolean editing = true;
            while (editing) {
                System.out.println();
                System.out.println("Current product: " + chosenProduct.getName());
                System.out.println();
                System.out.println("Enter the field you want to update ");
                System.out.println("1.Description: " + chosenProduct.getDescription());
                System.out.println("2.Quantity: " + chosenProduct.getQuantity());
                System.out.println("3.Price: " + chosenProduct.getPrice());
                System.out.println("4.Add new coupon to this product");
                System.out.println("5. Remove coupon from this product");
                System.out.println("0: Exit editing");
                System.out.println();
                System.out.print("Your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter the new description: ");
                        String newDescription = scanner.nextLine();
                        chosenProduct.setDescription(newDescription);
                    }
                    case 2 -> {
                        System.out.print("Enter the new quantity: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character
                        chosenProduct.setQuantity(newQuantity);
                    }
                    case 3 -> {
                        System.out.print("Enter the new price: ");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine(); // consume the newline character
                        chosenProduct.setPrice(newPrice);
                    }
                    case 4 -> {
                        String couponCode;
                        String couponType;
                        int value;
                        System.out.print("Enter the code for the new coupon: ");
                        couponCode = scanner.nextLine();
                        System.out.print("Enter the type of the new coupon (PRICE, PERCENT): ");
                        couponType = scanner.nextLine();

                        if (!couponType.equalsIgnoreCase("PRICE") && !couponType.equalsIgnoreCase("PERCENT")) {
                            System.out.println("Invalid input");
                            break;
                        }

                        System.out.print("Enter the value of the coupon: ");
                        value = scanner.nextInt();
                        scanner.nextLine();

                        Coupon newCoupon = new Coupon(couponCode, CouponType.valueOf(couponType), value);
                        newCoupon.setProduct(chosenProduct);
                        System.out.println();
                        System.out.println("Successfully create a new coupon");
                    }
                    case 5 -> {
                        ArrayList<Coupon> couponsFromProduct = new ArrayList<>();
                        for (String coupon : Coupon.getAllCoupon().keySet()) {
                            if (Coupon.getAllCoupon().get(coupon).getProduct().getName()
                                    .equals(chosenProduct.getName())) {
                                couponsFromProduct.add(Coupon.getAllCoupon().get(coupon));
                            }
                        }

                        if (couponsFromProduct.size() == 0) {
                            System.out.println("There is no coupon for this product");
                            break;
                        }

                        int index = 1;
                        int chosenIndex;
                        for (Coupon coupon : couponsFromProduct) {
                            System.out.println(index + ". " + coupon.getCode());
                            index += 1;
                        }
                        System.out.print("Enter the code number you want to remove: ");
                        chosenIndex = scanner.nextInt();
                        scanner.nextLine();

                        chosenIndex -= 1;

                        if (chosenIndex < 0 || chosenIndex > couponsFromProduct.size()) {
                            System.out.println("Invalid input");
                            break;
                        }

                        Coupon couponToBeRemoved = couponsFromProduct.get(chosenIndex);

                        if (couponToBeRemoved.getProduct().getName().equals(chosenProduct.getName())) {
                            Coupon.getAllCoupon().remove(couponToBeRemoved.getCode());
                            System.out.println("Successfully remove " + couponToBeRemoved.getCode());
                        }

                        break;
                    }
                    case 0 -> editing = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    public void removeProduct() {
        System.out.println();
        String productName;
        viewProducts();
        System.out.print("Enter the product name that you want to delete: ");
        productName = scanner.nextLine();

        if (productController.getProduct(productName) == null) {
            System.out.println();
            System.out.println("There is no item with the name: " + productName);
            return;
        }
        productController.removeProduct(productName);
        System.out.println("Successfully remove: " + productName);
    }

    public void addCoupon() {

    }

}
