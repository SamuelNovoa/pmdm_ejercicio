package com.example.projectpmdm.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectpmdm.models.User;
import com.example.projectpmdm.services.DbMgr;
import com.example.projectpmdm.services.UserMgr;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static boolean exists(Context context, String name) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getReadableDatabase();

        Cursor cursor = db.query("users", new String[] {"*"}, String.format("name = '%s'", name), null, null, null, null, "1");

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    public static boolean saveUser(Context context, User user) {
        if (user.getId() == -1)
            return createUser(context, user);
        else
            return updateUser(context, user);
    }

    private static boolean createUser(Context context, User user) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();

        ContentValues userValues = new ContentValues();

        userValues.put("name", user.getName());
        userValues.put("pwd", user.getPwd());
        userValues.put("address", user.getAddress());
        userValues.put("website", user.getWebsite());
        userValues.put("tlf", user.getTlf());

        return db.insert("users", null, userValues) != -1;
    }

    private static boolean updateUser(Context context, User user) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();

        ContentValues userValues = new ContentValues();

        userValues.put("name", user.getName());
        userValues.put("pwd", user.getPwd());
        userValues.put("address", user.getAddress());
        userValues.put("website", user.getWebsite());
        userValues.put("tlf", user.getTlf());

        return db.update("users", userValues, "id = ?", new String[] {Long.toString(user.getId())}) != -1;
    }

    public static User getUserByName(Context context, String name) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getReadableDatabase();

        Cursor cursor = db.query("users", new String[] {"*"}, String.format("name = '%s'", name), null, null, null, null, "1");

        User user = new User();
        if (cursor.moveToFirst()) {
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setPwd(cursor.getString(2));
            user.setAddress(cursor.getString(3));
            user.setWebsite(cursor.getString(4));
            user.setTlf(cursor.getString(5));
        } else
            user = null;

        cursor.close();
        db.close();

        return user;
    }

    public static List<User> getUsers(Context context, String selection) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getReadableDatabase();

        Cursor cursor = db.query("users", new String[] {"*"}, selection, null, null, null, null, null);

        List<User> users = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setPwd(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setWebsite(cursor.getString(4));
                user.setTlf(cursor.getString(5));

                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return users;
    }

    public static void remove(Context context, User user) {
        DbMgr dbMgr = new DbMgr(context, "DBProject", null, 1);
        SQLiteDatabase db = dbMgr.getWritableDatabase();
        db.delete("users", String.format("id = '%d'", user.getId()), null);
        db.close();
    }
}
