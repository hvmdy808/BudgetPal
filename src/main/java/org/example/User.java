package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String name, email, phone, pass, reminderDate;
    private List<Money> moneyFlow = new ArrayList<>();
    private double balance = 0;
    private Map<String, Double> budgetAllocations = new HashMap<>();

    public User(String name, String email, String phone, String pass) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public void setReminderDate(String date) {
        this.reminderDate = date;
    }

    public void addMoney(Money money) {
        moneyFlow.add(money);

        switch (money.getCategory()) {
            case "Income":
                balance += money.getAmount();
                break;
            case "Budget":
                Budget b = (Budget) money;
                budgetAllocations.put(b.getType(), budgetAllocations.getOrDefault(b.getType(), 0.0) + b.getAmount());
                break;
            case "Expense":
                Expense e = (Expense) money;
                String type = e.getType();
                double amt = e.getAmount();
                if (budgetAllocations.getOrDefault(type, 0.0) >= amt) {
                    budgetAllocations.put(type, budgetAllocations.get(type) - amt);
                    balance -= amt;
                    System.out.println("Expense recorded under budget: " + type);
                } else {
                    System.out.println("Warning: Not enough budget for expense type '" + type + "'. Expense still added but over budget!");
                    balance -= amt;
                }
                break;
        }
    }

    public void showMoneyFlow() {
        System.out.println("\n=== Money Flow Summary for " + name + " ===");
        for (Money m : moneyFlow) {
            m.display();
        }
    }

    public void showAllIncomes() {
        System.out.println("\n=== Income Operations ===");
        for (Money m : moneyFlow) {
            if (m.getCategory().equals("Income")) {
                m.display();
            }
        }
    }

    public void sendReminder(String todayDate) {
        System.out.println("\n=== Reminder for " + todayDate + " ===");
        boolean found = false;
        for (Money m : moneyFlow) {
            if (m.getCategory().equals("Expense")) {
                Expense e = (Expense) m;
                if (e.getDate().equals(todayDate)) {
                    System.out.println("You have an expense due today:");
                    e.display();
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No expenses are due today.");
        }
    }

    public void showSummary() {
        System.out.println("\n=== Financial Summary ===");
        System.out.println("Current Balance: " + balance);
        System.out.println("Budgets Remaining:");
        for (Map.Entry<String, Double> entry : budgetAllocations.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
