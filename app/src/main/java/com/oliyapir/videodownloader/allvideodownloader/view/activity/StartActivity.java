package com.oliyapir.videodownloader.allvideodownloader.view.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.placements.InterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.LargeNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.MiniNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

public class StartActivity extends AppCompatActivity {

    TextView btn_start;
    LinearLayout llRate, llShare, llpolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn_start = (TextView) findViewById(R.id.btn_start);
        llRate = (LinearLayout) findViewById(R.id.llRate);
        llShare = (LinearLayout) findViewById(R.id.llShare);
        llpolicy = (LinearLayout) findViewById(R.id.llpolicy);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InterAds().showInterAds(StartActivity.this, new InterAds.OnAdClosedListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(StartActivity.this, GabMainActivity.class));
                    }
                });
            }
        });

        llpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String policylink = PowerPreference.getDefaultFile().getString(Utils.PolicyLink);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(policylink)));
            }
        });

        llRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sharemsg = "Download " + getString(R.string.app_name) + "\n" + Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, sharemsg);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitdialog();
    }

    private void exitdialog() {
        try {

            Dialog dialog = new Dialog(StartActivity.this);
            dialog.setContentView(R.layout.dialog_exit);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
            final AppCompatButton btnRate = dialog.findViewById(R.id.btnRate);
            final AppCompatButton btnExit = dialog.findViewById(R.id.btnExit);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    new LargeNativeAds().showNativeAds(StartActivity.this, dialog);
                }
            });

            dialog.show();

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            });

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PowerPreference.getDefaultFile().putInt(Utils.APP_INTERVAL_COUNT, 0);

                    if (PowerPreference.getDefaultFile().getBoolean(Utils.GoogleExitSplashInterOnOff, true)) {
                        new InterAds().showInterAds(StartActivity.this, new InterAds.OnAdClosedListener() {
                            @Override
                            public void onAdClosed() {
                                Intent intent = new Intent(StartActivity.this, ExitActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Intent intent = new Intent(StartActivity.this, ExitActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.cancel();
                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(viewIntent);
                }
            });
        } catch (Exception e) {
            Log.e("Catch", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PowerPreference.getDefaultFile().getBoolean(Utils.FullScreenOnOff, true)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        new MiniNativeAds().showNativeAds(this, null);
        new LargeNativeAds().showNativeAds(this, null);
    }
}