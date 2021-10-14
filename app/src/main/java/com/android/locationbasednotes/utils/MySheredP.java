package com.android.locationbasednotes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MySheredP {
    private SharedPreferences prefs;

    public MySheredP(Context context) {
         prefs = context.getSharedPreferences("MyPref1", MODE_PRIVATE);
    }


    public String getString(String key, String defValue)
    {
       return prefs.getString(key  , defValue);
    }

    public void putString(String key, String value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
