package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView signUpButton;
    databaseFunctions DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        emailEditText = findViewById(R.id.EduEmail);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.createAccount);
        DB = new databaseFunctions(this);

        loginButton.setOnClickListener(this);
        clearSharedPreferences();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                String userEmail = emailEditText.getText().toString().trim();
                String userPassword = passwordEditText.getText().toString().trim();
                if (DB.checkUserCredentials(userEmail, userPassword)) {
                    String username = getUsernameByEmail(userEmail);
                    if (username != null) {
                        storeUsernameInSharedPreferences(username); // Store username in SharedPreferences
                        Intent intent = new Intent(MainActivity.this, homepage.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error retrieving username", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getUsernameByEmail(String email) {
        try {
            SQLiteDatabase MyDB = DB.getReadableDatabase();
            Cursor cursor = MyDB.rawQuery("SELECT username FROM users WHERE email = ?", new String[]{email});
            if (cursor.moveToFirst()) {
                String username = cursor.getString(0);
                cursor.close();
                return username;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void storeUsernameInSharedPreferences(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void onCreateAccountClicked(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
