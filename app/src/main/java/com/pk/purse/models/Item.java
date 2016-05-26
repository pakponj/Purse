package com.pk.purse.models;

public class Item {

    private int quantity;
    private double price;

    public Item(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
