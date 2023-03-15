package com.example.projectpmdm.daos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectpmdm.models.Product;
import com.example.projectpmdm.models.User;
import com.example.projectpmdm.services.DbMgr;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    public static Product getProductById(Context context, long id) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();

        @SuppressLint("DefaultLocale") Cursor cursor = db.query("products", new String[] {"*"}, String.format("id = '%d'", id), null, null, null, null, "1");

        Product product = new Product();
        if (cursor.moveToFirst()) {
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setDescription(cursor.getString(2));
            product.setPrice(cursor.getFloat(3));
            product.setQty(cursor.getInt(4));
        }

        cursor.close();
        db.close();

        return product;
    }

    public static boolean saveProduct(Context context, Product product) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();

        ContentValues productValues = new ContentValues();

        productValues.put("name", product.getName());
        productValues.put("description", product.getDescription());
        productValues.put("price", product.getPrice());
        productValues.put("qty", product.getQty());

        long newId = db.insert("products", null, productValues);
        if (newId != -1)
            product.setId(newId);

        return newId != -1;
    }

    public static List<Product> getProducts(Context context, String selection) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();

        Cursor cursor = db.query("products", new String[] {"*"}, selection, null, null, null, null, null);

        List<Product> products = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();

                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setDescription(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setQty(cursor.getInt(4));

                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return products;
    }

    public static void remove(Context context, Product product) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();
        db.delete("products", String.format("id = '%d'", product.getId()), null);
        db.close();
    }
}
