package com.suncode.kedaiasik.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Store implements Serializable {
    private String name;
    private String open;
    private String close;
    private Menu menu;

    public Store() {
    }

    public Store(String name, String open, String close) {
        this.name = name;
        this.open = open;
        this.close = close;
        this.menu = null;
    }

    public String getName() {
        return name;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public Menu getMenu() {
        return menu;
    }
}
