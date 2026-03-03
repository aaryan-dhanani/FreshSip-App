package com.freshshipdrink.app;

public class CartItem {
    public long id;
    public String mocktailId;
    public String name;
    public double price;
    public int quantity;

    public CartItem(long id, String mocktailId, String name, double price, int quantity) {
        this.id = id;
        this.mocktailId = mocktailId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

