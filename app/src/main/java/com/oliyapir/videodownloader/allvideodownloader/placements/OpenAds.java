package com.oliyapir.videodownloader.allvideodownloader.placements;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.oliyapir.videodownloader.allvideodownloader.MyApplication;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.oliyapir.videodownloader.allvideodownloader.view.activity.SplashScreen;
import com.preference.PowerPreference;

public class OpenAds implements LifecycleObserver, android.app.Application.ActivityLifecycleCallbacks {

    @SuppressLint("StaticFieldLeak")
    public static OpenAds mAppAds;
    private static final String LOG_TAG = "AppOpenManager";
    private AppOpenAd appOpenAd1 = null;

    Dialog mDialog = null;
    private final MyApplication Application = MyApplication.getInstance();
    private Activity currentActivity;

    private static boolean isShowingAd = false;

    public interface OnAdClosedListener {
        public void onAdClosed();
    }

    public OpenAds() {
        if (mAppAds == null) {
            mAppAds = this;
            this.Application.registerActivityLifecycleCallbacks(this);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        }
    }

    public void loadOpenAd() {
        AppOpenAd.AppOpenAdLoadCallback loadCallback1 = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(AppOpenAd ad) {
                OpenAds.this.appOpenAd1 = ad;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                OpenAds.this.appOpenAd1 = null;
            }
        };

        final String appOpenAd = PowerPreference.getDefaultFile().getString(Utils.OPENAD, "123");
        AdRequest request = getAdRequest();
        AppOpenAd.load(Application, appOpenAd, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback1);

    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public void showOpenAd(OnAdClosedListener onAdClosedListener) {

        if (appOpenAd1 != null && !isShowingAd) {

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd1 = null;
                            isShowingAd = false;

                            if (onAdClosedListener != null)
                                onAdClosedListener.onAdClosed();

                            loadOpenAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd1 = null;
                            isShowingAd = false;

                            loadOpenAd();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd1.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd1.show(currentActivity);


        } else if (!isShowingAd) {
            loadOpenAd();
        }
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {

        if (PowerPreference.getDefaultFile().getBoolean(Utils.AdsOnOff, true)) {
            if (!(currentActivity instanceof SplashScreen)) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                } else {
                    showOpenAd(null);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

}
