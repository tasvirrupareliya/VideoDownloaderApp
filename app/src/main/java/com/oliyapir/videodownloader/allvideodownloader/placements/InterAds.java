package com.oliyapir.videodownloader.allvideodownloader.placements;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

public class InterAds {

    @SuppressLint("StaticFieldLeak")
    public static Activity mActivity;

    public static InterstitialAd mInterstitialAd;
    public static OnAdClosedListener mOnAdClosedListener;

    public interface OnAdClosedListener {
        public void onAdClosed();
    }

    public void loadInterAds(Activity activity) {
        final String interAd = PowerPreference.getDefaultFile().getString(Utils.INTERID, "123");
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, interAd, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                Utils.Log("onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Utils.Log("onAdClosed");
                        loadInterAds(activity);

                        if (mOnAdClosedListener != null) {
                            mOnAdClosedListener.onAdClosed();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        Utils.Log("The ad failed to show.");
                        mInterstitialAd = null;
                        loadInterAds(activity);

                        if (mOnAdClosedListener != null)
                            mOnAdClosedListener.onAdClosed();

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                        Utils.Log("The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
                Utils.Log("onAdFailedToLoad");
            }
        });

    }

    public void showInterAds(Activity context, OnAdClosedListener onAdClosedListener) {
        Utils.Log("showInterAds");
        mActivity = context;
        mOnAdClosedListener = onAdClosedListener;

        if (PowerPreference.getDefaultFile().getBoolean(Utils.GoogleInterOnOff, true) && PowerPreference.getDefaultFile().getBoolean(Utils.AdsOnOff, true)) {

            int custGCount = PowerPreference.getDefaultFile().getInt(Utils.SERVER_INTERVAL_COUNT);
            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_INTERVAL_COUNT);

            if (custGCount != 0 && appGCount % custGCount == 0) {
                watchAds(context, onAdClosedListener);
            } else {
                appGCount++;
                PowerPreference.getDefaultFile().putInt(Utils.APP_INTERVAL_COUNT, appGCount);
                if (mOnAdClosedListener != null)
                    mOnAdClosedListener.onAdClosed();
            }
        } else {
            if (mOnAdClosedListener != null)
                mOnAdClosedListener.onAdClosed();
        }
    }

    public void watchAds(Activity context, OnAdClosedListener onAdClosedListener) {
        mActivity = context;
        mOnAdClosedListener = onAdClosedListener;
        if (mInterstitialAd != null) {

            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_INTERVAL_COUNT);

            appGCount++;
            PowerPreference.getDefaultFile().putInt(Utils.APP_INTERVAL_COUNT, appGCount);
            mInterstitialAd.show(context);


        } else {
            loadInterAds(context);

            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_INTERVAL_COUNT);

            appGCount++;
            PowerPreference.getDefaultFile().putInt(Utils.APP_INTERVAL_COUNT, appGCount);

            if (mOnAdClosedListener != null)
                mOnAdClosedListener.onAdClosed();
        }
    }
}
