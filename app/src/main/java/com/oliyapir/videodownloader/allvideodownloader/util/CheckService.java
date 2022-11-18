package com.oliyapir.videodownloader.allvideodownloader.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.preference.PowerPreference;

public class CheckService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        PowerPreference.getDefaultFile().putBoolean("running", false);
        Utils.stopVpn();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        PowerPreference.getDefaultFile().putBoolean("running", false);
        Utils.stopVpn();
        super.onDestroy();
    }
}
