package com.suncode.kedaiasik.model;

import java.io.Serializable;

public class FormMenu implements Serializable {
    private String storeId;
    private String menuId;

    public FormMenu(String storeId, String menuId) {
        this.storeId = storeId;
        this.menuId = menuId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getMenuId() {
        return menuId;
    }
}
