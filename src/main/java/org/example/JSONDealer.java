package org.example;

import java.util.ArrayList;

public class JSONDealer {
    private JSONDealingStrategy strategy; // Using the correct interface name

    public void setStrategy(JSONDealingStrategy newStrategy) {
        this.strategy = newStrategy;
    }

    public void dealWithJSON(ArrayList<User> usersList) {
        if (strategy == null) {
            throw new IllegalStateException("No strategy has been set for the JSONDealer");
        }
        strategy.Deal(usersList); // Calling the lowercase method name
    }
}