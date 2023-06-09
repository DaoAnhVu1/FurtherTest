package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

public class PhysicalProduct extends Product {
    private double weight;

    public PhysicalProduct(String name, String description, int quantity, double price, TaxType taxType,
            boolean canBeGifted, double weight) {
        super(name, description, quantity, price, taxType, canBeGifted);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PHYSICAL - " + this.getName();
    }

}
