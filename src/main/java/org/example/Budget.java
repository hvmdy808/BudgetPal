package org.example;

class Budget extends Money {
    private String type;

    public Budget(double amount, String type) {
        super("Budget", amount);
        this.type = type;
    }

    public String getType() { return type; }

    @Override
    public void display() {
        System.out.println("[Budget] Amount: " + getAmount() + ", Type: " + type);
    }
}
