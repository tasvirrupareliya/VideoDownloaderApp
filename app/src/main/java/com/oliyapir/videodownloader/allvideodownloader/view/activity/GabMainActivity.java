package com.oliyapir.videodownloader.allvideodownloader.view.activity;

import static com.oliyapir.videodownloader.allvideodownloader.util.Utils.createFileFolder;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.databinding.ActivityMainBinding;
import com.oliyapir.videodownloader.allvideodownloader.placements.BackInterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.InterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.LargeNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.MiniNativeAds;
import com.oliyapir.videodownloader.allvideodownloader.util.AppLangSessionManager;
import com.oliyapir.videodownloader.allvideodownloader.util.ClipboardListener;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;

public class GabMainActivity extends AppCompatActivity implements View.OnClickListener {
    GabMainActivity activity;
    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String CopyKey = "";
    String CopyValue = "";

    AppLangSessionManager appLangSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activity = this;

        appLangSessionManager = new AppLangSessionManager(activity);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        if (PowerPreference.getDefaultFile().getBoolean(Utils.FullScreenOnOff, true)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        new LargeNativeAds().showNativeAds(this, null);
        new LargeNativeAds().showListNativeAds(this, binding.nativeAd2, binding.adSpace2);
        new MiniNativeAds().showNativeAds(this, null);
    }

    public void initViews() {

        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    CopyValue = extractLinks(CopyValue);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

        binding.rvLikee.setOnClickListener(this);
        binding.rvInsta.setOnClickListener(this);
        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvTikTok.setOnClickListener(this);
        binding.rvFB.setOnClickListener(this);
        binding.rvTwitter.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);
        binding.rvSnack.setOnClickListener(this);
        binding.rvShareChat.setOnClickListener(this);
        binding.rvRoposo.setOnClickListener(this);
        binding.rvJosh.setOnClickListener(this);
        binding.rvChingari.setOnClickListener(this);
        binding.rvMitron.setOnClickListener(this);
        binding.rvMoj.setOnClickListener(this);
        binding.rvMX.setOnClickListener(this);
        binding.rvGames.setOnClickListener(this);

