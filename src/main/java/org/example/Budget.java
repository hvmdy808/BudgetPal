package org.example;

class Budget implements Money {
    private String category = "Budget";
    private double amount;
    private String type;

    public Budget(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getType() { return type; }

    public void display() {
        System.out.println("[Budget] Amount: " + amount + ", Type: " + type);
    }
}
