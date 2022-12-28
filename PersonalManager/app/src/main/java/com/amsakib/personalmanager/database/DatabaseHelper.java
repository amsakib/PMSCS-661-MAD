package com.amsakib.personalmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "personal_manager";
    private static final int DB_VERSION = 1;

    static final class TABLE_INCOME {
        public static final String NAME = "Income";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_DATE = "date";
        public static final String COL_SOURCE = "source";
    }

    public static final class TABLE_EXPENSE_CATEGORY {
        public static final String NAME = "ExpenseCategory";
        public static final String COL_NAME = "name";
    }

    public static final class TABLE_EXPENSE {
        public static final String NAME = "Expense";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_DATE = "date";
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Income (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amount REAL," +
                "date INTEGER," +
                "source TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_EXPENSE_CATEGORY.NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_EXPENSE_CATEGORY.COL_NAME + " TEXT)");

        // insert default category items
        insetExpenseCategory(db, "Food");
        insetExpenseCategory(db, "Transport");
        insetExpenseCategory(db, "Education");
        insetExpenseCategory(db, "Utility");
        insetExpenseCategory(db, "Others");

        db.execSQL("CREATE TABLE Expense (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "category_id INTEGER," +
                "date INTEGER," +
                "source TEXT," +
                "FOREIGN KEY(category_id) REFERENCES ExpenseCategory(id))");


    }

    private static void insetExpenseCategory(SQLiteDatabase db, String name) {
        ContentValues categoryValues = new ContentValues();
        categoryValues.put(TABLE_EXPENSE_CATEGORY.COL_NAME, name);
        db.insert(TABLE_EXPENSE_CATEGORY.NAME, null, categoryValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
