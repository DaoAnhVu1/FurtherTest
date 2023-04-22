package com.shop.models;

public class ProductItem {
    private Product product;
    private double weight = 0;

    public ProductItem(Product product) {
        this.product = product;

        if (product instanceof PhysicalProduct) {
            this.weight = ((PhysicalProduct) product).getWeight();
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
