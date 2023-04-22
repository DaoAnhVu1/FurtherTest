package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187
 */
public class DigitalProduct extends Product {
    public DigitalProduct() {
    }

    public DigitalProduct(String name, String description, int quantity, double price, TaxType taxType,
            boolean canBeGifted) {
        super(name, description, quantity, price, taxType, canBeGifted);
    }

    @Override
    public String toString() {
        return "DIGITAL - " + this.getName();
    }

}
