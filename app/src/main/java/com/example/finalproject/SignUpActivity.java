package com.example.finalproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    EditText username, password, email, confirmPass;
    Button signUpButton;
    ImageView addProfile;
    String imagePath;
    databaseFunctions DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        confirmPass = findViewById(R.id.confirmPass);
        signUpButton = findViewById(R.id.signUpButton);
        addProfile = findViewById(R.id.addProfile);
        DB = new databaseFunctions(this);

        addProfile.setImageResource(R.drawable.addprofile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String pass2 = confirmPass.getText().toString().trim();
                String mail = email.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || pass2.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(pass2)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imagePath != null && !imagePath.isEmpty()) {
                    boolean insert = DB.insertData(user, pass, mail, imagePath);
                    if (insert) {
                        Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Profile picture not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imagePath = getImagePathFromUri(imageUri);
                    addProfile.setImageURI(imageUri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getImagePathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
