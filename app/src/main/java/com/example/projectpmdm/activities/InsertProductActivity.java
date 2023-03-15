package com.example.projectpmdm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectpmdm.R;
import com.example.projectpmdm.daos.ProductDao;
import com.example.projectpmdm.models.Product;

public class InsertProductActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtDescription;
    TextView txtPrice;
    TextView txtQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        txtName = findViewById(R.id.txtInsProductName);
        txtDescription = findViewById(R.id.txtInsProductDesc);
        txtPrice = findViewById(R.id.txtInsProductPrice);
        txtQty = findViewById(R.id.txtInsProductQty);
    }

    public void onAdd(View view) {
        String name = txtName.getText().toString();
        String description = txtName.getText().toString();
        String price = txtPrice.getText().toString();
        String qty = txtQty.getText().toString();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || qty.isEmpty()) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(name, description, Float.parseFloat(price), Integer.parseInt(qty));
        if (!ProductDao.saveProduct(this, product))
            return;

        Intent i = new Intent();
        i.putExtra("newProductId", product.getId());
        setResult(RESULT_OK, i);

        finish();
    }
}