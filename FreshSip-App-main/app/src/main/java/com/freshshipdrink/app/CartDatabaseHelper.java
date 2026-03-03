package com.freshshipdrink.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CartDatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "freshsip.db";
    static final int DB_VERSION = 2;

    // Cart table
    static final String TABLE_CART = "cart";
    static final String COL_ID = "_id";
    static final String COL_MOCKTAIL_ID = "mocktail_id";
    static final String COL_NAME = "name";
    static final String COL_PRICE = "price";
    static final String COL_QTY = "quantity";

    // Users table
    static final String TABLE_USERS = "users";
    static final String COL_USER_ID = "_id";
    static final String COL_USER_NAME = "name";
    static final String COL_USER_EMAIL = "email";
    static final String COL_USER_PHONE = "phone";
    static final String COL_USER_PASSWORD = "password";
    static final String COL_USER_LOGGED_IN = "is_logged_in";

    // Orders table
    static final String TABLE_ORDERS = "orders";
    static final String COL_ORDER_ID = "_id";
    static final String COL_ORDER_USER_ID = "user_id";
    static final String COL_ORDER_CREATED_AT = "created_at";
    static final String COL_ORDER_STATUS = "status";
    static final String COL_ORDER_TOTAL = "total";

    // Order items table
    static final String TABLE_ORDER_ITEMS = "order_items";
    static final String COL_ORDER_ITEM_ID = "_id";
    static final String COL_ORDER_ITEM_ORDER_ID = "order_id";
    static final String COL_ORDER_ITEM_MOCKTAIL_ID = "mocktail_id";
    static final String COL_ORDER_ITEM_NAME = "name";
    static final String COL_ORDER_ITEM_PRICE = "price";
    static final String COL_ORDER_ITEM_QTY = "quantity";

    CartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCart = "CREATE TABLE " + TABLE_CART + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MOCKTAIL_ID + " TEXT, "
                + COL_NAME + " TEXT, "
                + COL_PRICE + " REAL, "
                + COL_QTY + " INTEGER)";
        db.execSQL(createCart);

        String createUsers = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NAME + " TEXT, "
                + COL_USER_EMAIL + " TEXT UNIQUE, "
                + COL_USER_PHONE + " TEXT, "
                + COL_USER_PASSWORD + " TEXT, "
                + COL_USER_LOGGED_IN + " INTEGER DEFAULT 0)";
        db.execSQL(createUsers);

        String createOrders = "CREATE TABLE " + TABLE_ORDERS + " ("
                + COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ORDER_USER_ID + " INTEGER, "
                + COL_ORDER_CREATED_AT + " INTEGER, "
                + COL_ORDER_STATUS + " TEXT, "
                + COL_ORDER_TOTAL + " REAL)";
        db.execSQL(createOrders);

        String createOrderItems = "CREATE TABLE " + TABLE_ORDER_ITEMS + " ("
                + COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ORDER_ITEM_ORDER_ID + " INTEGER, "
                + COL_ORDER_ITEM_MOCKTAIL_ID + " TEXT, "
                + COL_ORDER_ITEM_NAME + " TEXT, "
                + COL_ORDER_ITEM_PRICE + " REAL, "
                + COL_ORDER_ITEM_QTY + " INTEGER)";
        db.execSQL(createOrderItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }
}

