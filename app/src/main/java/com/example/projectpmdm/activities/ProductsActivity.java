package com.example.projectpmdm.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.projectpmdm.R;
import com.example.projectpmdm.adapters.ProductAdapter;
import com.example.projectpmdm.daos.ProductDao;
import com.example.projectpmdm.models.Product;
import com.example.projectpmdm.services.UserMgr;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    ListView productsList;
    ProductAdapter adapter;

    private static final int REQUEST_INSERT_PRODUCT = 1;
    private static final int NOTIFICATION_REQUEST_CODE = 2;

    private static final String CHANNEL_ID = "ProjChan";
    private static final int NOTIFICATION_ID = 1;

    private int lastRow = 0;
    private String lastTlf;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            return;

        switch (requestCode) {
            case NOTIFICATION_REQUEST_CODE:
                initNotificationChannel();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.productsMenuInsert:
                i = new Intent(this, InsertProductActivity.class);
                startActivityForResult(i, REQUEST_INSERT_PRODUCT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        switch (requestCode) {
            case REQUEST_INSERT_PRODUCT:
                long id = data.getLongExtra("newProductId", -1);
                if (id == -1)
                    return;

                Product product = ProductDao.getProductById(this, id);
                adapter.add(product);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String name = productsList.getAdapter().getItem(info.position).toString();
        menu.setHeaderTitle(name);

        lastRow = info.position;
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contextMenuRemove:
                Product product = adapter.getItem(lastRow);
                adapter.remove(product);

                ProductDao.remove(this, product);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initNotificationChannel();

        if (!UserMgr.isLogged()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        }

        productsList = findViewById(R.id.usersList);

        @SuppressLint("DefaultLocale") List<Product> products = ProductDao.getProducts(this, null);
        adapter = new ProductAdapter(this, R.layout.product_row, products);

        productsList.setAdapter(adapter);
        productsList.setOnItemClickListener((adapterView, view, i, l) -> {
            notificate();
        });

        registerForContextMenu(productsList);
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void notificate() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sp.getBoolean("notifications", false))
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificateModern();
        else
            notificateLegacy();
    }

    private void notificateModern() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.alert_light_frame)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user));

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(NOTIFICATION_ID, builder.build());
    }

    private void notificateLegacy() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.alert_light_frame)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.user));

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, builder.build());
    }
}