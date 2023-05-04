package com.shop;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.shop.models.DigitalProduct;
import com.shop.models.PhysicalProduct;
import com.shop.models.Product;
import com.shop.models.TaxType;

public class ProductTest {
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setUp() {
        product1 = new PhysicalProduct("Iphone 13", "very nice phone", 20, 1500, TaxType.NORMAL_TAX, true, 2);
        product2 = new DigitalProduct("Harry Porter", "E-books version of harry porter", 20, 20,
                TaxType.TAX_FREE, false);
        product3 = new DigitalProduct("Cant't Hurt Me", "Good book by David Goggins", 20, 15, TaxType.LUXURY_TAX,
                false);
    }

    @Test
    public void toStringWithTypePhysical() {
        assertEquals("PHYSICAL - Iphone 13", product1.toString());
    }

    @Test
    public void toStringWithTypeDigital() {
        assertEquals("DIGITAL - Harry Porter", product2.toString());
    }

    @Test
    public void taxTypeValueTest() {
        // Product 2 have tax free
        assertEquals(0, product2.getTaxRate());

        // Product 1 have normal tax
        assertEquals(0.1, product1.getTaxRate());

        // Product 3 have luxury tax
        assertEquals(0.2, product3.getTaxRate());
    }
}