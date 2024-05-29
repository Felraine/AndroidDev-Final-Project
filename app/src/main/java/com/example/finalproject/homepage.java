package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalproject.databinding.ActivityHomepageBinding;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class homepage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomepageBinding binding;
    private CircleImageView profilePic;
    private TextView usernameNav, emailNav;
    private String currentUser; // Store current user globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        setSupportActionBar(binding.appBarHomepage.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_themes, R.id.nav_feedback, R.id.nav_budget, R.id.nav_schedule, R.id.nav_notes, R.id.nav_reminders,R.id.nav_todolist)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        profilePic = navigationView.getHeaderView(0).findViewById(R.id.profilePic);
        usernameNav = navigationView.getHeaderView(0).findViewById(R.id.usernameNav);
        emailNav = navigationView.getHeaderView(0).findViewById(R.id.emailNav);

        // Retrieve current user from SharedPreferences
        currentUser = getLoggedInUsername();
        getUserDataFromDatabase(currentUser);
    }

    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "default_username");
    }

    private void getUserDataFromDatabase(String username) {
        databaseFunctions dbHelper = new databaseFunctions(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            String imagePath = cursor.getString(cursor.getColumnIndex("profile_picture_path"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap != null) {
                    profilePic.setImageBitmap(bitmap);
                } else {
                    profilePic.setImageResource(R.drawable.photo3);
                }
            } else {
                profilePic.setImageResource(R.drawable.photo3);
            }

            usernameNav.setText(username);
            emailNav.setText(email);
        } else {
            profilePic.setImageResource(R.drawable.photo3);
            usernameNav.setText("Default Username");
            emailNav.setText("Default Email");
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
