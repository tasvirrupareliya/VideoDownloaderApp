package com.oliyapir.videodownloader.allvideodownloader.view.activity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oliyapir.videodownloader.allvideodownloader.BuildConfig;
import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.placements.BackInterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.InterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.LargeNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.MiniNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.OpenAds;
import com.oliyapir.videodownloader.allvideodownloader.util.AppLangSessionManager;
import com.oliyapir.videodownloader.allvideodownloader.util.CheckService;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import unified.vpn.sdk.AuthMethod;
import unified.vpn.sdk.ClientInfo;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.HydraTransportConfig;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.SdkNotificationConfig;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.TransportConfig;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.UnifiedSdkConfig;
import unified.vpn.sdk.User;
import unified.vpn.sdk.VpnException;

public class SplashScreen extends AppCompatActivity {
    SplashScreen activity;
    Context context;
    AppLangSessionManager appLangSessionManager;
    LottieAnimationView lottieAnimationView;
    private static final String CHANNEL_ID = "vpn";
    public static int versioncode = BuildConfig.VERSION_CODE;
    String flag, policylink = "https://oliyapirinfotech.blogspot.com/2022/04/all-video-downloader.html";
    RelativeLayout privcypolicy, splashview;
    AppCompatButton continueapp;
    TextView link, link2;

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                Log.e("ServiceStatus", "Running");
                return true;
            }
        }
        Log.e("ServiceStatus", "Not running");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animationView);
        privcypolicy = (RelativeLayout) findViewById(R.id.privcypolicy);
        continueapp = (AppCompatButton) findViewById(R.id.continueapp);
        splashview = (RelativeLayout) findViewById(R.id.splashview);
        link = (TextView) findViewById(R.id.link);
        link2 = (TextView) findViewById(R.id.link2);

        PowerPreference.getDefaultFile().putBoolean(Utils.isVpnAuto, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isMyServiceRunning(CheckService.class)) {
                    startService(new Intent(SplashScreen.this, CheckService.class));
                }
                PowerPreference.getDefaultFile().putBoolean("running", true);
            }
        }, 1000);


        if (!PowerPreference.getDefaultFile().getBoolean(Utils.POLICY_ACCEPT)) {

            privcypolicy.setVisibility(View.VISIBLE);
            splashview.setVisibility(View.GONE);

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String packageName = "com.android.chrome";
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimaryDark));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.intent.setPackage(packageName);
                        customTabsIntent.launchUrl(SplashScreen.this, Uri.parse(policylink));
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            });

            link2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String packageName = "com.android.chrome";
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimaryDark));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.intent.setPackage(packageName);
                        customTabsIntent.launchUrl(SplashScreen.this, Uri.parse(policylink));
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            });


            continueapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PowerPreference.getDefaultFile().putBoolean(Utils.POLICY_ACCEPT, true);
                    privcypolicy.setVisibility(View.GONE);
                    splashview.setVisibility(View.VISIBLE);

                    if (Utils.checkInternet(SplashScreen.this)) {
                        Updateapi();
                    } else {
                        splashview.setVisibility(View.VISIBLE);
                        Dialog dialog = new Dialog(SplashScreen.this);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        TextView retry = (TextView) dialog.findViewById(R.id.retry);
                        dialog.setCancelable(false);
                        dialog.show();

                        retry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Utils.checkInternet(SplashScreen.this)) {
                                    Updateapi();
                                    dialog.cancel();
                                } else {
                                    dialog.show();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            splashview.setVisibility(View.VISIBLE);
            if (Utils.checkInternet(this)) {
                Updateapi();
            } else {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_no_internet);
                TextView retry = (TextView) dialog.findViewById(R.id.retry);
                dialog.setCancelable(false);
                dialog.show();

                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.checkInternet(SplashScreen.this)) {
                            Updateapi();
                            dialog.cancel();
                        } else {
                            dialog.show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        if (PowerPreference.getDefaultFile().getBoolean(Utils.FullScreenOnOff, true)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void HomeScreen() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PowerPreference.getDefaultFile().getBoolean(Utils.StartActivityOnOff)) {
                    startActivity(new Intent(SplashScreen.this, StartActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, GabMainActivity.class));
                    finish();
                }
            }
        }, 4000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                HomeScreen();
            } else {
                HomeScreen();
            }
        }
    }

    public void setLocale(String lang) {
        if (lang.equals("")) {
            lang = "en";
        }
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void Updateapi() {

        RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);

        StringRequest request = new StringRequest(Request.Method.GET, BuildConfig.BaseURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    PowerPreference.getDefaultFile().putInt(Utils.SERVER_INTERVAL_COUNT, Integer.parseInt(object.getString("GoogleIntervalCount")));
                    PowerPreference.getDefaultFile().putInt(Utils.APP_INTERVAL_COUNT, 0);

                    PowerPreference.getDefaultFile().putInt(Utils.SERVER_BACK_COUNT, Integer.parseInt(object.getString("GoogleBackInterIntervalCount")));
                    PowerPreference.getDefaultFile().putInt(Utils.APP_BACK_COUNT, 0);
                    PowerPreference.getDefaultFile().putBoolean(Utils.GoogleInterOnOff, Boolean.parseBoolean(object.getString("GoogleInterOnOff")));
                    PowerPreference.getDefaultFile().putBoolean(Utils.GoogleBackInterOnOff, Boolean.parseBoolean(object.getString("GoogleBackInterOnOff")));

                    PowerPreference.getDefaultFile().putBoolean(Utils.GoogleExitSplashInterOnOff, Boolean.parseBoolean(object.getString("GoogleExitSplashInterOnOff")));
                    PowerPreference.getDefaultFile().putString(Utils.PolicyLink, object.getString("PolicyLink"));

                    PowerPreference.getDefaultFile().putBoolean(Utils.MiniNativeOnOff, Boolean.parseBoolean(object.getString("GoogleMiniNativeOnOff")));
                    PowerPreference.getDefaultFile().putBoolean(Utils.LargeNativeOnOff, Boolean.parseBoolean(object.getString("GoogleLargeNativeOnOff")));

                    PowerPreference.getDefaultFile().putBoolean(Utils.AdsOnOff, Boolean.parseBoolean(object.getString("AdsOnOff")));
                    PowerPreference.getDefaultFile().putBoolean(Utils.FullScreenOnOff, Boolean.parseBoolean(object.getString("FullScreenOnOff")));
                    PowerPreference.getDefaultFile().putBoolean(Utils.StartActivityOnOff, Boolean.parseBoolean(object.getString("StartActivityOnOff")));

                    PowerPreference.getDefaultFile().putBoolean(Utils.GoogleNativeTextOnOff, Boolean.parseBoolean(object.getString("GoogleNativeTextOnOff")));
                    PowerPreference.getDefaultFile().putString(Utils.GoogleNativeText, object.getString("GoogleNativeText"));

                    PowerPreference.getDefaultFile().putString(Utils.INTERID, object.getString("GoogleInterAds"));
                    PowerPreference.getDefaultFile().putString(Utils.NATIVEID, object.getString("GoogleNativeAds"));
                    PowerPreference.getDefaultFile().putString(Utils.OPENAD, object.getString("GoogleAppOpenAds"));

                    /*PowerPreference.getDefaultFile().putString(Utils.INTERID, "ca-app-pub-3940256099942544/1033173712");
                    PowerPreference.getDefaultFile().putString(Utils.NATIVEID, "ca-app-pub-3940256099942544/2247696110");
                    PowerPreference.getDefaultFile().putString(Utils.OPENAD, "ca-app-pub-3940256099942544/3419835294");*/

                    PowerPreference.getDefaultFile().putBoolean(Utils.VpnOnOff, Boolean.parseBoolean(object.getString("VpnOnOff")));
                    PowerPreference.getDefaultFile().putString(Utils.Default_Vpn_Country, "United States");
                    PowerPreference.getDefaultFile().putString(Utils.Default_Country_code, object.getString("VpnDefaultCountrycode"));
                    PowerPreference.getDefaultFile().putInt(Utils.VpnDialogTime, 1);


                    new LargeNativeAds().loadNativeAds(SplashScreen.this, null);
                    new MiniNativeAds().loadNativeAds(SplashScreen.this, null);
                    new OpenAds().loadOpenAd();
                    new InterAds().loadInterAds(SplashScreen.this);
                    new BackInterAds().loadInterAds(SplashScreen.this);

                    createNotificationChannel();
                    ClientInfo clientInfo = ClientInfo.newBuilder()
                            .addUrl(object.getString("VpnUrl"))
                            .carrierId(object.getString("VpnCarriedId"))
                            .build();

                    List<TransportConfig> transportConfigList = new ArrayList<>();
                    transportConfigList.add(HydraTransportConfig.create());
                    transportConfigList.add(OpenVpnTransportConfig.tcp());
                    transportConfigList.add(OpenVpnTransportConfig.udp());
                    UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);

                    SdkNotificationConfig notificationConfig = SdkNotificationConfig.newBuilder()
                            .title(getResources().getString(R.string.app_name))
                            .channelId("vpn")
                            .clickAction("com.sdk.notification.action")
                            .build();

                    UnifiedSdk.update(notificationConfig);
                    UnifiedSdkConfig config = UnifiedSdkConfig.newBuilder().build();
                    UnifiedSdk.getInstance(clientInfo, config);


                    flag = object.getString("flag");

                    Dialog dialogupdate = new Dialog(SplashScreen.this);
                    dialogupdate.setContentView(R.layout.dialog_update);
                    dialogupdate.setCancelable(false);

                    TextView btnSkip = (TextView) dialogupdate.findViewById(R.id.btnSkip);
                    TextView btnUpdate = (TextView) dialogupdate.findViewById(R.id.btnUpdate);

                    int version = Integer.parseInt(object.getString("version"));

                    if (flag.equals("NORMAL")) {
                        if (PowerPreference.getDefaultFile().getBoolean(Utils.VpnOnOff, false)) {
                            if (PowerPreference.getDefaultFile().getInt(Utils.VpnDialogTime, 1) == 1 && PowerPreference.getDefaultFile().getBoolean(Utils.Vpnaccept, false) && !PowerPreference.getDefaultFile().getBoolean(Utils.isNotify, false)) {
                                login();
                            } else {
                                startActivity(new Intent(SplashScreen.this, VNActivity.class));
                                finish();
                            }
                        } else {
                            HomeScreen();
                        }

                    } else if (flag.equals("SKIP")) {

                        if (version == versioncode) {
                            if (PowerPreference.getDefaultFile().getBoolean(Utils.VpnOnOff, false)) {
                                if (PowerPreference.getDefaultFile().getInt(Utils.VpnDialogTime, 1) == 1 && PowerPreference.getDefaultFile().getBoolean(Utils.Vpnaccept, false) && !PowerPreference.getDefaultFile().getBoolean(Utils.isNotify, false)) {
                                    login();
                                } else {
                                    startActivity(new Intent(SplashScreen.this, VNActivity.class));
                                    finish();
                                }
                            } else {
                                HomeScreen();
                            }
                        } else {
                            dialogupdate.show();
                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final String appName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                                    }
                                }
                            });

                            btnSkip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    HomeScreen();
                                    dialogupdate.cancel();
                                }
                            });
                        }

                    } else if (flag.equals("FORCE")) {
                        if (version == versioncode) {
                            if (PowerPreference.getDefaultFile().getBoolean(Utils.VpnOnOff, false)) {
                                if (PowerPreference.getDefaultFile().getInt(Utils.VpnDialogTime, 1) == 1 && PowerPreference.getDefaultFile().getBoolean(Utils.Vpnaccept, false) && !PowerPreference.getDefaultFile().getBoolean(Utils.isNotify, false)) {
                                    login();
                                } else {
                                    startActivity(new Intent(SplashScreen.this, VNActivity.class));
                                    finish();
                                }
                            } else {
                                HomeScreen();
                            }
                        } else {
                            dialogupdate.show();
                            btnSkip.setVisibility(View.GONE);

                            btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String appName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Dialog dialog = new Dialog(SplashScreen.this);
                dialog.setContentView(R.layout.dialog_no_internet);
                TextView retry = (TextView) dialog.findViewById(R.id.retry);
                dialog.setCancelable(false);
                dialog.show();

                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.checkInternet(SplashScreen.this)) {
                            Updateapi();
                            dialog.cancel();
                        } else {
                            dialog.show();
                        }
                    }
                });
            }
        });
        queue.add(request);
    }

    public void login() {

        AuthMethod authMethod = AuthMethod.anonymous();
        UnifiedSdk.getInstance().getBackend().login(authMethod, new unified.vpn.sdk.Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startVpn();
                    }
                }, 2000);
            }

            @Override
            public void failure(@NonNull VpnException e) {
                Toast.makeText(SplashScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                HomeScreen();
            }
        });
    }

    public void startVpn() {
        List<String> fallbackOrder = new ArrayList<>();
        fallbackOrder.add(HydraTransport.TRANSPORT_ID);
        fallbackOrder.add(OpenVpnTransportConfig.tcp().getName());
        fallbackOrder.add(OpenVpnTransportConfig.udp().getName());

        UnifiedSdk.getInstance().getVpn().start(new SessionConfig.Builder()
                .withReason(TrackingConstants.GprReasons.M_UI)
                .withTransportFallback(fallbackOrder)
                .withTransport(HydraTransport.TRANSPORT_ID)
                .withVirtualLocation(PowerPreference.getDefaultFile().getString(Utils.VpnCountryMain, PowerPreference.getDefaultFile().getString(Utils.Default_Country_code, "us")))
                .build(), new CompletableCallback() {
            @Override
            public void complete() {
                HomeScreen();
            }

            @Override
            public void error(@NonNull VpnException e) {
                Log.e("TAG HELL", e.toString() + " hell");
                HomeScreen();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sample VPN";
            String description = "VPN notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
