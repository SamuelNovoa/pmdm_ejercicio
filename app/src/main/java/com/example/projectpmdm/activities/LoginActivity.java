package com.example.projectpmdm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectpmdm.R;
import com.example.projectpmdm.services.UserMgr;

public class LoginActivity extends AppCompatActivity {
    TextView txtUsername;
    TextView txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtLoginUsername);
        txtPwd = findViewById(R.id.txtLoginPwd);
    }

    public void onLogin(View view) {
        String username = txtUsername.getText().toString();
        String pwd = txtPwd.getText().toString();

        if (!UserMgr.login(this, username, pwd)) {
            Toast.makeText(this, R.string.toast_wrong_pwd, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void onRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}