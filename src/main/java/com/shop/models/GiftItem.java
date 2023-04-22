package com.shop.models;

public class GiftItem extends ProductItem implements ItemCanBeGifted {

    private String message = "No message";

    public GiftItem(Product product) {
        super(product);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
