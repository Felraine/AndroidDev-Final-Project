// databaseFunctions.java
package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseFunctions extends SQLiteOpenHelper {
    public static final String DBNAME = "studyLife.db";
    private static final int DATABASE_VERSION = 3;

    public databaseFunctions(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT, email TEXT, profile_picture_path TEXT)");
        MyDB.execSQL("CREATE TABLE feedback(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, feedback TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            MyDB.execSQL("DROP TABLE IF EXISTS users");
            MyDB.execSQL("DROP TABLE IF EXISTS feedback");
            onCreate(MyDB);
        }
    }

    public Boolean insertData(String username, String password, String email, String profilePicturePath) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("profile_picture_path", profilePicturePath);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean insertFeedback(String username, String feedback) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("feedback", feedback);
        long result = MyDB.insert("feedback", null, contentValues);
        return result != -1;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }
}