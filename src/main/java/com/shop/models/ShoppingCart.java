package com.shop.models;

/**
 * @author Dao Anh Vu - s3926187 <Group4>
 * @author Nguyen Dinh Khai - s3925921 <Group4>
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shop.controllers.ProductController;

public class ShoppingCart implements Comparable<ShoppingCart> {
    private List<ProductItem> normalItems = new ArrayList<>();
    private List<GiftItem> giftItems = new ArrayList<>();
    private String coupon = null;
    private double couponPrice = 0;
    private double totalWeight;
    private String date = null;
    private final double shippingFeeBased = 0.1;
    private ProductController productController = ProductController.getInstance();

    public ShoppingCart() {
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProductItem> getNormalItems() {
        return normalItems;
    }

    public List<GiftItem> getGiftItems() {
        return giftItems;
    }

    public List<ProductItem> getAllItems() {
        List<ProductItem> mergedList = new ArrayList<>();
        mergedList.addAll(normalItems);
        mergedList.addAll(giftItems);
        return mergedList;
    }

    // Add method for general item
    public boolean addProduct(String productName, int quantity) {
        Product chosenProduct = productController.getAllProducts().get(productName);
        if (chosenProduct == null) {
            // There is no product with such name
            return false;
        }

        if (chosenProduct.getQuantity() < quantity) {
            // There is not enough products in stock
            return false;
        }

        if (chosenProduct instanceof PhysicalProduct) {
            totalWeight += (quantity * ((PhysicalProduct) chosenProduct).getWeight());
        }

        for (int i = 0; i < quantity; i++) {
            ProductItem newItem = new ProductItem(chosenProduct);
            normalItems.add(newItem);
        }

        productController.updateProductQuantity(productName, quantity);

        return true;
    }

    // Add method for gift items
    public boolean addProduct(String productName, int quantity, List<String> messagesForGift) {
        Product chosenProduct = productController.getAllProducts().get(productName);

        if (chosenProduct == null) {
            // There is no product with such name
            return false;
        }

        if (chosenProduct.getQuantity() < quantity) {
            // There is not enough products in stock
            return false;
        }

        if (!chosenProduct.canBeGifted()) {
            // The current item cannot be gifted
            return false;
        }

        // keep track of the current message
        int indexForGift = 0;

        if (chosenProduct instanceof PhysicalProduct) {
            totalWeight += (quantity * ((PhysicalProduct) chosenProduct).getWeight());
        }

        for (int i = 0; i < quantity; i++) {
            GiftItem newItem = new GiftItem(chosenProduct);
            // If there are still messages, we will still append the message to this item
            if (indexForGift < messagesForGift.size()) {
                newItem.setMessage(messagesForGift.get(indexForGift));
                indexForGift += 1;
            }
            giftItems.add(newItem);
        }

        productController.updateProductQuantity(productName, quantity);
        return true;
    }

    // Remove method for general item
    public boolean removeNormalItem(String productName, int quantity) {
        int count = 0;
        for (ProductItem productItem : normalItems) {
            if (productItem.getProduct().getName().equals(productName)) {
                count += 1;
            }
        }

        if (count < quantity) {
            // There are not enough item in this cart
            return false;
        }

        Product chosenProduct = productController.getAllProducts().get(productName);

        if (chosenProduct instanceof PhysicalProduct) {
            totalWeight -= (quantity * ((PhysicalProduct) chosenProduct).getWeight());
        }

        // Remove i item with the provided name
        for (int i = 0; i < quantity; i++) {
            for (int j = 0; j < normalItems.size(); j++) {
                ProductItem currentItem = normalItems.get(j);
                if (currentItem.getProduct().getName().equals(productName)) {
                    normalItems.remove(j);
                    break;
                }
            }
        }

        productController.updateProductQuantity(productName, -quantity);

        boolean apply = applyCoupon(coupon);
        if (!apply)
            removeCoupon();
        return true;
    }

    public boolean removeGiftItem(String productName, int quantity, List<Integer> index) {
        boolean removed = false;
        for (Integer i : index) {
            giftItems.set(i, null);
            removed = true;
        }

        // Update weight
        Product chosenProduct = productController.getAllProducts().get(productName);

        if (chosenProduct instanceof PhysicalProduct) {
            totalWeight -= (quantity * ((PhysicalProduct) chosenProduct).getWeight());
        }

        // remove all null elements from the list
        giftItems.removeAll(Collections.singleton(null));
        productController.updateProductQuantity(productName, -quantity);

        boolean apply = applyCoupon(coupon);
        if (!apply)
            removeCoupon();
        return removed;
    }

    //

    public boolean applyCoupon(String coupon) {
        // If there is no such coupon
        if (!Coupon.getAllCoupon().containsKey(coupon)) {
            return false;
        }

        String productNameThatTiedToCurrentCoupon = Coupon.getAllCoupon().get(coupon).getProduct().getName();

        int numberOfProductInCart = 0;
        for (ProductItem productItem : getAllItems()) {
            if (productItem.getProduct().getName().equals(productNameThatTiedToCurrentCoupon)) {
                numberOfProductInCart += 1;
            }
        }

        if (numberOfProductInCart == 0) {
            // There is no item in cart that match with current coupon
            return false;
        }

        // Remove previous coupon before apply a new one
        removeCoupon();
        Coupon currentCoupon = Coupon.getAllCoupon().get(coupon);
        this.coupon = coupon;

        if (currentCoupon.getType() == CouponType.PERCENT) {
            // If it is percent , calculate the discount and then multiply by the quantity
            couponPrice = currentCoupon.getProduct().getPrice() * (currentCoupon.getValue() / 100)
                    * numberOfProductInCart;
        } else if (currentCoupon.getType() == CouponType.PRICE) {
            // If it is price, just multiply the value with the item in cart
            couponPrice = currentCoupon.getValue() * numberOfProductInCart;
        }

        return true;
    }

    public boolean removeCoupon() {
        if (this.coupon != null) {
            this.coupon = null;
            this.couponPrice = 0;
            return true;
        }
        return false;
    }

    public double calculatePrice() {
        double totalAfterTax = 0;
        for (ProductItem item : getAllItems()) {
            double productTax = item.getProduct().getTaxRate() * item.getProduct().getPrice();
            totalAfterTax += (productTax + item.getProduct().getPrice());
        }
        double shippingFee = this.getTotalWeight() * shippingFeeBased;

        double result = totalAfterTax - couponPrice + shippingFee;
        return result >= 0 ? result : 0;
    }

    public void print(boolean finalStatus) {
        int index = 1;
        double tax = 0;
        for (ProductItem productItem : getAllItems()) {
            Product currentProduct = productItem.getProduct();
            System.out.println("Item " + index);
            System.out.println("Name: " + currentProduct.getName());
            System.out.println("Description: " + currentProduct.getDescription());
            System.out.println("Price: $" + currentProduct.getPrice());
            System.out.println("Tax Type: " + currentProduct.getTaxType());
            if (currentProduct instanceof PhysicalProduct) {
                System.out.println("Weight: " + String.format("%.1f", ((PhysicalProduct) currentProduct).getWeight()));
            }
            if (productItem instanceof GiftItem) {
                System.out.println("Message: " + ((GiftItem) productItem).getMessage());
            }
            System.out.println();
            index += 1;
            tax += currentProduct.getPrice() * currentProduct.getTaxRate();
        }

        if (finalStatus) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentDate = new Date();
            date = dateFormat.format(currentDate);
        }

        if (this.getCoupon() != null) {
            System.out.println("Coupon: " + this.getCoupon());
            System.out.println("Price deducted from coupon: " + this.getCouponPrice());
        }
        System.out.println("Tax: " + tax);
        System.out.println("Shipping fee: " + String.format("%.2f", totalWeight * shippingFeeBased));
        System.out.println("Total price: " + String.format("%.2f", calculatePrice()));

        if (date != null) {
            System.out.println("Purchased Date: " + date);
        }

    }

    @Override
    public int compareTo(ShoppingCart o) {
        return Double.compare(this.getTotalWeight(), o.getTotalWeight());
    }

}
