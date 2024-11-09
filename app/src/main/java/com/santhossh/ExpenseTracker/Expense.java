package com.santhossh.ExpenseTracker;

import java.io.Serializable;
import java.util.HashMap;

public class Expense implements Serializable {
    private int id;
    private String title;
    private double amount;
    private String payer;
    private HashMap<String, Double> participantShares;

    public Expense(String title, double amount, String payer, HashMap<String, Double> participantShares) {
        this.title = title;
        this.amount = amount;
        this.payer = payer;
        this.participantShares = participantShares;
    }

    public Expense(int id, String title, double amount, String payer, HashMap<String, Double> participantShares) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.payer = payer;
        this.participantShares = participantShares;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getPayer() { return payer; }
    public HashMap<String, Double> getParticipantShares() { return participantShares; }
}
