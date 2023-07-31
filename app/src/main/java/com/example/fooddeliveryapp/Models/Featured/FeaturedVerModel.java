package com.example.fooddeliveryapp.Models.Featured;

import android.widget.ImageView;

public class FeaturedVerModel {

    int imageView;
    String name, desc, rating, timing;

    public FeaturedVerModel(int imageView, String name, String desc, String rating, String timing) {
        this.imageView = imageView;
        this.name = name;
        this.desc = desc;
        this.rating = rating;
        this.timing = timing;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
