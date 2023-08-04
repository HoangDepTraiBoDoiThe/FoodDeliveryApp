package com.example.fooddeliveryapp.Models.Home;

public class HomeVerModel {

    int image;
    String name;
    String description;
    String rating;
    double price;
    String timing;
    String type;
    String id;
    int curQuan = 1;

    boolean isFavorite;

    public HomeVerModel() {

    }

    public HomeVerModel(String id, String name, String description, int image, double price, String rating, String timing, String type) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
        this.type = type;
    }

    public HomeVerModel(String id, String name, String description, int image, double price, String rating, String timing, String type, boolean isFavorite) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.timing = timing;
        this.type = type;
        this.isFavorite = isFavorite;
    }

    public int getCurQuan() {
        return curQuan;
    }

    public void setCurQuan(int curQuan) {
        this.curQuan = curQuan;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
