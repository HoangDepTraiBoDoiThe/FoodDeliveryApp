package com.example.fooddeliveryapp.Models.Featured;

public class FeaturedModel {

    int imgage;
    String name, desc;

    public FeaturedModel(int imgage, String name, String desc) {
        this.imgage = imgage;
        this.name = name;
        this.desc = desc;
    }

    public int getImgage() {
        return imgage;
    }

    public void setImgage(int imgage) {
        this.imgage = imgage;
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
}
