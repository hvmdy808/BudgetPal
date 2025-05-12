package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

class Expense extends Money {
    private String type;
    private LocalDate date;

    public Expense(double amount, String type, LocalDate date) {
        super("Expense", amount);
        this.type = type;
        this.date = date;
    }

    public String getType() { return type; }
    public LocalDate getDate() { return date; }

    public void display() {
        System.out.println("[Expense] Amount: " + getAmount() + ", Type: " + type + ", Date: " + date);
    }
}
