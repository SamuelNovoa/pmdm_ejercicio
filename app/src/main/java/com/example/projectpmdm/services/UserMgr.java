package com.example.projectpmdm.services;

import android.content.Context;

import com.example.projectpmdm.daos.UserDao;
import com.example.projectpmdm.models.User;

public class UserMgr {
    public static User activeUser = null;

    public static User getActiveUser() {
        return activeUser;
    }

    public static boolean isLogged() {
        return activeUser != null;
    }

    public static boolean login(Context context, String username, String pwd) {
        User user = UserDao.getUserByName(context, username);

        if (user == null || !user.getPwd().equals(pwd))
            return false;

        activeUser = user;
        return true;
    }

    public static void logout() {
        activeUser = null;
    }

    public static boolean register(Context context, String username, String pwd) {
        return UserDao.saveUser(context, new User(username, pwd));
    }
}
