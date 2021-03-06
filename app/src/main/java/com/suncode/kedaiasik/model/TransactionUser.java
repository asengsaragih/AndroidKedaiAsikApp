package com.suncode.kedaiasik.model;

import java.util.HashMap;

public class TransactionUser {
    private String storeId;
    private HashMap<String, Integer> listMenu;
    private Double total;
    private long timestamp;

    public TransactionUser() {
    }

    public TransactionUser(String storeId, HashMap<String, Integer> listMenu, Double total) {
        this.storeId = storeId;
        this.listMenu = listMenu;
        this.total = total;
        this.timestamp = System.currentTimeMillis();
    }

    public String getStoreId() {
        return storeId;
    }

    public HashMap<String, Integer> getListMenu() {
        return listMenu;
    }

    public Double getTotal() {
        return total;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
