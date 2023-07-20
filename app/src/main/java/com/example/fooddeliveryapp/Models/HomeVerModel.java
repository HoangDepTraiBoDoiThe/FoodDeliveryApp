package com.example.fooddeliveryapp.Models;

public class HomeVerModel {

    int Image;
    String Name;
    String Timing;
    String Rating;
    String Price;

    public HomeVerModel(int image, String name, String timing, String rating, String price) {
        Image = image;
        Name = name;
        Timing = timing;
        Rating = rating;
        Price = price;
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

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String timing) {
        Timing = timing;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
