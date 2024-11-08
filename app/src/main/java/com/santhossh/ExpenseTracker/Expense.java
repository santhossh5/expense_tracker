package com.santhossh.ExpenseTracker;

// Expense.java
import java.io.Serializable;
import java.util.ArrayList;

public class Expense implements Serializable {
    private String title;
    private double totalAmount;
    private ArrayList<String> participants;

    public Expense(String title, double totalAmount, ArrayList<String> participants) {
        this.title = title;
        this.totalAmount = totalAmount;
        this.participants = participants;
    }

    public String getTitle() { return title; }
    public double getTotalAmount() { return totalAmount; }
    public ArrayList<String> getParticipants() { return participants; }
}