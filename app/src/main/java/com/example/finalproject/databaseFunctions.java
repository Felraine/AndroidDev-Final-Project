package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class databaseFunctions extends SQLiteOpenHelper {
    public static final String DBNAME = "studyLife.db";
    private static final int DATABASE_VERSION = 14;

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

    public void checkTable(String date) {
        String create = "CREATE TABLE IF NOT EXISTS `"+date+
                "` (`ID` integer, `Schedule` text, `From` text, `To` text, `Color` text );";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(create);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSched(Sched t, String date) {
        checkTable(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO `"+date+"` (`ID`, `Schedule`, `From`, `To`, `Color`) VALUES " +
                "( '"+t.getID()+"', '"+t.getSched()+"', '"+t.getFromToString()+"', '"+t.getToToString()+
                "', '"+t.getColor()+"' );";
        db.execSQL(insert);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Sched> getAllTasks(String date) {
        checkTable(date);
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Sched> tasks = new ArrayList<>();
        String select = "SELECT * FROM `"+date+"`;";
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Sched s = new Sched();
                s.setID(cursor.getInt(0));
                s.setSched(cursor.getString(1));
                s.setFrom(cursor.getString(2));
                s.setTo(cursor.getString(3));
                s.setColor(cursor.getString(4));
                tasks.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getNextID(String date) {
        ArrayList<Sched> sched = getAllTasks(date);
        int id = 0;
        int size = sched.size();
        if (size != 0) {
            int lastIndex = sched.size()-1;
            id = sched.get(lastIndex).getID()+1;
        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateSched(Sched t, String date) {
        String update = "UPDATE `"+date+"` SET `Schedule` = '"+t.getSched()+"', `From` = '"+
                t.getFromToString()+"', `To` = '"+t.getToToString()+"', `Color` = '"+t.getColor()
                +"' WHERE `ID` = "+t.getID()+";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }

    public void deleteSched(int id, String date) {
        String delete = "DELETE FROM `"+date+"` WHERE `ID` = '"+id+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delete);
    }
}
