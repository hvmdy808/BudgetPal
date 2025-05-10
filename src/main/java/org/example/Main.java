package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import java.util.*;

interface Money {
    String getCategory();
    double getAmount();
    void display();
}

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

public class Main {
    public static void main(String[] args) {
        User user = new User("John", "john@example.com", "1234567890", "password");
        showMenu(user);
    }

    public static void showMenu(User user) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:\n"
                    + "1. Add Income\n"
                    + "2. Add Budget\n"
                    + "3. Add Expense\n"
                    + "4. View All Money Flow\n"
                    + "5. View Only Income\n"
                    + "6. View Expenses with Reminder\n"
                    + "7. Set Reminder Date\n"
                    + "8. Exit\n"
                    + "9. Show Financial Summary\n"
                    + "Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            double amount;
            String type, date, source;

            switch (choice) {
                case 1:
                    System.out.print("Enter amount, source, date: ");
                    amount = sc.nextDouble();
                    source = sc.next();
                    date = sc.next();
                    user.addMoney(new Income(amount, source, date));
                    System.out.println("Income added successfully!");
                    break;
                case 2:
                    System.out.print("Enter amount, budget type: ");
                    amount = sc.nextDouble();
                    type = sc.next();
                    user.addMoney(new Budget(amount, type));
                    System.out.println("Budget added successfully!");
                    break;
                case 3:
                    System.out.print("Enter amount, expense type, date: ");
                    amount = sc.nextDouble();
                    type = sc.next();
                    date = sc.next();
                    user.addMoney(new Expense(amount, type, date));
                    System.out.println("Expense added successfully!");
                    break;
                case 4:
                    user.showMoneyFlow();
                    break;
                case 5:
                    user.showAllIncomes();
                    break;
                case 6:
                    System.out.print("Enter today's date (e.g., 2025-05-09): ");
                    date = sc.next();
                    user.sendReminder(date);
                    break;
                case 7:
                    System.out.print("Set reminder date: ");
                    date = sc.next();
                    user.setReminderDate(date);
                    break;
                case 8:
                    System.out.println("Exiting program...");
                    return;
                case 9:
                    user.showSummary();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
