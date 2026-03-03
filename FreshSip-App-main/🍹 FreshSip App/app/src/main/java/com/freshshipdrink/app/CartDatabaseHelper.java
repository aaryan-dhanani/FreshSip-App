package com.freshshipdrink.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CartDatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "freshsip_cart.db";
    static final int DB_VERSION = 1;

    static final String TABLE_CART = "cart";
    static final String COL_ID = "_id";
    static final String COL_MOCKTAIL_ID = "mocktail_id";
    static final String COL_NAME = "name";
    static final String COL_PRICE = "price";
    static final String COL_QTY = "quantity";

    CartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_CART + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MOCKTAIL_ID + " TEXT, "
                + COL_NAME + " TEXT, "
                + COL_PRICE + " REAL, "
                + COL_QTY + " INTEGER)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }
}

