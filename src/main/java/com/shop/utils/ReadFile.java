package com.shop.utils;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.shop.controllers.ProductController;
import com.shop.controllers.ShoppingCartController;
import com.shop.models.Coupon;
import com.shop.models.CouponType;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.ShoppingCart;
import com.shop.models.TaxType;

public class ReadFile {
    private static ProductController productController = ProductController.getInstance();
    private static ShoppingCartController shoppingCartController = ShoppingCartController.getInstance();

    public static void loadProduct() {
        try {
            Files.lines(Paths.get("./src/main/java/com/shop/data/products.txt"))
                    .forEach(line -> {
                        // Extract the fields on each line
                        String[] fields = line.split(",");
                        String productType = fields[0];
                        String name = fields[1];
                        String description = fields[2];
                        int quantity = Integer.parseInt(fields[3]);
                        double price = Double.parseDouble(fields[4]);
                        TaxType taxType = TaxType.valueOf(fields[5]);
                        boolean canBeGifted = Boolean.parseBoolean(fields[6]);
                        double weight = Double.parseDouble(fields[7]);
                        Product finalProduct;

                        // Create the product accordingly
                        if (productType.equals("physical")) {
                            finalProduct = new PhysicalProduct(name, description, quantity, price, taxType,
                                    canBeGifted, weight);
                        } else {
                            finalProduct = new DigitalProduct(name, description, quantity, price, taxType,
                                    canBeGifted);
                        }

                        // For each 3 fields after the 7th one, it represents one coupon
                        for (int i = 8; i < fields.length; i += 3) {
                            String code = fields[i];
                            CouponType type = CouponType.valueOf(fields[i + 1]);
                            double value = Double.parseDouble(fields[i + 2]);
                            Coupon coupon = new Coupon(code, type, value);
                            coupon.setProduct(finalProduct);
                        }

                        productController.addProduct(finalProduct);
                    });

        } catch (Exception e) {
            System.out.println("Fail to load products.txt");
        }
    }

    public static void loadCart() {
        try {
            Files.lines(Paths.get("./src/main/java/com/shop/data/carts.txt"))
                    .forEach(line -> {
                        // Extract fields for the shopping cart
                        ShoppingCart cart = new ShoppingCart();
                        String[] fields = line.split(",");
                        String couponCode = fields[0];
                        String date = fields[1];
                        if (couponCode.trim().length() != 0) {
                            cart.setCoupon(couponCode);
                        }
                        cart.setDate(date);

                        // For each field after the 2nd one, it represents a product, if there is any
                        // message for a product, it will be appended by using = symbol
                        for (int i = 2; i < fields.length; i++) {
                            String[] productAndMessage = fields[i].split("=");
                            Product currentProduct = productController.getProduct(productAndMessage[0]);
                            if (productAndMessage.length == 2) {
                                List<String> messages = new ArrayList<>();
                                messages.add(productAndMessage[1]);
                                cart.addProduct(productAndMessage[0], 1, messages);
                            } else if (productAndMessage.length == 1) {
                                if (currentProduct.canBeGifted()) {
                                    cart.addProduct(productAndMessage[0], 1, new ArrayList<>());
                                } else {
                                    cart.addProduct(productAndMessage[0], 1);
                                }
                            }
                        }

                        cart.applyCoupon(couponCode);

                        shoppingCartController.addACart(cart);
                    });

            shoppingCartController.createNewCart();
        } catch (Exception e) {
            System.out.println("There is a problem reading carts.txt");
        }

    }
}
