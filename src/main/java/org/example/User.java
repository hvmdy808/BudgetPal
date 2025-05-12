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

    // some setters and getters
    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPass(){
        return  this.pass;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setPassword(String password){
        this.pass = password;
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


    public void showSummary() {
        System.out.println("\n=== Financial Summary ===");
        System.out.println("Current Balance: " + balance);
        System.out.println("Budgets Remaining:");
        for (Map.Entry<String, Double> entry : budgetAllocations.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
