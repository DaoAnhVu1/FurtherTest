package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import java.util.HashMap;

public class Coupon {
    private String code;
    private CouponType type;
    private double value;
    private Product product;
    private static HashMap<String, Coupon> allCoupon = new HashMap<>();

    public Coupon(String code, CouponType type, double value) {
        this.code = code;
        this.type = type;
        this.value = value;
        allCoupon.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public CouponType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static HashMap<String, Coupon> getAllCoupon() {
        return allCoupon;
    }

    
}
