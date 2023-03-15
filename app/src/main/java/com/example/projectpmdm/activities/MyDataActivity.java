package com.example.projectpmdm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectpmdm.R;
import com.example.projectpmdm.daos.UserDao;
import com.example.projectpmdm.models.User;
import com.example.projectpmdm.services.UserMgr;

public class MyDataActivity extends AppCompatActivity {
    TextView dataTitle;

    TextView address;
    TextView website;
    TextView tlf;

    Button editBtn;

    User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.listMenuEdit:
                address.setEnabled(true);
                website.setEnabled(true);
                tlf.setEnabled(true);

                editBtn.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);

        if (!UserMgr.isLogged()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }

        dataTitle = findViewById(R.id.txtMyDataTitle);

        address = findViewById(R.id.txtDataAddress);
        website = findViewById(R.id.txtDataWebsite);
        tlf = findViewById(R.id.txtDataTlf);

        editBtn = findViewById(R.id.btnMyDataEdit);

        user = UserMgr.getActiveUser();

        dataTitle.setText(getString(R.string.data_title, user.getName()));

        address.setText(user.getAddress());
        website.setText(user.getWebsite());
        tlf.setText(user.getTlf());
    }

    public void onEdit(View view) {
        user.setAddress(address.getText().toString());
        user.setWebsite(website.getText().toString());
        user.setTlf(tlf.getText().toString());

        UserDao.saveUser(this, user);

        address.setEnabled(false);
        website.setEnabled(false);
        tlf.setEnabled(false);

        editBtn.setVisibility(View.GONE);
    }
}