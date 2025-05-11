package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import java.util.*;




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
