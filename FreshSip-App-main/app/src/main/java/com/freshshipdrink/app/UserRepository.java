package com.freshshipdrink.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {

    private final CartDatabaseHelper helper;

    public UserRepository(Context context) {
        helper = new CartDatabaseHelper(context.getApplicationContext());
    }

    public boolean register(String name, String email, String phone, String password, StringBuilder errorOut) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor existing = db.query(
                CartDatabaseHelper.TABLE_USERS,
                null,
                CartDatabaseHelper.COL_USER_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );
        boolean emailExists = existing.moveToFirst();
        existing.close();

        if (emailExists) {
            if (errorOut != null) {
                errorOut.append("Email already registered");
            }
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(CartDatabaseHelper.COL_USER_NAME, name);
        values.put(CartDatabaseHelper.COL_USER_EMAIL, email);
        values.put(CartDatabaseHelper.COL_USER_PHONE, phone);
        values.put(CartDatabaseHelper.COL_USER_PASSWORD, password); // For demo only; not secure
        values.put(CartDatabaseHelper.COL_USER_LOGGED_IN, 1);

        long userId = db.insert(CartDatabaseHelper.TABLE_USERS, null, values);
        if (userId == -1) {
            if (errorOut != null) {
                errorOut.append("Could not save user");
            }
            return false;
        }

        // Mark all other users as logged out
        ContentValues logoutValues = new ContentValues();
        logoutValues.put(CartDatabaseHelper.COL_USER_LOGGED_IN, 0);
        db.update(
                CartDatabaseHelper.TABLE_USERS,
                logoutValues,
                CartDatabaseHelper.COL_USER_ID + " <> ?",
                new String[]{String.valueOf(userId)}
        );

        return true;
    }

    public boolean login(String email, String password, StringBuilder errorOut) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                CartDatabaseHelper.TABLE_USERS,
                null,
                CartDatabaseHelper.COL_USER_EMAIL + "=? AND " + CartDatabaseHelper.COL_USER_PASSWORD + "=?",
                new String[]{email, password},
                null,
                null,
                null
        );

        if (!c.moveToFirst()) {
            c.close();
            if (errorOut != null) {
                errorOut.append("Invalid email or password");
            }
            return false;
        }

        long userId = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_USER_ID));
        c.close();

        // Set this user as logged in and all others as logged out
        SQLiteDatabase writableDb = helper.getWritableDatabase();
        ContentValues logoutValues = new ContentValues();
        logoutValues.put(CartDatabaseHelper.COL_USER_LOGGED_IN, 0);
        writableDb.update(CartDatabaseHelper.TABLE_USERS, logoutValues, null, null);

        ContentValues loginValues = new ContentValues();
        loginValues.put(CartDatabaseHelper.COL_USER_LOGGED_IN, 1);
        writableDb.update(
                CartDatabaseHelper.TABLE_USERS,
                loginValues,
                CartDatabaseHelper.COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)}
        );

        return true;
    }

    public User getLoggedInUser() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
                CartDatabaseHelper.TABLE_USERS,
                null,
                CartDatabaseHelper.COL_USER_LOGGED_IN + "=?",
                new String[]{"1"},
                null,
                null,
                null,
                "1"
        );

        if (!c.moveToFirst()) {
            c.close();
            return null;
        }

        long id = c.getLong(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_USER_ID));
        String name = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_USER_NAME));
        String email = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_USER_EMAIL));
        String phone = c.getString(c.getColumnIndexOrThrow(CartDatabaseHelper.COL_USER_PHONE));
        c.close();

        return new User(id, name, email, phone);
    }

    public void logout() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CartDatabaseHelper.COL_USER_LOGGED_IN, 0);
        db.update(CartDatabaseHelper.TABLE_USERS, values, null, null);
    }
}

