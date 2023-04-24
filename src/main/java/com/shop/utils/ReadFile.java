package com.shop.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.shop.controllers.ProductController;
import com.shop.models.Coupon;
import com.shop.models.CouponType;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.TaxType;

public class ReadFile {
    private static ProductController productController = ProductController.getInstance();

    public static void loadProduct() {
        try {
            Files.lines(Paths.get("./src/main/java/com/shop/data/products.txt"))
                    .forEach(line -> {
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
                        if (productType.equals("physical")) {
                            finalProduct = new PhysicalProduct(name, description, quantity, price, taxType,
                                    canBeGifted, weight);
                        } else {
                            finalProduct = new DigitalProduct(name, description, quantity, price, taxType,
                                    canBeGifted);
                        }

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
}
