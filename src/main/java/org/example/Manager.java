package org.example;

public class Manager {

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


}
