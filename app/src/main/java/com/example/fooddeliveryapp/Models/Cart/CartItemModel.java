package com.example.fooddeliveryapp.Models.Cart;

import com.google.firebase.database.Exclude;

public class CartItemModel {
    private String cartID;
    private String cartItemID;
    private String foodID;
    private int quantity;
    private int image;
    private String name;
    private String description;
    private String rating;
    private double price;
    private double itemTotalPrice;
    private double cartTotalPrice;
    private String timing;
    private String type;

    public CartItemModel() {}

    public CartItemModel(String cartID, String foodID, int quantity) {
        this.cartID = cartID;
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public CartItemModel(String cartID, String cartItemID, String foodID, int quantity, int image, String name, String description, String rating, double price, String timing, String type) {
        this.cartID = cartID;
        this.cartItemID = cartItemID;
        this.foodID = foodID;
        this.quantity = quantity;
        this.image = image;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
        this.type = type;
        this.itemTotalPrice = quantity * price;
    }

    @Exclude
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Exclude
    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    @Exclude
    public double getCartTotalPrice() {
        return cartTotalPrice;
    }
    @Exclude
    public void setCartTotalPrice(double cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }
    @Exclude
    public String getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(String cartItemID) {
        this.cartItemID = cartItemID;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Exclude
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    @Exclude
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Exclude
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Exclude
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    @Exclude
    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
    @Exclude
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
