package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//private String name, email, pass;
//private LocalDate reminderDate;
//private List<Money> moneyFlow = new ArrayList<>();
//private double balance;

public class JSONWriter implements JSONDealingStrategy{
    @Override
    public void Deal(ArrayList<User> UsersList) {
        JSONArray arr = new JSONArray();
        for (User user : UsersList) {
            JSONObject obj = new JSONObject();
            obj.put("name", user.getName());
            System.out.println(user.getName());
            obj.put("email", user.getEmail());
            obj.put("balance", user.getBalance());
            obj.put("pass", user.getPass());
            if(user.getReminderDate() != null){
                obj.put("date",user.getReminderDate().toString());
            }

            JSONArray moneyArray = new JSONArray();
            for (Money money : user.getMoneyFlow()) {
                JSONObject moneyObj = new JSONObject();
                moneyObj.put("category", money.getCategory());
                moneyObj.put("amount", money.getAmount());

                if (money instanceof Income) {
                    Income income = (Income) money;
                    moneyObj.put("source", income.getSource());
                    moneyObj.put("date", income.getDate().toString());
                } else if (money instanceof Budget) {
                    Budget budget = (Budget) money;
                    moneyObj.put("type", budget.getType());
                } else if (money instanceof Expense) {
                    Expense expense = (Expense) money;
                    moneyObj.put("type", expense.getType());
                    moneyObj.put("date", expense.getDate().toString());
                }

                moneyArray.add(moneyObj);
            }

            // Attach moneyFlow array to the user object
            obj.put("moneyFlow", moneyArray);

            // Add user object to the array
            arr.add(obj);
        }

        // Write to file
        try (FileWriter writer = new FileWriter("Users.json")) {
            writer.write(arr.toJSONString());
            System.out.println(1111);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error writing to JSON file");
        }

    }
}