        //TODO :  Change Language Dialog Open
        binding.rvChangeLang.setOnClickListener(v -> {
            final BottomSheetDialog dialogSortBy = new BottomSheetDialog(GabMainActivity.this, R.style.SheetDialog);
            dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSortBy.setContentView(R.layout.dialog_language);
            final TextView tv_english = dialogSortBy.findViewById(R.id.tv_english);
            final TextView tv_hindi = dialogSortBy.findViewById(R.id.tv_hindi);
            final TextView tv_cancel = dialogSortBy.findViewById(R.id.tv_cancel);
            final TextView tvArabic = dialogSortBy.findViewById(R.id.tvArabic);
            dialogSortBy.show();
            tv_english.setOnClickListener(view -> {
                setLocale("en");
                appLangSessionManager.setLanguage("en");
            });
            tv_hindi.setOnClickListener(view -> {
                setLocale("hi");
                appLangSessionManager.setLanguage("hi");
            });
            tvArabic.setOnClickListener(view -> {
                setLocale("ar");
                appLangSessionManager.setLanguage("ar");
            });
            tv_cancel.setOnClickListener(view -> dialogSortBy.dismiss());

        });

        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("likee")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
            } else if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com") || CopiedText.contains("fb")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            } else if (CopiedText.contains("sharechat")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
            } else if (CopiedText.contains("roposo")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
            } else if (CopiedText.contains("snackvideo") || CopiedText.contains("sck.io")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
            } else if (CopiedText.contains("josh")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(110);
                } else {
                    callJoshActivity();
                }
            } else if (CopiedText.contains("chingari")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(111);
                } else {
                    callChingariActivity();
                }
            } else if (CopiedText.contains("mitron")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(112);
                } else {
                    callMitronActivity();
                }
            } else if (CopiedText.contains("mxtakatak")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(113);
                } else {
                    callMXActivity();
                }
            } else if (CopiedText.contains("moj")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(114);
                } else {
                    callMojActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {
            case R.id.rvLikee:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
                break;
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;

            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                break;
            case R.id.rvFB:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvTwitter:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.rvAbout:
                i = new Intent(activity, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.rvShareApp:
                Utils.ShareApp(activity);
                break;

            case R.id.rvRateApp:
                Utils.RateApp(activity);
                break;
            case R.id.rvMoreApp:
                Utils.MoreApp(activity);
                break;
            case R.id.rvShareChat:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
                break;
            case R.id.rvRoposo:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
                break;
            case R.id.rvSnack:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
                break;
            case R.id.rvJosh:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(110);
                } else {
                    callJoshActivity();
                }
                break;
            case R.id.rvChingari:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(111);
                } else {
                    callChingariActivity();
                }
                break;
            case R.id.rvMitron:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(112);
                } else {
                    callMitronActivity();
                }
                break;
            case R.id.rvMX:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(113);
                } else {
                    callMXActivity();
                }
                break;

            case R.id.rvMoj:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(114);
                } else {
                    callMojActivity();
                }
                break;
            case R.id.rvGames:
                callGamesActivity();
                break;
        }
    }


    public void callJoshActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabJoshActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callChingariActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabChingariActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callMitronActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, MitronActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callMXActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, MXTakaTakActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callMojActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabMojActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }


    public void callLikeeActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabLikeeActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callInstaActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabInstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }


    public void callWhatsappActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabWhatsappActivity.class);
                startActivity(i);
            }
        });
    }

    public void callTikTokActivity() {
        Intent i = new Intent(activity, TikTokActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callFacebookActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, FacebookActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callTwitterActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabTwitterActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }


    public void callGalleryActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, GabGalleryActivity.class);
                startActivity(i);
            }
        });
    }

    public void callRoposoActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, RoposoActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callShareChatActivity() {
        new InterAds().showInterAds(this, new InterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(activity, ShareChatActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }
        });
    }

    public void callSnackVideoActivity() {

        Intent i = new Intent(activity, SnackVideoActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callGamesActivity() {
        Intent i = new Intent(activity, AllGamesActivity.class);
        startActivity(i);
    }


    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(activity, GabMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon_round)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.drawable.icon_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {
            if (type == 100) {
                callLikeeActivity();
            } else if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 103) {
                callTikTokActivity();
            } else if (type == 104) {
                callFacebookActivity();
            } else if (type == 105) {
                callGalleryActivity();
            } else if (type == 106) {
                callTwitterActivity();
            } else if (type == 107) {
                callShareChatActivity();
            } else if (type == 108) {
                callRoposoActivity();
            } else if (type == 109) {
                callSnackVideoActivity();
            } else if (type == 110) {
                callJoshActivity();
            } else if (type == 111) {
                callChingariActivity();
            } else if (type == 112) {
                callMitronActivity();
            } else if (type == 113) {
                callMXActivity();
            } else if (type == 114) {
                callMojActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLikeeActivity();
            } else {
            }
            return;
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            } else {
            }
            return;
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        } else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            } else {
            }
            return;
        } else if (requestCode == 107) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callShareChatActivity();
            } else {
            }
            return;
        } else if (requestCode == 108) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callRoposoActivity();
            } else {
            }
            return;
        } else if (requestCode == 109) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callSnackVideoActivity();
            } else {
            }
            return;
        } else if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callJoshActivity();
            }
        } else if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callChingariActivity();
            }
        } else if (requestCode == 112) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMitronActivity();
            }
        } else if (requestCode == 113) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMXActivity();
            }
        } else if (requestCode == 114) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMojActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (PowerPreference.getDefaultFile().getBoolean(Utils.StartActivityOnOff, false)) {
            new BackInterAds().showInterAds(this, new BackInterAds.OnAdClosedListener() {
                @Override
                public void onAdClosed() {
                    finish();
                }
            });
        } else {
            exitdialog();
        }
    }

    private void exitdialog() {
        try {

            Dialog dialog = new Dialog(GabMainActivity.this);
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
                    new LargeNativeAds().showNativeAds(GabMainActivity.this, dialog);
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
                        new InterAds().showInterAds(GabMainActivity.this, new InterAds.OnAdClosedListener() {
                            @Override
                            public void onAdClosed() {
                                Intent intent = new Intent(GabMainActivity.this, ExitActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Intent intent = new Intent(GabMainActivity.this, ExitActivity.class);
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

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(GabMainActivity.this, GabMainActivity.class);
        startActivity(refresh);
        finish();
    }

    public static String extractLinks(String text) {
        Matcher m = Patterns.WEB_URL.matcher(text);
        String url = "";
        while (m.find()) {
            url = m.group();
            Log.d("New URL", "URL extracted: " + url);

            break;
        }
        return url;
    }

}
