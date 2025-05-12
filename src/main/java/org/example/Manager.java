package org.example;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private Connector connector;
    private JSONDealer dealer;
    private ArrayList<User> usersList = new ArrayList<>();

    // Constructor
    public Manager() {
        this.dealer = new JSONDealer();
    }

    public Manager(Connector connector, JSONDealer dealer) {
        this.connector = connector;
        this.dealer = dealer;
    }
    
    public Money createMoney(String category) {
        switch (category.toLowerCase()) {
            case "income":
                return new Income(0, "", "");
            case "expense":
                return new Expense(0, "", "");
            case "budget":
                return new Budget(0, "");
            default:
                throw new IllegalArgumentException("Unknown money category: " + category);
        }
    }

    public boolean alreadyFound(User user) {
        return usersList.contains(user);
    }

    public int login(User userToCheck) {
        if(alreadyFound(userToCheck)){
            return usersList.indexOf(userToCheck);

        }else{
            // return -1 if user does not exist
            return -1;
        }
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    ///////////////////////////////////////////



    public Money editMoney(Money oldMoney, User user, double newAmount, String newType, String newDate) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (oldMoney == null) {
            throw new IllegalArgumentException("Original money entry cannot be null");
        }

        // Validate amount
        if (newAmount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }


        int index = -1;
        for (int i = 0; i < user.getMoneyFlow().size(); i++) {
            if (user.getMoneyFlow().get(i) == oldMoney) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("Money entry not found for this user");
        }

        // First, reverse the financial effects of the old money entry
        // We need to do this manually since we don't have a removeMoney method in User
        if (oldMoney instanceof Income) {
            // Subtract the old income amount from balance
            user.modifyAccountBalance(-oldMoney.getAmount());
        } else if (oldMoney instanceof Budget) {
            Budget b = (Budget) oldMoney;
            // Subtract the old budget allocation
            user.updateBudgetAllocation(b.getType(), -b.getAmount());
        } else if (oldMoney instanceof Expense) {
            Expense e = (Expense) oldMoney;
            // Add back the expense amount to balance
            user.modifyAccountBalance(e.getAmount());
            // Add back to budget allocation if applicable
            user.updateBudgetAllocation(e.getType(), e.getAmount());
        }

        // Create a new Money object with the updated values
        Money newMoney = null;

        if (oldMoney instanceof Income) {
            newMoney = new Income(newAmount, newType, newDate);
        } else if (oldMoney instanceof Expense) {
            newMoney = new Expense(newAmount, newType, newDate);
        } else if (oldMoney instanceof Budget) {
            newMoney = new Budget(newAmount, newType);
        } else {
            throw new IllegalArgumentException("Unknown money type");
        }

        // Replace the old object with the new one in the user's moneyFlow
        user.getMoneyFlow().set(index, newMoney);

        // Apply the financial effects of the new money entry
        // Using addMoney would add the object to the list again, so we need to manually update
        if (newMoney instanceof Income) {
            // Add the new income amount to balance
            user.updateBalance(newMoney.getAmount());
        } else if (newMoney instanceof Budget) {
            Budget b = (Budget) newMoney;
            // Add the new budget allocation
            user.updateBudgetAllocation(b.getType(), b.getAmount());
        } else if (newMoney instanceof Expense) {
            Expense e = (Expense) newMoney;
            // Subtract the new expense amount from balance
            // Check if enough budget is available
            double availableBudget = user.getBudgetForType(e.getType());
            if (availableBudget >= e.getAmount()) {
                // Enough budget, update as normal
                user.updateBudgetAllocation(e.getType(), -e.getAmount());
                user.updateBalance(-e.getAmount());
                System.out.println("Expense updated under budget: " + e.getType());
            } else {
                // Not enough budget
                user.updateBudgetAllocation(e.getType(), -availableBudget);
                user.updateBalance(-e.getAmount());
                System.out.println("Warning: Not enough budget for expense type '" + e.getType() + "'. Expense still updated but over budget!");
            }
        }

        System.out.println("Money entry updated successfully");
        return newMoney;
    }

    public void removeMoney(Money money, User user) {
        if (user == null || money == null) {
            throw new IllegalArgumentException("User and money must not be null");
        }
        // Reverse financial effects before removing
        if (money instanceof Income) {
            user.updateBalance(-money.getAmount());
        } else if (money instanceof Budget) {
            Budget b = (Budget) money;
            user.updateBudgetAllocation(b.getType(), -b.getAmount());
        } else if (money instanceof Expense) {
            Expense e = (Expense) money;
            user.updateBalance(e.getAmount());
            user.updateBudgetAllocation(e.getType(), e.getAmount());
        }

        // Remove the money object from user's money flow
        user.getMoneyFlow().remove(money);
        System.out.println("Money entry removed successfully");
    }
    

    
    
   
 

    
    


    public void resetPassword(User user) {
        if (user == null || !usersList.contains(user)) {
            throw new IllegalArgumentException("Invalid user");
        }

        String verificationCode = Connector.generateVerificationCode();
        boolean emailSent = Connector.sendVerificationEmail(user.getEmail(), verificationCode);

        if (!emailSent) {
            throw new RuntimeException("Failed to send password reset email");
        }

        // In a real system, store the verification code and timestamp
        // Wait for user to enter the code and verify it matches
        // Then allow setting new password

        System.out.println("Password reset email sent to: " + user.getEmail());
        System.out.println("Please check your email for verification code");
    }
    


    public void sendReminder(User user) {
        if (user == null || user.getReminderDate() == null) {
            return;
        }

        String reminderDate = user.getReminderDate();
        System.out.println("\n=== Reminder for " + user.getName() + " on " + reminderDate + " ===");

        // Using connector to send actual reminders (email, SMS, etc.)
        if (connector != null) {
            // connector.sendNotification(user.getEmail(), "Reminder", "You have expenses due today");
        }
    }
}