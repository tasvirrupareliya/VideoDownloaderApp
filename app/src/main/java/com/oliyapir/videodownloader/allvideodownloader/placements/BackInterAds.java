package com.oliyapir.videodownloader.allvideodownloader.placements;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

public class BackInterAds {

    public static Activity mActivity;

    public static InterstitialAd mInterstitialAd;
    public static OnAdClosedListener mOnAdClosedListener;

    public interface OnAdClosedListener {
        public void onAdClosed();
    }

    public void loadInterAds(Activity context) {
        final String interAd = PowerPreference.getDefaultFile().getString(Utils.INTERID, "123");
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, interAd, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {

                        loadInterAds(context);

                        if (mOnAdClosedListener != null) {
                            mOnAdClosedListener.onAdClosed();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        mInterstitialAd = null;

                        Utils.Log("The ad failed to show.");
                        loadInterAds(mActivity);

                        if (mOnAdClosedListener != null)
                            mOnAdClosedListener.onAdClosed();

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });
    }


    public void showInterAds(Activity context, OnAdClosedListener onAdClosedListener) {
        mActivity = context;
        mOnAdClosedListener = onAdClosedListener;

        if (PowerPreference.getDefaultFile().getBoolean(Utils.GoogleBackInterOnOff, true) && PowerPreference.getDefaultFile().getBoolean(Utils.AdsOnOff, true)) {
            int custGCount = PowerPreference.getDefaultFile().getInt(Utils.SERVER_BACK_COUNT);
            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_BACK_COUNT);

            if (custGCount != 0 && appGCount % custGCount == 0) {
                showInterAds1(context, onAdClosedListener);
            } else {
                appGCount++;
                PowerPreference.getDefaultFile().putInt(Utils.APP_BACK_COUNT, appGCount);
                if (mOnAdClosedListener != null)
                    mOnAdClosedListener.onAdClosed();
            }
        } else {
            if (mOnAdClosedListener != null)
                mOnAdClosedListener.onAdClosed();
        }
    }


    public void showInterAds1(Activity context, OnAdClosedListener onAdClosedListener) {
        mActivity = context;
        mOnAdClosedListener = onAdClosedListener;
        if (mInterstitialAd != null) {
            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_BACK_COUNT);

            appGCount++;
            PowerPreference.getDefaultFile().putInt(Utils.APP_BACK_COUNT, appGCount);
            mInterstitialAd.show(context);
        } else {
            loadInterAds(context);

            int appGCount = PowerPreference.getDefaultFile().getInt(Utils.APP_BACK_COUNT);

            appGCount++;
            PowerPreference.getDefaultFile().putInt(Utils.APP_BACK_COUNT, appGCount);

            if (mOnAdClosedListener != null)
                mOnAdClosedListener.onAdClosed();
        }
    }
}
