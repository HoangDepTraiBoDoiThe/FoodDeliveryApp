package com.example.fooddeliveryapp.Models.Order;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class OrderModel implements Serializable {

    String orderID, status, orderDate, orderAddress, paymentMethod, userID, totalPrice;
    int orderStatusImage;

    public OrderModel(String orderID) {
        this.orderID = orderID;
    }

    public OrderModel(String orderID, String status, String orderDate, String orderAddress, String paymentMethod, String userID, int orderStatusImage) {
        this.orderID = orderID;
        this.status = status;
        this.orderDate = orderDate;
        this.orderAddress = orderAddress;
        this.paymentMethod = paymentMethod;
        this.userID = userID;
        this.orderStatusImage = orderStatusImage;
    }

    public OrderModel(String orderID, String status, String orderDate, String orderAddress, String paymentMethod, String userID) {
        this.orderID = orderID;
        this.status = status;
        this.orderDate = orderDate;
        this.orderAddress = orderAddress;
        this.paymentMethod = paymentMethod;
        this.userID = userID;
    }

    public OrderModel(String orderID, String status, String orderDate, String orderAddress, String paymentMethod, String userID, String totalPrice) {
        this.orderID = orderID;
        this.status = status;
        this.orderDate = orderDate;
        this.orderAddress = orderAddress;
        this.paymentMethod = paymentMethod;
        this.userID = userID;
        this.totalPrice = totalPrice;
    }
    @Exclude
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    @Exclude
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = String.valueOf(totalPrice);
    }
    @Exclude
    public int getOrderStatusImage() {
        return orderStatusImage;
    }

    public void setOrderStatusImage(int orderStatusImage) {
        this.orderStatusImage = orderStatusImage;
    }
}
