package com.movilix;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref Preference;
    private SharedPreferences sharedPreferences;

    public static SharedPref getInstance(Context context) {
        if (Preference == null) {
            Preference = new SharedPref(context);
        }
        return Preference;
    }

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("reward_points",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
