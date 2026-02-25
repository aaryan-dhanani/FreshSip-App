package com.freshshipdrink.app;

public class Order {
    public String id;
    public long createdAt;
    public double total;
    public String status;

    public Order(String id, long createdAt, double total, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }
}

