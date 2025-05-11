package org.example;

class Expense implements Money {
    private String category = "Expense";
    private double amount;
    private String type;
    private String date;

    public Expense(double amount, String type, String date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getDate() { return date; }

    public void display() {
        System.out.println("[Expense] Amount: " + amount + ", Type: " + type + ", Date: " + date);
    }
}
