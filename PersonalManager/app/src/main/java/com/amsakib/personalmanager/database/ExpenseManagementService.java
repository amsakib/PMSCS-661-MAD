package com.amsakib.personalmanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.amsakib.personalmanager.models.Expense;
import com.amsakib.personalmanager.models.ExpenseCategory;
import com.amsakib.personalmanager.models.Income;

import java.util.ArrayList;
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
}
