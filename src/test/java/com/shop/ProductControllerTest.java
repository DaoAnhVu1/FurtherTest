package com.shop;

/**
 * @author Group 4
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.shop.controllers.ProductController;
import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.TaxType;

public class ProductControllerTest {
    private static ProductController productController = ProductController.getInstance();

    @BeforeAll
    public static void setUp() {
        Product product1 = new PhysicalProduct("Iphone 13", "very nice phone", 20, 1500, TaxType.NORMAL_TAX, true, 2);
        Product product2 = new DigitalProduct("Harry Porter", "E-books version of harry porter", 20, 20,
                TaxType.TAX_FREE, false);
        productController.addProduct(product1);
        productController.addProduct(product2);
    }

    @Test
    public void addRemoveGetTest() {
        Product product3 = new PhysicalProduct("Ipad", "Up to date tablet", 30, 900, TaxType.LUXURY_TAX, true, 10);
        productController.addProduct(product3);
        assertEquals(3, productController.getAllProducts().size());
        assertEquals("Ipad", productController.getProduct("Ipad").getName());
        productController.removeProduct("Ipad");
        assertEquals(2, productController.getAllProducts().size());
    }

    @Test
    public void quantityUpdateTest() {
        ProductController productController = ProductController.getInstance();
        productController.updateProductQuantity("Iphone 13", 1);
        assertEquals(19, productController.getProduct("Iphone 13").getQuantity());
        productController.updateProductQuantity("Iphone 13", 10);
        assertEquals(9, productController.getProduct("Iphone 13").getQuantity());
        productController.updateProductQuantity("Iphone 13", -5);
        assertEquals(14, productController.getProduct("Iphone 13").getQuantity());
    }
}