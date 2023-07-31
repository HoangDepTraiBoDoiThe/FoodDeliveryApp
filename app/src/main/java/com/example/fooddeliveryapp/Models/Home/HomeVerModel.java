package com.example.fooddeliveryapp.Models.Home;

public class HomeVerModel {

    int Image;
    String Name;
    String Timing;
    String Rating;
    String Price;

    String type;

    public HomeVerModel(int image, String name, String timing, String rating, String price) {
        Image = image;
        Name = name;
        Timing = timing;
        Rating = rating;
        Price = price;
    }

    public HomeVerModel(int image, String name, String timing, String rating, String price, String type) {
        Image = image;
        Name = name;
        Timing = timing;
        Rating = rating;
        Price = price;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
