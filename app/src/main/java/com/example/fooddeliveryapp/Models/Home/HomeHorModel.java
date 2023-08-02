package com.example.fooddeliveryapp.Models.Home;

public class HomeHorModel {

    int Image;
    String Name, foodType;

    public HomeHorModel(int image, String name, String foodType) {
        Image = image;
        Name = name;
        this.foodType = foodType;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
