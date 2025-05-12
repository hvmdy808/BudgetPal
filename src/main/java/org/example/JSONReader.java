package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//private String name, email, pass;
//private LocalDate reminderDate;
//private List<Money> moneyFlow = new ArrayList<>();
//private double balance;


public class JSONReader implements JSONDealingStrategy{
    @Override
    public void Deal(ArrayList<User> UsersList) {
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader("Users.json"));
            JSONArray list = (JSONArray) obj;
            UsersList.clear();
            for(Object user : list) {
                JSONObject userObj = (JSONObject) user;
                String name = (String)userObj.get("name");
                String email = (String)userObj.get("email");
                String pass = (String)userObj.get("pass");
                double balance = (double)userObj.get("balance");
                JSONArray money = (JSONArray) userObj.get("moneyFlow");
                User newUser = new User(name, email, pass, balance);
                String dateStr = (String) userObj.get("reminderDate");
                if (dateStr != null && !dateStr.isEmpty()) {
                    newUser.setReminderDate(LocalDateTime.parse(dateStr));
                }
                newUser.setBalance(balance);
                if(money != null) {
                    for(Object moneyItem : money) {
                        JSONObject moneyObj = (JSONObject) moneyItem;
                        String category = (String) moneyObj.get("category");
                        double amount = ((Number) moneyObj.get("amount")).doubleValue();

                        if (category.equalsIgnoreCase("Income")) {
                            String source = (String) moneyObj.get("source");
                            LocalDate incomeDate = LocalDate.parse((String) moneyObj.get("date"));
                            Income income = new Income(amount, source, incomeDate);
                            newUser.addMoney(income);

                        } else if (category.equalsIgnoreCase("Budget")) {
                            String type = (String) moneyObj.get("type");
                            Budget budget = new Budget(amount, type);
                            newUser.addMoney(budget);

                        } else if (category.equalsIgnoreCase("Expense")) {
                            String type = (String) moneyObj.get("type");
                            LocalDate expenseDate = LocalDate.parse((String) moneyObj.get("date"));
                            Expense expense = new Expense(amount, type, expenseDate);
                            newUser.addMoney(expense);

                        } else {
                            System.out.println("Unknown category: " + category);
                        }
                    }
                }
                UsersList.add(newUser);
            }
        }catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("Failed to parse users.json");
        }
    }
}
