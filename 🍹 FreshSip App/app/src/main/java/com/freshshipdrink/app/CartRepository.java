package com.freshshipdrink.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {

    private final CartDatabaseHelper helper;

    public CartRepository(Context context) {
        helper = new CartDatabaseHelper(context.getApplicationContext());
    }

    public void addOrIncrement(Mocktail mocktail, int quantityToAdd) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(
                CartDatabaseHelper.TABLE_CART,
                null,
                CartDatabaseHelper.COL_MOCKTAIL_ID + "=?",
                new String[]{mocktail.id},
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            int existingQty = c.getInt(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_QTY));
            long id = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ID));
            ContentValues values = new ContentValues();
            values.put(CartDatabaseHelper.COL_QTY, existingQty + quantityToAdd);
            db.update(CartDatabaseHelper.TABLE_CART, values,
                    CartDatabaseHelper.COL_ID + "=?",
                    new String[]{String.valueOf(id)});
        } else {
            ContentValues values = new ContentValues();
            values.put(CartDatabaseHelper.COL_MOCKTAIL_ID, mocktail.id);
            values.put(CartDatabaseHelper.COL_NAME, mocktail.name);
            values.put(CartDatabaseHelper.COL_PRICE, mocktail.price);
            values.put(CartDatabaseHelper.COL_QTY, quantityToAdd);
            db.insert(CartDatabaseHelper.TABLE_CART, null, values);
        }
        c.close();
    }

    public List<CartItem> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(CartDatabaseHelper.TABLE_CART, null,
                null, null, null, null, null);
        List<CartItem> items = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ID));
            String mocktailId = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_MOCKTAIL_ID));
            String name = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_NAME));
            double price = c.getDouble(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_PRICE));
            int qty = c.getInt(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_QTY));
            items.add(new CartItem(id, mocktailId, name, price, qty));
        }
        c.close();
        return items;
    }

    public void updateQuantity(long id, int quantity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CartDatabaseHelper.COL_QTY, quantity);
        db.update(CartDatabaseHelper.TABLE_CART, values,
                CartDatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public void remove(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(CartDatabaseHelper.TABLE_CART,
                CartDatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public void clear() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(CartDatabaseHelper.TABLE_CART, null, null);
    }
}

