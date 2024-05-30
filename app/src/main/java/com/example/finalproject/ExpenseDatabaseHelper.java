package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "ExpenseManager.db";

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EXPENSE_NAME = "expense_name";
    private static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
    private static final String COLUMN_TYPE = "type"; // New column for distinguishing income and expenses

    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EXPENSE_NAME + " TEXT,"
                + COLUMN_EXPENSE_AMOUNT + " REAL,"
                + COLUMN_TYPE + " INTEGER" // 0 for expenses, 1 for income
                + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public void addExpense(String username, String expenseName, double expenseAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EXPENSE_NAME, expenseName);
        values.put(COLUMN_EXPENSE_AMOUNT, expenseAmount);
        values.put(COLUMN_TYPE, 0); // 0 for expenses
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public void addIncome(String username, double incomeAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EXPENSE_NAME, "Income");
        values.put(COLUMN_EXPENSE_AMOUNT, incomeAmount);
        values.put(COLUMN_TYPE, 1); // 1 for income
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public List<Expense> getExpenses(String username) {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_USERNAME + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(
                        cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))
                );
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }
}
