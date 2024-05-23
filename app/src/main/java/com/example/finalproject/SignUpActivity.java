package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText username, password, email, confirmPass;
    Button signUpButton;
    databaseFunctions DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        confirmPass = findViewById(R.id.confirmPass);
        signUpButton = findViewById(R.id.signUpButton);
        DB = new databaseFunctions(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String pass2 = confirmPass.getText().toString();
                String mail = email.getText().toString();

                if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || pass2.isEmpty() && getText(Integer.parseInt(pass)) == getText(Integer.parseInt(pass2))) {
                    Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkUsername(user);
                    if (!checkUser) {
                        Boolean insert = DB.insertData(user, pass, mail);
                        if (insert) {
                            Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "User already exists! Please sign in", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
