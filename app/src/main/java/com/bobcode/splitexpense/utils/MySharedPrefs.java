package com.bobcode.splitexpense.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vijayananjalees on 4/22/15.
 */
public class MySharedPrefs {
    public static final String SPLIT_EXPENSE_SHARED_PREFS = "com.bobcode.splitexpense.SPLITEXPENSEPREFS";
    public static final String PREFS_KEY_FOR_USERNAME = "USER_NAME";
    public static final String PREFS_KEY_FOR_REMEMBER_ME = "REMEMBER_ME";
    public static final String PREFS_KEY_FOR_AEE_CATEGORY = "AEE_CATEGORY";

    public static final String PREFS_KEY_FOR_ADD_ACCOUNT_DESCRIPTION = "ADD_ACCOUNT_DESCRIPTION";

    Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MySharedPrefs(Context context) {
        this.context = context;
    }

    public void storeDataToSharePrefs(String key, String value) {
        sharedPreferences = context.getSharedPreferences(SPLIT_EXPENSE_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }


    public String getDataFromSharePrefs(String key) {
        sharedPreferences = context.getSharedPreferences(SPLIT_EXPENSE_SHARED_PREFS, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, null);
        } else
            return "key not found";
    }
}
