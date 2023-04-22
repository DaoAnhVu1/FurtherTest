package com.shop.models;

import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shop.controllers.ProductController;

public class ShoppingCart implements Comparable<ShoppingCart> {
    private List<ProductItem> itemsInCart = new ArrayList<>();
    private String coupon = null;
    private double couponPrice = 0;
    private double totalWeight;
    private String date = null;
    private ProductController productController = ProductController.getInstance();

    public ShoppingCart() {
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<ProductItem> getItems() {
        return itemsInCart;
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
            itemsInCart.add(newItem);
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
            itemsInCart.add(newItem);
        }

        productController.updateProductQuantity(productName, quantity);
        return true;
    }

    // Remove method for both general and gift item
    public boolean removeProduct(String productName, int quantity) {
        int count = 0;
        for (ProductItem productItem : itemsInCart) {
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

        // Remove i item with the provided name, could be item with any messages, the
        // user will manualy update the message if needed
        for (int i = 0; i < quantity; i++) {
            for (int j = 0; j < itemsInCart.size(); j++) {
                ProductItem currentItem = itemsInCart.get(j);
                if (currentItem.getProduct().getName().equals(productName)) {
                    itemsInCart.remove(j);
                    break;
                }
            }
        }

        productController.updateProductQuantity(productName, -quantity);
        return true;
    }

    public boolean applyCoupon(String coupon) {
        // If there is no such coupon
        if (!Coupon.getAllCoupon().containsKey(coupon)) {
            return false;
        }

        String productNameThatTiedToCurrentCoupon = Coupon.getAllCoupon().get(coupon).getProduct().getName();

        int numberOfProductInCart = 0;
        for (ProductItem productItem : itemsInCart) {
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
            couponPrice = currentCoupon.getProduct().getPrice() * currentCoupon.getValue() * numberOfProductInCart;
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
        for (ProductItem item : itemsInCart) {
            double productTax = item.getProduct().getTaxRate() * item.getProduct().getPrice();
            totalAfterTax += (productTax + item.getProduct().getPrice());
        }
        double shippingFee = this.getTotalWeight() * 0.1;

        return totalAfterTax - couponPrice + shippingFee;
    }

    public void print(boolean finalStatus) {
        int index = 1;
        for (ProductItem productItem : itemsInCart) {
            Product currentProduct = productItem.getProduct();
            System.out.println("Item " + index);
            System.out.println("Name: " + currentProduct.getName());
            System.out.println("Description:" + currentProduct.getDescription());
            System.out.println("Price: $" + currentProduct.getPrice());
            System.out.println("Tax Type: " + currentProduct.getTaxType());
            if (productItem instanceof GiftItem) {
                System.out.println("Message: " + ((GiftItem) productItem).getMessage());
            }
            System.out.println();
            index += 1;
        }

        if (finalStatus) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentDate = new Date();
            date = dateFormat.format(currentDate);
        }

        if (date != null) {
            System.out.println("Purchased Date: " + date);
        }

        System.out.println("Shipping fee: " + (totalWeight * 0.1));
        System.out.println("Total price: " + calculatePrice());

    }

    @Override
    public int compareTo(ShoppingCart o) {
        return Double.compare(this.getTotalWeight(), o.getTotalWeight());
    }

}
