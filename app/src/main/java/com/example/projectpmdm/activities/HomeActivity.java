package com.example.projectpmdm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.projectpmdm.R;
import com.example.projectpmdm.services.UserMgr;

public class HomeActivity extends AppCompatActivity {
    TextView txtTitle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.homeMenuMyData:
                i = new Intent(this, MyDataActivity.class);
                startActivity(i);
                return true;
            case R.id.homeMenuPrefs:
                i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                return true;
            case R.id.homeMenuLogout:
                UserMgr.logout();

                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!UserMgr.isLogged()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }

        txtTitle = findViewById(R.id.txtHomeTitle);
        txtTitle.setText(getString(R.string.home_title, UserMgr.getActiveUser().getName()));
    }

    public void onSeeUsers(View view) {
        Intent i = new Intent(this, UsersActivity.class);
        startActivity(i);
    }

    public void onSeeProducts(View view) {
        Intent i = new Intent(this, ProductsActivity.class);
        startActivity(i);
    }
}