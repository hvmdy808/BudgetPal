package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    private String name, email, pass;
    private LocalDateTime reminderDate;
    private List<Money> moneyFlow = new ArrayList<>();
    private double balance;

    public User(String name, String email, String pass, double balance) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.balance = balance;
        //reminderDate = LocalDateTime.now();
    }

    // some setters and getters
    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPass(){
        return  this.pass;
    }

    public LocalDateTime getReminderDate(){
        return this.reminderDate;
    }

    public List<Money> getMoneyFlow() {
        return this.moneyFlow;
    }
    public double getBalance() {return this.balance;}
    

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.pass = password;
    }
    public void setBalance(double balance){this.balance = balance;}


    public void setReminderDate(LocalDateTime date) {
        this.reminderDate = date;
    }

    public void addMoney(Money money) {
        if (money.getCategory().equals("Income")) {
            if (money.getAmount() < 0) {
                System.out.println("You can not add negative income amount");
            } else {
                balance += money.getAmount();
                System.out.println("Income added. New balance: " + balance);
                moneyFlow.add(money);
            }
        }
        else if (money.getCategory().equals("Budget")) {
            Budget b = (Budget) money;
            double currentTotalBudgets = calculateTotalBudgets();
            double newTotalBudgets = currentTotalBudgets + b.getAmount();

            if (newTotalBudgets <= balance) {
                System.out.println("Budget allocated to " + b.getType() + ". Total allocated: " + newTotalBudgets);
                moneyFlow.add(money);
            } else {
                System.out.println("Error: Cannot allocate budget. Total allocations would exceed balance (" + balance + ").");
            }
        }
        else if (money.getCategory().equals("Expense")) {
            Expense e = (Expense) money;
            String type = e.getType().toLowerCase().trim();
            double amt = e.getAmount();

            double allocatedForType = calculateAllocatedForType(type);
            double freeBalance = calculateFreeBalance();

            if (allocatedForType >= amt) {
                System.out.println("Expense recorded under budget: " + type);
                moneyFlow.add(money);
                balance -= amt;
            } else if (freeBalance >= amt) {
                System.out.println("Expense recorded from free balance. Not enough budget under '" + type + "'.");
                moneyFlow.add(money);
                balance -= amt;
            } else {
                System.out.println("Error: Not enough free balance to cover the expense (" + amt + "). Expense not added.");
            }
        }
        else {
            System.out.println("Unknown money category: " + money.getCategory());
        }
    }



    public double calculateTotalBudgets() {
        double total = 0.0;
        for (Money m : moneyFlow) {
            if (m instanceof Budget) {
                total += m.getAmount();
            }
        }
        return total;
    }

    public double calculateAllocatedForType(String type) {
        double totalBudget = 0.0;
        double totalExpense = 0.0;
        for (Money m : moneyFlow) {
            if (m instanceof Budget) {
                Budget b = (Budget) m;
                if (b.getType().equals(type)) {
                    totalBudget += b.getAmount();
                }
            } else if (m instanceof Expense) {
                Expense e = (Expense) m;
                if (e.getType().equals(type)) {
                    totalExpense += e.getAmount();
                }
            }
        }
        return totalBudget - totalExpense;
    }

    public double calculateFreeBalance() {
        double totalBudgets = calculateTotalBudgets();
        return balance - totalBudgets;
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

        // First, collect all unique budget types
        ArrayList<String> budgetTypes = new ArrayList<>();
        for (Money m : moneyFlow) {
            if (m instanceof Budget) {
                Budget b = (Budget) m;
                if (!budgetTypes.contains(b.getType())) {
                    budgetTypes.add(b.getType());
                }
            }
        }

        // For each type, calculate remaining budget (allocated - expenses)
        for (String type : budgetTypes) {
            double allocated = 0.0;
            double spent = 0.0;

            for (Money m : moneyFlow) {
                if (m instanceof Budget) {
                    Budget b = (Budget) m;
                    if (b.getType().toLowerCase().trim().equals(type.toLowerCase().trim())) {
                        allocated += b.getAmount();
                    }
                } else if (m instanceof Expense) {
                    Expense e = (Expense) m;
                    if (e.getType().toLowerCase().trim().equals(type.toLowerCase().trim())) {
                        spent += e.getAmount();
                    }
                }
            }

            double remaining = allocated - spent;
            System.out.println("  " + type + ": " + remaining);
        }
    }

}
