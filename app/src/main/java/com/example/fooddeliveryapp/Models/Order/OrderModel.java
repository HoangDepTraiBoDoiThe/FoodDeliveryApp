package com.example.fooddeliveryapp.Models.Order;

import java.io.Serializable;

public class OrderModel implements Serializable {

    String orderID, orderStatus, orderDate, orderAddress, paymentMethod, userID, totalPrice;

    public OrderModel(String orderID) {
        this.orderID = orderID;
    }

    public OrderModel(String orderID, String orderStatus, String orderDate, String orderAddress, String paymentMethod, String userID) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderAddress = orderAddress;
        this.paymentMethod = paymentMethod;
        this.userID = userID;
    }

    public OrderModel(String orderID, String orderStatus, String orderDate, String orderAddress, String paymentMethod, String userID, String totalPrice) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderAddress = orderAddress;
        this.paymentMethod = paymentMethod;
        this.userID = userID;
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = String.valueOf(totalPrice);
    }
}
