package com.freshshipdrink.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final CartDatabaseHelper helper;

    public OrderRepository(Context context) {
        helper = new CartDatabaseHelper(context.getApplicationContext());
    }

    public long createOrder(long userId, List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return -1;
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        long now = System.currentTimeMillis();
        double total = 0;
        for (CartItem c : cartItems) {
            total += c.price * c.quantity;
        }

        ContentValues orderValues = new ContentValues();
        orderValues.put(CartDatabaseHelper.COL_ORDER_USER_ID, userId);
        orderValues.put(CartDatabaseHelper.COL_ORDER_CREATED_AT, now);
        orderValues.put(CartDatabaseHelper.COL_ORDER_STATUS, "Preparing");
        orderValues.put(CartDatabaseHelper.COL_ORDER_TOTAL, total);

        long orderId = db.insert(CartDatabaseHelper.TABLE_ORDERS, null, orderValues);
        if (orderId == -1) {
            return -1;
        }

        for (CartItem c : cartItems) {
            ContentValues itemValues = new ContentValues();
            itemValues.put(CartDatabaseHelper.COL_ORDER_ITEM_ORDER_ID, orderId);
            itemValues.put(CartDatabaseHelper.COL_ORDER_ITEM_MOCKTAIL_ID, c.mocktailId);
            itemValues.put(CartDatabaseHelper.COL_ORDER_ITEM_NAME, c.name);
            itemValues.put(CartDatabaseHelper.COL_ORDER_ITEM_PRICE, c.price);
            itemValues.put(CartDatabaseHelper.COL_ORDER_ITEM_QTY, c.quantity);
            db.insert(CartDatabaseHelper.TABLE_ORDER_ITEMS, null, itemValues);
        }

        return orderId;
    }

    public List<Order> getOrdersForUser(long userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Order> orders = new ArrayList<>();

        Cursor c = db.query(
                CartDatabaseHelper.TABLE_ORDERS,
                null,
                CartDatabaseHelper.COL_ORDER_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                CartDatabaseHelper.COL_ORDER_CREATED_AT + " DESC"
        );

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ORDER_ID));
            long createdAt = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ORDER_CREATED_AT));
            double total = c.getDouble(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ORDER_TOTAL));
            String status = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_ORDER_STATUS));
            orders.add(new Order(String.valueOf(id), createdAt, total, status != null ? status : ""));
        }
        c.close();

        return orders;
    }
}

