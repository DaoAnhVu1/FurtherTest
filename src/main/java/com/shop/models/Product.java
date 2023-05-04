package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

public abstract class Product {
    private String name;
    private String description;
    private int quantity;
    private double price;
    private TaxType taxType;
    private boolean canBeGifted;

    public Product() {
    }

    public Product(String name, String description, int quantity, double price, TaxType taxType, boolean canBeGifted) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.taxType = taxType;
        this.canBeGifted = canBeGifted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public boolean canBeGifted() {
        return canBeGifted;
    }

    public double getTaxRate() {
        return switch (taxType) {
            case LUXURY_TAX -> 0.2;
            case NORMAL_TAX -> 0.1;
            default -> 0.0;
        };
    }

    public abstract String toString();

}
