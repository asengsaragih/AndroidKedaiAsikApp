package com.suncode.kedaiasik.model;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {
    private String name;
    private String phone;
    private String email;

    @Nullable
    private String storeID;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getStoreID() {
        return storeID;
    }
}
