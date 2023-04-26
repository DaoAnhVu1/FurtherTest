package com.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.shop.controllers.ProductController;
import com.shop.controllers.ShoppingCartController;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.ShoppingCart;
import com.shop.models.TaxType;

public class ShoppingCartControllerTest {
    private static ShoppingCartController shoppingCartController = ShoppingCartController.getInstance();
    private static ProductController productController = ProductController.getInstance();

    @BeforeAll
    public static void setUp() {
        Product product1 = new PhysicalProduct("Iphone 13", "very nice phone", 20, 1500, TaxType.NORMAL_TAX, false, 2);
        Product product2 = new DigitalProduct("Harry Porter", "E-books version of harry porter", 20, 20,
                TaxType.TAX_FREE, false);
        productController.addProduct(product1);
        productController.addProduct(product2);
    }

    @Test
    public void sortCartTest() {
        shoppingCartController.createNewCart();

        // Add 2 Iphone 13 to cart number 1
        shoppingCartController.getCurrentCart().addProduct("Iphone 13", 2);

        // Create cart number 2
        shoppingCartController.createNewCart();

        // Add 3 Iphone 13 to cart number 2
        shoppingCartController.getCurrentCart().addProduct("Iphone 13", 3);

        List<ShoppingCart> sortedShoppingCarts = shoppingCartController.getSortedShoppingCarts();
        // 3 Iphone 13 with a weight of 2 should let the cart have the total weight of 6
        // which is the first element of the return list even when added second
        assertEquals(6, sortedShoppingCarts.get(0).getTotalWeight());
    }
}
