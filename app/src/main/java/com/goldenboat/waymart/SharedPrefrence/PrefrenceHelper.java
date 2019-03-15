package com.goldenboat.waymart.SharedPrefrence;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceHelper {
    SharedPreferences preferences;


    public PrefrenceHelper(Context context) {
        this.preferences = context.getSharedPreferences("APP_DATA",Context.MODE_PRIVATE);
    }

    public boolean isLogin() {
        return preferences.getBoolean("isLogin", false);
    }

    public String getUID() {
        return preferences.getString("UID", "null");
    }

    public void setUID(String s) {
        preferences.edit().putString("UID", s).apply();
    }

    public String getEmail() {
        return preferences.getString("email", "null");
    }

    public void setEmail(String s) {
        preferences.edit().putString("email", s).apply();
    }

    public void setIsLogin(boolean b) {
        preferences.edit().putBoolean("isLogin", b).apply();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
