package com.oliyapir.videodownloader.allvideodownloader;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.oliyapir.videodownloader.allvideodownloader.util.AppLangSessionManager;

import java.util.Locale;

public class MyApplication extends Application {
    AppLangSessionManager appLangSessionManager;
    public static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

       // FirebaseMessaging.getInstance().subscribeToTopic("all");
        appLangSessionManager = new AppLangSessionManager(getApplicationContext());
        setLocale(appLangSessionManager.getLanguage());

        application = this;
        MobileAds.initialize(this, initializationStatus -> {

        });
    }

    public static MyApplication getInstance() {
        if (application == null)
            application = new MyApplication();
        return application;
    }

    public void setLocale(String lang) {
        if (lang.equals("")) {
            lang = "en";
        }
        Log.d("Support", lang + "");
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
