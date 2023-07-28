package com.example.fooddeliveryapp.Models.DailyMeal;

import android.widget.ImageView;

public class DailyMealModel {

    int img;
    String name;
    String type;
    String Description;
    String Discount;

    public DailyMealModel(int img, String name, String discount, String description) {
        this.img = img;
        this.name = name;
        Description = description;
        Discount = discount;
    }

    public DailyMealModel(int img, String name, String discount, String description, String type) {
        this.img = img;
        this.name = name;
        this.type = type;
        Description = description;
        Discount = discount;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
