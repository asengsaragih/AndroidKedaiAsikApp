package com.suncode.kedaiasik.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

@IgnoreExtraProperties
public class Menu {
    private String name;
    private double price;
    private String image;
    private int type;

    public Menu() {
    }

    public Menu(String name, double price, String image, int type) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }
}
