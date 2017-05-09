package com.tuannguyen.framework.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private AppConfig mAppConfig;

    public AppModule(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mAppConfig = new AppConfig(mSharedPreferences);
    }

    @Provides
    @Singleton
    Context getContext() {
        return mContext;
    }

    @Provides
    @Singleton
    SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Provides
    @Singleton
    AppConfig getAppConfig() {
        return mAppConfig;
    }

    @Provides
    @Singleton
    FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseDatabase getFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    LoginManager getFacebookLoginManager(){
        return LoginManager.getInstance();
    }

    @Provides
    @Singleton
    CallbackManager getCallbackMananger() { return CallbackManager.Factory.create(); }
}
