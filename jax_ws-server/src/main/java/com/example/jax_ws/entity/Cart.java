package com.example.jax_ws.entity;

public class Cart {
    private int id;
    private int product_Id;
    private double totalPrice;
    private int quantity;

    public Cart() {
    }

    public Cart(int id, int product_Id, double totalPrice, int quantity) {
        this.id = id;
        this.product_Id = product_Id;
        this.totalPrice = totalPrice;
        this.quantity = quantity;

    }

    public Cart(int product_Id, double totalPrice, int quantity) {
        this.product_Id = product_Id;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(int product_Id) {
        this.product_Id = product_Id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
