package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


abstract class Money {
    private final String category;
    private double amount;

    Money(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }
    void setAmount(double amount){
        this.amount = amount;
    };
    String getCategory(){
        return category;
    };
    double getAmount(){
        return amount;
    };
    abstract void display();
}
