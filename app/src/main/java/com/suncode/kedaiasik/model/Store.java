package com.suncode.kedaiasik.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Store implements Serializable {
    private String name;
    private String open;
    private String close;
    private Menu menu;
    private String userId;

    public Store() {
    }

    public Store(String name, String open, String close, String uid) {
        this.name = name;
        this.open = open;
        this.close = close;
        this.menu = null;
        this.userId = uid;
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

    public String getUserId() {
        return userId;
    }
}
