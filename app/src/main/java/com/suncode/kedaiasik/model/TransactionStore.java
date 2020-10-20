package com.suncode.kedaiasik.model;

import java.util.HashMap;

public class TransactionStore {
    private String userId;
    private String address;
    private HashMap<String, Integer> listMenu;
    private Double total;
    private long timestamp;

    public TransactionStore() {
    }

    public TransactionStore(String userId, String address, HashMap<String, Integer> listMenu, Double total) {
        this.userId = userId;
        this.address = address;
        this.listMenu = listMenu;
        this.total = total;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public HashMap<String, Integer> getListMenu() {
        return listMenu;
    }

    public Double getTotal() {
        return total;
    }

    public String getAddress() {
        return address;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
