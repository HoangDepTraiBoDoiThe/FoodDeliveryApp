package com.example.fooddeliveryapp.Models.Order;

public class OrderItemModel {
    int image, quantity;
    String name;
    String description;
    String rating;
    String price;
    String totalPrice;
    String timing;
    String type;
    String id;

    public OrderItemModel() {

    }

    public OrderItemModel(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public OrderItemModel(String id, String name, String description, int image, String price, String rating, String timing, String type) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
        this.type = type;
    }

    public OrderItemModel(String id, String name, String description, int image, String totalPrice, String price, String rating, String timing, String type, int quantity) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.totalPrice = totalPrice;
        this.timing = timing;
        this.type = type;
        this.quantity = quantity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return String.valueOf(quantity);
    }

    public int getQuantity_int() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
