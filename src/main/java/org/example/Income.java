package org.example;

import java.time.LocalDate;

class Income extends Money {
    private String source;
    private LocalDate date;

    public Income(double amount, String source, LocalDate date) {
        super("Income", amount);
        this.source = source;
        this.date = date;
    }

    public String getSource() { return source; }
    public LocalDate getDate() { return date; }

    public void display() {
        System.out.println("[Income] Amount: " + getAmount() + ", Source: " + source + ", Date: " + date);
    }
}