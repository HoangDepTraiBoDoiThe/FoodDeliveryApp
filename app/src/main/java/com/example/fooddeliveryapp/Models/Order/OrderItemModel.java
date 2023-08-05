package com.example.fooddeliveryapp.Models.Order;

import com.google.firebase.database.Exclude;

public class OrderItemModel {
    int image, quantity;
    String name;
    String description;
    String rating;
    String price;
    String totalPrice;
    String timing;
    String type;
    String foodID;
    String orderID;

    public OrderItemModel() {

    }

    public OrderItemModel(String foodID, int quantity) {
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public OrderItemModel(String orderID, String foodID, int quantity) {
        this.foodID = foodID;
        this.quantity = quantity;
        this.orderID = orderID;
    }

    public OrderItemModel(String foodID, String name, String description, int image, String price, String rating, String timing, String type) {
        this.image = image;
        this.foodID = foodID;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
        this.type = type;
    }

    public OrderItemModel(String foodID, String name, String description, int image, String totalPrice, String price, String rating, String timing, String type, int quantity) {
        this.image = image;
        this.foodID = foodID;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.totalPrice = totalPrice;
        this.timing = timing;
        this.type = type;
        this.quantity = quantity;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
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
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    @Exclude
    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }
    @Exclude
    public String getQuantityText() {
        return String.valueOf(quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Exclude
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
