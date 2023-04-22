package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187
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

    public void switchCanBeGifted() {
        this.canBeGifted = !canBeGifted;
    }

    public double getTaxRate() {
        switch (taxType) {
            case LUXURY_TAX:
                return 0.2;
            case NORMAL_TAX:
                return 0.1;
            default:
                return 0.0;
        }
    }

    public abstract String toString();

}
