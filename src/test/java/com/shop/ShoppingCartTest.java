package com.shop;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.shop.controllers.ProductController;
import com.shop.models.Coupon;
import com.shop.models.CouponType;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.ShoppingCart;
import com.shop.models.TaxType;

public class ShoppingCartTest {
    private static ProductController productController = ProductController.getInstance();

    @BeforeAll
    public static void setUp() {
        Coupon IphoneCoupon = new Coupon("IPHONE100", CouponType.PRICE, 100);
        Coupon BookCoupon = new Coupon("HARRY10", CouponType.PERCENT, 10);
        Product product1 = new PhysicalProduct("Iphone 13", "very nice phone", 20, 1500, TaxType.NORMAL_TAX, true, 2);
        Product product2 = new DigitalProduct("Harry Porter", "E-books version of harry porter", 20, 20,
                TaxType.TAX_FREE, false);
        IphoneCoupon.setProduct(product1);
        BookCoupon.setProduct(product2);
        productController.addProduct(product1);
        productController.addProduct(product2);
    }

    @Test
    public void calculatePriceWithCouponTest() {
        // Create a cart
        ShoppingCart cart = new ShoppingCart();

        // add Iphone 13 to a cart
        cart.addProduct("Iphone 13", 2);

        // 3000 base price + 300 tax + 0.4 shipping fee
        assertEquals(3000 + 300 + (0.1 * 4), cart.calculatePrice());

        // After applying coupon, each Iphone will be reduced by 100
        cart.applyCoupon("IPHONE100");

        // 2800 base price + 300 tax + 0.4 shipping fee
        assertEquals(2800 + 300 + (0.1 * 4), cart.calculatePrice());

        // There is no harry porter book in the cart, therefore apply coupon should
        // fail
        assertFalse(cart.applyCoupon("HARRY10"));

        cart.addProduct("Harry Porter", 1);

        // After adding Harry Porter, this should be true
        assertTrue(cart.applyCoupon("HARRY10"));

        // Since a new coupon is applied, the old coupon for Iphone will be removed
        // leading to the new price
        assertEquals(3000 + 300 + (0.1 * 4) + 18, cart.calculatePrice());

        // Remove harry porter, now the coupon should be removed with the product
        cart.removeNormalItem("Harry Porter", 1);

        assertEquals(null, cart.getCoupon());
        assertEquals(0, cart.getCouponPrice());

        // Add Harry Porter again and calculate the price, now no coupon is applied so
        // the price should be normal

        cart.addProduct("Harry Porter", 1);
        assertEquals(3000 + 300 + (0.1 * 4) + 20, cart.calculatePrice());

    }

}
