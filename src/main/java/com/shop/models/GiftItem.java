package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

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
