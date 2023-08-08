package com.example.fooddeliveryapp.Models.Cart;

public class CartModel {

    private String userID;

    public CartModel(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
