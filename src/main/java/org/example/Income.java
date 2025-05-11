package org.example;

class Income implements Money {
    private String category = "Income";
    private double amount;
    private String source;
    private String date;

    public Income(double amount, String source, String date) {
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getSource() { return source; }
    public String getDate() { return date; }

    public void display() {
        System.out.println("[Income] Amount: " + amount + ", Source: " + source + ", Date: " + date);
    }
}