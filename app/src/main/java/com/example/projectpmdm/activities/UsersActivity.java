package com.example.projectpmdm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projectpmdm.R;
import com.example.projectpmdm.adapters.UserAdapter;
import com.example.projectpmdm.daos.UserDao;
import com.example.projectpmdm.models.User;
import com.example.projectpmdm.services.UserMgr;

import java.util.List;

public class UsersActivity extends AppCompatActivity {
    ListView usersList;
    UserAdapter adapter;

    private static final String CHANNEL_ID = "ProjChan";
    private static final int NOTIFICATION_ID = 1;

    private static final int CALL_REQUEST_CODE = 1;

    private int lastRow = 0;
    private String lastTlf;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            return;

        switch (requestCode) {
            case CALL_REQUEST_CODE:
                doCall();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String name = usersList.getAdapter().getItem(info.position).toString();
        menu.setHeaderTitle(name);

        lastRow = info.position;
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contextMenuRemove:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.users_remove_dialog_title);
                builder.setMessage(R.string.users_remove_dialog_body);

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                builder.setView(input);

                builder.setPositiveButton(R.string.confirmation_yes, (dialog, which) -> {
                    if (!input.getText().toString().equals("123")) {
                        Toast.makeText(this, R.string.toast_admin_pwd_incorrect, Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        return;
                    }

                    User user = adapter.getItem(lastRow);
                    adapter.remove(user);

                    UserDao.remove(this, user);
                });
                
                builder.setNegativeButton(R.string.confirmation_no, (dialog, which) -> {
                    dialog.cancel();
                });
                
                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        if (!UserMgr.isLogged()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }

        usersList = findViewById(R.id.usersList);

        @SuppressLint("DefaultLocale") List<User> users = UserDao.getUsers(this, String.format("id != '%d'", UserMgr.getActiveUser().getId()));
        adapter = new UserAdapter(this, R.layout.user_row, users);

        usersList.setAdapter(adapter);
        usersList.setOnItemClickListener((adapterView, view, i, l) -> {
            User user = (User) adapterView.getItemAtPosition(i);

            lastTlf = user.getTlf();
            call();
        });

        registerForContextMenu(usersList);
    }

    public void call() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
        else
            doCall();
    }

    private void doCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel: %s", lastTlf)));
        if (intent.resolveActivity(getPackageManager()) == null)
            return;

        startActivity(intent);
    }
}