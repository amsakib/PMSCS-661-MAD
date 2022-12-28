package com.amsakib.personalmanager.models;

import android.util.Log;

import java.util.Date;

public class ExpenseHistoryItem implements Comparable<ExpenseHistoryItem> {
    Date date;
    String category;
    double amount;
    boolean isIncome;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    @Override
    public int compareTo(ExpenseHistoryItem expenseHistoryItem) {
        return  getDate().compareTo(expenseHistoryItem.getDate());
    }
}
