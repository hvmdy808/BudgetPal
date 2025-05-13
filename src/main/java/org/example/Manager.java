package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Manager {
    private Connector connector;
    private JSONDealer dealer;
    private ArrayList<User> usersList = new ArrayList<>();

    public static LocalDateTime readDateTimeFromUser(String prompt) {
        while (true) {
            System.out.print(prompt);
            Scanner scanner = new Scanner(System.in);
            String dateStr = scanner.nextLine();
            try {
                return LocalDateTime.parse(dateStr); // expects YYYY-MM-DD
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DDTHH:MM");
            }
        }
    }

    private static LocalDate readDateFromUser(String prompt) {
        while (true) {
            System.out.print(prompt);
            Scanner scanner = new Scanner(System.in);
            String dateStr = scanner.nextLine();
            try {
                return LocalDate.parse(dateStr); // expects YYYY-MM-DD
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }


    public Manager() {
        JSONDealer dealer = new JSONDealer();
        dealer.setStrategy(new JSONReader());
        dealer.dealWithJSON(usersList);
    }

    public void saveData(){
        JSONDealer dealer = new JSONDealer();
        dealer.setStrategy(new JSONWriter());
        dealer.dealWithJSON(usersList);
    }
    
    public Money createMoney(String category, double amount) {
        Scanner scanner = new Scanner(System.in);
        if(category.equalsIgnoreCase("income")) {
            System.out.print("Enter source of income: ");
            String source = scanner.nextLine();
            LocalDate date = readDateFromUser("Enter income date (YYYY-MM-DD): ");
            return new Income(amount, source, date);
        }
        else if(category.equalsIgnoreCase("expense")) {
            System.out.print("Enter purpose of expense: ");
            String type = scanner.nextLine();
            LocalDate date = readDateFromUser("Enter income date (YYYY-MM-DD): ");
            return new Expense(amount, type, date);
        }
        else if (category.equalsIgnoreCase("budget")) {
            System.out.print("Enter budget type (for what do you want to budget): ");
            String type = scanner.nextLine();
            return new Budget(amount, type);
        }
        throw new IllegalArgumentException("Unknown money category: " + category);
    }

    public boolean alreadyFound(User user) {
        for (User existingUser : usersList) {
            if (existingUser.getEmail().equals(user.getEmail()) ||
                    existingUser.getName().equals(user.getName()) ||
                    existingUser.getPass().equals(user.getPass())) {
                return true; // one of the fields already exists
            }
        }
        return false;
    }


    public int login(String name, String password) {
        for(User user : usersList) {
            if(name.equals(user.getName()) && password.equals(user.getPass())) {
                return usersList.indexOf(user);
            }
        }
        return -1;
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    ///////////////////////////////////////////



//    public Money editMoney(Money oldMoney, User user, double newAmount, String newType, String newDate) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//
//        if (oldMoney == null) {
//            throw new IllegalArgumentException("Original money entry cannot be null");
//        }
//
//        // Validate amount
//        if (newAmount <= 0) {
//            throw new IllegalArgumentException("Amount must be positive");
//        }
//
//
//        int index = -1;
//        for (int i = 0; i < user.getMoneyFlow().size(); i++) {
//            if (user.getMoneyFlow().get(i) == oldMoney) {
//                index = i;
//                break;
//            }
//        }
//
//        if (index == -1) {
//            throw new IllegalArgumentException("Money entry not found for this user");
//        }
//
//        // First, reverse the financial effects of the old money entry
//        // We need to do this manually since we don't have a removeMoney method in User
//        if (oldMoney instanceof Income) {
//            // Subtract the old income amount from balance
//            user.modifyAccountBalance(-oldMoney.getAmount());
//        } else if (oldMoney instanceof Budget) {
//            Budget b = (Budget) oldMoney;
//            // Subtract the old budget allocation
//            user.updateBudgetAllocation(b.getType(), -b.getAmount());
//        } else if (oldMoney instanceof Expense) {
//            Expense e = (Expense) oldMoney;
//            // Add back the expense amount to balance
//            user.modifyAccountBalance(e.getAmount());
//            // Add back to budget allocation if applicable
//            user.updateBudgetAllocation(e.getType(), e.getAmount());
//        }
//
//        // Create a new Money object with the updated values
//        Money newMoney = null;
//
//        if (oldMoney instanceof Income) {
//            newMoney = new Income(newAmount, newType, newDate);
//        } else if (oldMoney instanceof Expense) {
//            newMoney = new Expense(newAmount, newType, newDate);
//        } else if (oldMoney instanceof Budget) {
//            newMoney = new Budget(newAmount, newType);
//        } else {
//            throw new IllegalArgumentException("Unknown money type");
//        }
//
//        // Replace the old object with the new one in the user's moneyFlow
//        user.getMoneyFlow().set(index, newMoney);
//
//        // Apply the financial effects of the new money entry
//        // Using addMoney would add the object to the list again, so we need to manually update
//        if (newMoney instanceof Income) {
//            // Add the new income amount to balance
//            user.updateBalance(newMoney.getAmount());
//        } else if (newMoney instanceof Budget) {
//            Budget b = (Budget) newMoney;
//            // Add the new budget allocation
//            user.updateBudgetAllocation(b.getType(), b.getAmount());
//        } else if (newMoney instanceof Expense) {
//            Expense e = (Expense) newMoney;
//            // Subtract the new expense amount from balance
//            // Check if enough budget is available
//            double availableBudget = user.getBudgetForType(e.getType());
//            if (availableBudget >= e.getAmount()) {
//                // Enough budget, update as normal
//                user.updateBudgetAllocation(e.getType(), -e.getAmount());
//                user.updateBalance(-e.getAmount());
//                System.out.println("Expense updated under budget: " + e.getType());
//            } else {
//                // Not enough budget
//                user.updateBudgetAllocation(e.getType(), -availableBudget);
//                user.updateBalance(-e.getAmount());
//                System.out.println("Warning: Not enough budget for expense type '" + e.getType() + "'. Expense still updated but over budget!");
//            }
//        }
//
//        System.out.println("Money entry updated successfully");
//        return newMoney;
//    }

//    public void removeMoney(Money money, User user) {
//        if (user == null || money == null) {
//            throw new IllegalArgumentException("User and money must not be null");
//        }
//
//        if (money instanceof Income) {
//            // Directly affect balance because incomes are accumulated in balance
//            user.setBalance(user.getBalance() - money.getAmount());
//        }
//
//        // For Budget and Expense:
//        // No direct change needed because your logic dynamically recalculates budgets and expenses
//        // from user.getMoneyFlow() when displaying or adding new items.
//
//        // Remove the money from flow
//        if (user.getMoneyFlow().remove(money)) {
//            System.out.println("Money entry removed successfully");
//        } else {
//            System.out.println("Error: Money entry not found in user's records");
//        }
//    }


    public void resetPassword(User user) {
        if (user == null || !usersList.contains(user)) {
            throw new IllegalArgumentException("Invalid user");
        }
        String verificationCode = Connector.generateVerificationCode();
        String subject = "Password reset";
        String body = "Your password reset code is: " + verificationCode + "\n\nUse this code to reset your password.";
        boolean emailSent = Connector.sendEmail(user.getEmail(), subject, body);

        if (!emailSent) {
            throw new RuntimeException("Failed to send password reset email");
        }
        System.out.println("Password reset email sent to: " + user.getEmail());
        System.out.println("Please check your email for verification code");
        System.out.println("Enter your verification code: ");
        Scanner scanner = new Scanner(System.in);
        String codeInput = scanner.nextLine();
        if(verificationCode.equals(codeInput)) {
            System.out.println("Enter new password: ");
            String newPassword = scanner.nextLine();
            user.setPassword(newPassword);
            System.out.println("Password reset successfully");
        }else{
            System.out.println("Invalid verification code");
        }
    }


    public static class SendEmailJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            String userEmail = context.getJobDetail().getJobDataMap().getString("email");
            String expenseDescription = context.getJobDetail().getJobDataMap().getString("description");
            Connector.sendEmail(userEmail, "Expense Reminder", "Reminder: Your expense \"" + expenseDescription + "\" is due now.");
        }
    }


    public void scheduleReminder(String userEmail, String expenseDescription, LocalDateTime userInputDateTime) throws SchedulerException {
        Date remindDate = Date.from(userInputDateTime.atZone(ZoneId.of("Africa/Cairo")).toInstant());

        JobDetail job = JobBuilder.newJob(SendEmailJob.class)
                .withIdentity("reminderJob", "reminderGroup")
                .usingJobData("email", userEmail)
                .usingJobData("description", expenseDescription)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("reminderTrigger", "reminderGroup")
                .startAt(remindDate)
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

        System.out.println("Reminder scheduled for " + remindDate);
    }




//        if (user == null || user.getReminderDate() == null) {
//            return;
//        }
//
//        LocalDateTime reminderDate = user.getReminderDate();
//        System.out.println("\n=== Reminder for " + user.getName() + " on " + reminderDate + " ===");
//
//        // Using connector to send actual reminders (email, SMS, etc.)
//        if (connector != null) {
//            // connector.sendNotification(user.getEmail(), "Reminder", "You have expenses due today");
//        }

}