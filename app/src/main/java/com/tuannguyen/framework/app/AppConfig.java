package com.tuannguyen.framework.app;


import android.content.SharedPreferences;

public class AppConfig {
    private SharedPreferences mSharedPreferences;
    public AppConfig(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }
}
