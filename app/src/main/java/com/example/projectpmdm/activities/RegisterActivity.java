package com.example.projectpmdm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectpmdm.R;
import com.example.projectpmdm.daos.UserDao;
import com.example.projectpmdm.models.User;
import com.example.projectpmdm.services.UserMgr;

public class RegisterActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = findViewById(R.id.txtRegisterUsername);
        txtPwd = findViewById(R.id.txtRegisterPwd);
    }

    public void onRegister(View view) {
        String username = txtUsername.getText().toString();
        String pwd = txtPwd.getText().toString();

        if (username.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, R.string.toast_login_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (UserDao.exists(this, username)) {
            Toast.makeText(this, R.string.toast_username_used, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!UserMgr.register(this, username, pwd)) {
            Toast.makeText(this, R.string.toast_register_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}