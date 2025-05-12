package org.example;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import java.util.*;




public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to BudgetPal");
        Manager manager = new Manager();
        while(true){
            System.out.println("Log in");
            System.out.println("Enter your username: ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            int userInd = manager.login(username, password);
            if(userInd == -1) {
                System.out.println("Invalid username or password");
                System.out.println("Forgot your password?");
                System.out.println("1-Yes     2-No ");
                String answer = scanner.nextLine();
                if(answer.equals("1")) {
                    User tryingUser = null;
                    for(User user : manager.getUsersList()){
                        if(user.getName().equals(username)) {
                            tryingUser = user;
                        }
                    }
                    if(tryingUser != null){
                        manager.resetPassword(tryingUser);
                    }else{
                        System.out.println("We can not find that user");
                    }
                }else if(answer.equals("2")) {
                    System.out.println("Do not have an account? ");
                    System.out.println("1-Yes     2-No ");
                    String answer2 = scanner.nextLine();
                    if(answer2.equals("1"))
                    {
                        System.out.println("Sign up");
                        System.out.println("Enter your username: ");
                        String userName = scanner.nextLine();
                        System.out.println("Enter your email: ");
                        String email = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        String pass = scanner.nextLine();
                        System.out.println("Enter your current balance: ");
                        double balance = scanner.nextDouble();
                        User newUser = new User(userName, email, pass, balance);
                        if(manager.alreadyFound(newUser)){
                            System.out.println("That user already exists");
                        }else{
                            String code = Connector.generateVerificationCode();
                            String subject = "Email Verification Code";
                            String body = "Your verification code is: " + code + "\n\nEnter this code to verify your email address.";
                            if(Connector.sendEmail(newUser, subject, body)){
                                System.out.println("Please check your email for verification code");
                                System.out.println("Enter your verification code: ");
                                Scanner scan = new Scanner(System.in);
                                String codeInput = scan.nextLine();
                                if(codeInput.equals(code)){
                                    System.out.println("Signed up and Logged in");
                                    manager.getUsersList().add(newUser);
                                    showMenu(newUser, manager);
                                    break;
                                }else{
                                    System.out.println("Invalid verification code");
                                }
                            }else{
                                System.out.println("Try again later");
                            }
                        }

                    }
                }else{
                    System.out.println("Invalid choice");
                }
            }else{
                System.out.println("Logged in");
                User loggedInUser = manager.getUsersList().get(userInd);
                showMenu(loggedInUser, manager);
                break;
            }
        }
    }

    public static void showMenu(User user, Manager manager) {
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
                    + "8. Show Financial Summary\n"
                    + "9. Exit\n"
                    + "Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume the newline

            double amount;
            String type, source;
            LocalDate date;
            LocalDateTime dateTime;

            if (choice == 1) {
                System.out.print("Enter amount: ");
                amount = sc.nextDouble();
                user.addMoney(manager.createMoney("Income", amount));
                //System.out.println("Income added successfully!");

            } else if (choice == 2) {
                System.out.print("Enter amount: ");
                amount = sc.nextDouble();
                user.addMoney(manager.createMoney("Budget", amount));
                //System.out.println("Budget added successfully!");

            } else if (choice == 3) {
                System.out.print("Enter amount: ");
                amount = sc.nextDouble();
                user.addMoney(manager.createMoney("Expense", amount));
                //System.out.println("Expense added successfully!");

            } else if (choice == 4) {
                user.showMoneyFlow();

            } else if (choice == 5) {
                user.showAllIncomes();

            } else if (choice == 6) {
                System.out.print("Enter today's date (YYYY-MM-DD): ");

                System.out.println("Reminder feature is currently disabled in this version.");

            } else if (choice == 7) {
                dateTime = Manager.readDateTimeFromUser("Set reminder date (YYYY-MM-DDTHH:MM): ");
                user.setReminderDate(dateTime);
                System.out.println("Reminder date set!");

            } else if (choice == 8) {
                user.showSummary();
            } else if (choice == 9) {
                System.out.println("Exiting program...");
                manager.saveData();
                break;
            } else {
                System.out.println("Invalid option! Please choose again.");
            }
        }
    }
}

