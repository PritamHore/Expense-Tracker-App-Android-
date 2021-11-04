package com.webguru.expensetracker;

public class User { //POJO class
    int id, income, expense;
    private String name, date;

    public User() {
    }

    public User(int id, String name, int income, int expense, String date) {
        this.id = id;
        this.name = name;
        this.income = income;
        this.expense = expense;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
