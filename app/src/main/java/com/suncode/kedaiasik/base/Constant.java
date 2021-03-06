package com.suncode.kedaiasik.base;

public class Constant {

    public static final String ERR_AUTH_BAD_EMAIL = "The email address is badly formatted.";
    public static final String ERR_AUTH_EMAIL_USED = "The email address is already in use by another account.";
    public static final String ERR_AUTH_WRONG_PASS = "The password is invalid or the user does not have a password.";
    public static final String ERR_AUTH_WRONG_EMAIL = "There is no user record corresponding to this identifier. The user may have been deleted.";


    public static final String USER = "User";
    public static final String STORE = "Store";
    public static final String MENU = "Menu";
    public static final String TRANSACTION = "Transaction";
    public static final String STORE_ID = "storeID";
    public static final String INTENT_TO_STORE = "intent_store_id_to_store_activity";
    public static final String INTENT_TO_FORM_MENU = "intent_store_id_to_form_menu";
    public static final String INTENT_TO_HISTORY = "intent_store_id_to_history";

    //kategori makanan dan minuman
    public static final int MENU_CATEGORY_FOOD = 1;
    public static final int MENU_CATEGORY_DRINK = 2;

    public static final String INTENT_TO_ORDER_ID = "id_store";

    public static final String INTENT_TO_TRANSACTION_ID_STORE = "intent_transaction_id";
    public static final String INTENT_TO_TRANSACTION_HASHMAP = "intent_transaction_map";
    public static final String INTENT_TO_TRANSACTION_TOTAL = "intent_transaction_total";

    //reques code permission
    public static final int REQUEST_PERMISSION_WRITE_STORAGE = 2;
    public static final int REQUEST_PERMISSION_READ_STORAGE = 3;

    public static final int REQUEST_PICK_IMAGE = 18;
}
