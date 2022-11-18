package com.oliyapir.videodownloader.allvideodownloader.view.activity;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.oliyapir.videodownloader.allvideodownloader.util.Utils.createFileFolder;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.databinding.ActivityWhatsappBinding;
import com.oliyapir.videodownloader.allvideodownloader.placements.BackInterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.InterAds;
import com.oliyapir.videodownloader.allvideodownloader.placements.OpenAds;
import com.oliyapir.videodownloader.allvideodownloader.util.AppLangSessionManager;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.oliyapir.videodownloader.allvideodownloader.view.fragment.WhatsappImageFragment;
import com.oliyapir.videodownloader.allvideodownloader.view.fragment.WhatsappVideoFragment;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GabWhatsappActivity extends AppCompatActivity {
    private ActivityWhatsappBinding binding;
    private GabWhatsappActivity activity;

    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_whatsapp);
        activity = this;
        createFileFolder();
        initViews();

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        new OpenAds().loadOpenAd();
        new InterAds().loadInterAds(GabWhatsappActivity.this);
        new BackInterAds().loadInterAds(GabWhatsappActivity.this);
        //AdsUtils.showGoogleBannerAd(activity, binding.adView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;

        if (PowerPreference.getDefaultFile().getBoolean(Utils.FullScreenOnOff, true)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initViews() {
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
            binding.tabs.getTabAt(i).setCustomView(tv);
        }

        binding.LLOpenWhatsapp.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.whatsapp");
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new WhatsappImageFragment(), getResources().getString(R.string.images));
        adapter.addFragment(new WhatsappVideoFragment(), getResources().getString(R.string.videos));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onBackPressed() {
        new BackInterAds().showInterAds(this, new BackInterAds.OnAdClosedListener() {
            @Override
            public void onAdClosed() {
                GabWhatsappActivity.super.onBackPressed();
            }
        });
    }
}
