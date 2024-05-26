package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class databaseFunctions extends SQLiteOpenHelper {
    public static final String DBNAME = "studyLife.db";
    private static final int DATABASE_VERSION = 5;

    public databaseFunctions(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT, email TEXT, profile_picture_path TEXT)");
        MyDB.execSQL("CREATE TABLE feedback(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, feedback TEXT)");
        MyDB.execSQL("CREATE TABLE notes(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, title TEXT, content TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            MyDB.execSQL("DROP TABLE IF EXISTS users");
            MyDB.execSQL("DROP TABLE IF EXISTS notes");
            MyDB.execSQL("DROP TABLE IF EXISTS feedback");
            onCreate(MyDB);
        }
    }

    public boolean insertData(String username, String password, String email, String profilePicturePath) {
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

    public boolean insertNote(String username, String title, String content) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("title", title);
        contentValues.put("content", content);
        long result = MyDB.insert("notes", null, contentValues);
        MyDB.close();
        return result != -1;
    }

    public List<Note> fetchNotesByUsername(String username) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM notes WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Note note = new Note();
                note.setUsername(username);
                note.setTitle(title);
                note.setContent(content);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public void deleteNoteByUsername(String username, String title) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("notes", "username = ? AND title = ?", new String[]{username, title});
        MyDB.close();
    }
}
