package com.oliyapir.videodownloader.allvideodownloader.placements;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.Objects;

public class LargeNativeAds {

    private static ArrayList<NativeAd> gNativeAd = new ArrayList<>();
    private static String ad_type;

    public void loadNativeAds(Activity activity, Dialog dialog) {
        final String nativeAdstr = PowerPreference.getDefaultFile().getString(Utils.NATIVEID, "123");

        AdLoader.Builder builder = new AdLoader.Builder(activity, nativeAdstr);
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd natives) {

                gNativeAd.clear();
                gNativeAd.add(natives);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError errorCode) {
                Log.e(Utils.adsLog, "loadNativeAds failed" + errorCode.toString());
                gNativeAd.clear();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    public FrameLayout getFrameLayout(Activity activity, Dialog dialog) {
        if (dialog != null) {
            return dialog.findViewById(R.id.nativeAd);
        } else {
            return activity.findViewById(R.id.nativeAd);
        }
    }


    public TextView getTextLayout(Activity activity, Dialog dialog) {
        if (dialog != null) {
            return dialog.findViewById(R.id.ad_space);
        } else {
            return activity.findViewById(R.id.ad_space);
        }
    }


    public void showNativeAds(Activity activity, Dialog dialog) {

        FrameLayout nativeAdLayout = getFrameLayout(activity, dialog);
        TextView adSpace = getTextLayout(activity, dialog);

        LinearLayout adView = null;

        if (PowerPreference.getDefaultFile().getBoolean(Utils.AdsOnOff, true) && PowerPreference.getDefaultFile().getBoolean(Utils.GoogleLargeNativeOnOff, true)) {

            if (gNativeAd.size() > 0) {

                if (dialog != null) {
                    adView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.ads_native_dialog, null);
                } else {
                    adView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.ads_native_normal, null);
                }

                NativeAd lovalNative = gNativeAd.get(0);

                populateUnifiedNativeAdView(lovalNative, adView.findViewById(R.id.uadview));

                nativeAdLayout.removeAllViews();
                nativeAdLayout.addView(adView);

                adSpace.setVisibility(View.GONE);
                nativeAdLayout.setVisibility(View.VISIBLE);

                loadNativeAds(activity, dialog);

            } else {
                loadNativeAds(activity, dialog);
            }
        } else {
            nativeAdLayout.setVisibility(View.GONE);
            adSpace.setVisibility(View.GONE);
        }
    }


    public void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        if (adView.findViewById(R.id.ad_media) != null) {
            MediaView mediaView = adView.findViewById(R.id.ad_media);
            mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            adView.setMediaView(mediaView);

        }
        if (adView.findViewById(R.id.ad_headline) != null)
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));

        if (adView.findViewById(R.id.ad_body) != null)
            adView.setBodyView(adView.findViewById(R.id.ad_body));

        if (adView.findViewById(R.id.ad_call_to_action) != null)
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

        if (adView.findViewById(R.id.ad_app_icon) != null)
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        if (adView.findViewById(R.id.ad_stars) != null)
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));


        if (nativeAd.getStarRating() == null) {
            if (adView.getStarRatingView() != null)
                Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.GONE);
        } else {
            if (adView.getStarRatingView() != null) {
                Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.VISIBLE);
                ((RatingBar) adView.getStarRatingView()).setRating(Float.parseFloat(String.valueOf(nativeAd.getStarRating())));
            }
        }

        if (nativeAd.getHeadline() == null) {
            if (adView.getHeadlineView() != null)
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.GONE);
        } else {
            if (adView.getHeadlineView() != null) {
                Objects.requireNonNull(adView.getHeadlineView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            }
        }

        if (nativeAd.getBody() == null) {
            if (adView.getBodyView() != null)
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.GONE);
        } else {
            if (adView.getBodyView() != null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (nativeAd.getCallToAction() == null) {
            if (adView.getCallToActionView() != null)
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            if (adView.getCallToActionView() != null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                if (PowerPreference.getDefaultFile().getBoolean(Utils.GoogleNativeTextOnOff, true)) {
                    ((TextView) adView.getCallToActionView()).setText(PowerPreference.getDefaultFile().getString(Utils.GoogleNativeText, "OPEN"));
                } else {
                    ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
                }
            }
        }

        if (nativeAd.getIcon() == null) {
            if (adView.getIconView() != null)
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            if (adView.getIconView() != null) {
                ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);
    }

    public void showListNativeAds(Activity activity, FrameLayout nativeAd, TextView adSpace) {

        LinearLayout adView = null;

        if (PowerPreference.getDefaultFile().getBoolean(Utils.AdsOnOff, true) && PowerPreference.getDefaultFile().getBoolean(Utils.LargeNativeOnOff, true)) {

            if (gNativeAd.size() > 0) {

                adView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.ads_native_normal, null);
                CardView cardView = adView.findViewById(R.id.cvMain);

                NativeAd lovalNative = gNativeAd.get(0);
                populateUnifiedNativeAdView(lovalNative, adView.findViewById(R.id.uadview));

                nativeAd.removeAllViews();
                nativeAd.addView(adView);

                adSpace.setVisibility(View.GONE);
                nativeAd.setVisibility(View.VISIBLE);

                loadNativeAds(activity, null);

            } else {
                loadNativeAds(activity, null);
            }
        } else {
            nativeAd.setVisibility(View.GONE);
            adSpace.setVisibility(View.GONE);
        }
    }
}
