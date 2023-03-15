package com.example.projectpmdm.models;

public class Product {
    private long id;
    private String name;
    private String description;
    private float price;
    private int qty;

    public Product() {
    }

    public Product(String name, String description, float price, int qty) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.qty = qty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
