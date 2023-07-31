package com.example.fooddeliveryapp.Models.Home;

public class HomeHorModel {

    int Image;
    String Name, id;

    public HomeHorModel(int image, String name, String id) {
        Image = image;
        Name = name;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
