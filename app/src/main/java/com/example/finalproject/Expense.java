package com.example.finalproject;

public class Expense {
    private String name;
    private double amount;
    private int type;

    public Expense(String name, double amount, int type) {
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }
}
