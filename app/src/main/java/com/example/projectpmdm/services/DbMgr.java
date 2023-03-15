package com.example.projectpmdm.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbMgr extends SQLiteOpenHelper {
    public DbMgr(Context context, String name,
                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        String[][] users = {
                { "Yo", "123", "Estrada Valadares, 13", "https://www.edu.xunta.gal/centros/iesteis", "(+34) 622 60 94 62" },
                { "Perico", "321", "Calle falsa, 123", "https://google.es", "(+34) 622 60 94 62" },
                { "Josefina", "asd", "Gran vía, 1", "https://youtube.com", "(+34) 622 60 94 62" },
        };

        Object[][] products = {
                { "Producto A", "Un buen producto", 13.75f, 4 },
                { "Producto B", "Un mal producto", 73.75f, 21 },
        };

        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, pwd TEXT NOT NULL, address TEXT, website TEXT, tlf TEXT)");
        db.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, description TEXT NOT NULL, price FLOAT NOT NULL, qty INTEGER NOT NULL DEFAULT 0)");

        for (String[] user : users) {
            ContentValues values = new ContentValues();

            values.put("name", user[0]);
            values.put("pwd", user[1]);
            values.put("address", user[2]);
            values.put("website", user[3]);
            values.put("tlf", user[4]);

            db.insert("users", null, values);
        }

        for (Object[] product : products) {
            ContentValues values = new ContentValues();

            values.put("name", (String) product[0]);
            values.put("description", (String) product[1]);
            values.put("price", (Float) product[2]);
            values.put("qty", (Integer) product[3]);

            db.insert("products", null, values);
        }
    }
}
