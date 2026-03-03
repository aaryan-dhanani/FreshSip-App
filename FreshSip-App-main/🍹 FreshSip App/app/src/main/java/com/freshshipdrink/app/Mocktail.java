package com.freshshipdrink.app;

import java.io.Serializable;

public class Mocktail implements Serializable {
    public String id;
    public String name;
    public String description;
    public double price;
    public double rating;

    public Mocktail(String id, String name, String description, double price, double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }
}

