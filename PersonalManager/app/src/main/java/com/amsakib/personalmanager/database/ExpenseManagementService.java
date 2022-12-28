package com.amsakib.personalmanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amsakib.personalmanager.models.BalanceSummary;
import com.amsakib.personalmanager.models.CategorySummary;
import com.amsakib.personalmanager.models.Expense;
import com.amsakib.personalmanager.models.ExpenseCategory;
import com.amsakib.personalmanager.models.ExpenseHistoryItem;
import com.amsakib.personalmanager.models.Income;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ExpenseManagementService {
    private DatabaseHelper _dbHelper;

    public ExpenseManagementService(Context context) {
        _dbHelper = new DatabaseHelper(context);
    }

    public void addIncome(Income income) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount", income.get_amount());
        contentValues.put("date", income.get_date().getTime());
        contentValues.put("source", income.get_source());

        db.insert("Income", null, contentValues);
    }

    @SuppressLint("Range")
    public List<Income> getAllIncome() {
        List<Income> incomes = new ArrayList<Income>() ;
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from income", null );
        cursor.moveToFirst();
        int id_column_index = cursor.getColumnIndex("id");
        int amount_col_index = cursor.getColumnIndex("amount");
        int date_col_index = cursor.getColumnIndex("date");
        int source_col_index = cursor.getColumnIndex("source");
        while(!cursor.isAfterLast()) {
            Income income = new Income();
            income.set_id(cursor.getInt(id_column_index));
            income.set_amount(cursor.getDouble(amount_col_index));
            income.set_date(new Date(cursor.getInt(date_col_index)));
            income.set_source(cursor.getString(source_col_index));
            incomes.add(income);
            cursor.moveToNext();
        }

        return incomes;
    }

    public List<ExpenseCategory> getAllExpenseCategories() {
        List<ExpenseCategory> expenseCategories = new ArrayList<ExpenseCategory>() ;
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " + DatabaseHelper.TABLE_EXPENSE_CATEGORY.NAME , null );
        cursor.moveToFirst();
        int id_column_index = cursor.getColumnIndex("id");
        int name_col_index = cursor.getColumnIndex(DatabaseHelper.TABLE_EXPENSE_CATEGORY.COL_NAME);

        while(!cursor.isAfterLast()) {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            expenseCategory.setId(cursor.getInt(id_column_index));
            expenseCategory.setName(cursor.getString(name_col_index));
            expenseCategories.add(expenseCategory);
            cursor.moveToNext();
        }

        return expenseCategories;
    }

    public boolean haveIncomeValues() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "income");
        return numRows > 0;
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_EXPENSE.COL_AMOUNT, expense.getAmount());
        contentValues.put(DatabaseHelper.TABLE_EXPENSE.COL_DATE, expense.getDate().getTime());
        contentValues.put(DatabaseHelper.TABLE_EXPENSE.COL_CATEGORY_ID, expense.getCategoryId());

        db.insert(DatabaseHelper.TABLE_EXPENSE.NAME, null, contentValues);
    }

    public BalanceSummary getBalanceSummary(Calendar calendar) {
        BalanceSummary summary = new BalanceSummary();

        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        calendar.set(Calendar.DAY_OF_MONTH, 1);


        Log.d("ExpenseManagement", calendar.getTime() + "");

        // 1. get sum of all income amount dated before this month
        String query = "SELECT SUM(amount) from " + DatabaseHelper.TABLE_INCOME.NAME + " WHERE date < " + calendar.getTime().getTime() ;
        double totalPreviousIncome = getScalerValue(db, query);

        Log.d("ExpenseManagement", "totalPreviousIncome " + totalPreviousIncome);

        // 2. get sum of all expense amount dated dated before this month
        query = "SELECT SUM(amount) from " + DatabaseHelper.TABLE_EXPENSE.NAME + " WHERE date < " + calendar.getTime().getTime();
        double totalPreviousExpense = getScalerValue(db, query);

        Log.d("ExpenseManagement", "totalPreviousExpense " + totalPreviousExpense);


        // 3. get sum of all income amount of this month
        query = "SELECT SUM(amount) from " + DatabaseHelper.TABLE_INCOME.NAME + " WHERE date >=" + calendar.getTime().getTime() ;
        double totalCurrentIncome = getScalerValue(db, query);

        Log.d("ExpenseManagement", "totalCurrentIncome " + totalCurrentIncome);
        // 4. get sum of all expense amount of this month
        query = "SELECT SUM(amount) from " + DatabaseHelper.TABLE_EXPENSE.NAME + " WHERE date >= " + calendar.getTime().getTime() ;
        double totalCurrentExpense = getScalerValue(db, query);
        Log.d("ExpenseManagement", "totalCurrentExpense " + totalCurrentExpense);

        // 5. do the calculation
        double previousBalance = totalPreviousIncome - totalPreviousExpense;
        summary.setPreviousBalance( previousBalance );
        summary.setTotalIncome(totalCurrentIncome);
        summary.setTotalExpense(totalCurrentExpense);
        summary.setBalance(previousBalance - totalCurrentExpense + totalCurrentIncome);

        return summary;
    }

    private double getScalerValue(SQLiteDatabase db, String query) {
        Cursor cursor = db.rawQuery(query, null);
        Log.d("ExpenseManagement", "Query:  " + query);
        Log.d("ExpenseManagement", "IsAfterLast:  " + cursor.isAfterLast());

        cursor.moveToFirst();
        double totalPreviousIncome = 0;
        if(!cursor.isAfterLast()) {
            totalPreviousIncome = cursor.getDouble(0);
        }
        return totalPreviousIncome;
    }

    public List<CategorySummary> getExpenseSummary(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        String query = "SELECT name, sum(amount) as sum FROM Expense INNER JOIN ExpenseCategory ON ExpenseCategory.id = Expense.category_id WHERE date > "+ calendar.getTime().getTime() +" GROUP BY category_id";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        List<CategorySummary> summaries = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            CategorySummary summary = new CategorySummary();
            summary.setName(cursor.getString(0));
            summary.setAmount(cursor.getDouble(1));
            summaries.add(summary);
            cursor.moveToNext();
        }
        return summaries;
    }

    public List<ExpenseHistoryItem> getHistoryItems() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT name, amount, date FROM Expense INNER JOIN ExpenseCategory ON ExpenseCategory.id = Expense.category_id ORDER BY date DESC" ;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        List<ExpenseHistoryItem> historyItems = new ArrayList<>();

        while(!cursor.isAfterLast()) {
            ExpenseHistoryItem item = new ExpenseHistoryItem();
            item.setIncome(false);
            item.setAmount(cursor.getDouble(1));
            item.setDate(new Date(cursor.getLong(2)));
            item.setCategory(cursor.getString(0));
            historyItems.add(item);
            cursor.moveToNext();
        }

        query = "SELECT amount, date, source FROM Income ORDER BY date DESC";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExpenseHistoryItem item = new ExpenseHistoryItem();
            item.setIncome(true);
            item.setAmount(cursor.getDouble(0));
            item.setDate(new Date(cursor.getLong(1)));
            item.setCategory(cursor.getString(2));
            historyItems.add(item);
            cursor.moveToNext();
        }

        // sort history items by date
        Collections.sort(historyItems);
        Collections.reverse(historyItems);

        return historyItems;
    }
}
