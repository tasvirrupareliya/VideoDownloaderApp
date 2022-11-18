package com.oliyapir.videodownloader.allvideodownloader.view.activity;

import static com.preference.provider.PreferenceProvider.context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.adapter.VNMainAdapter;
import com.oliyapir.videodownloader.allvideodownloader.databinding.ActivityVnactivityBinding;
import com.oliyapir.videodownloader.allvideodownloader.model.VpnListModel;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import unified.vpn.sdk.AuthMethod;
import unified.vpn.sdk.Callback;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.User;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnState;

public class VNActivity extends AppCompatActivity {

    ActivityVnactivityBinding binding;
    RecyclerView recyclerView;
    VNMainAdapter.ItemClickListener itemClickListener;
    VNMainAdapter adapter;
    ImageView btn_connect1;
    TextView vpn_status;
    RelativeLayout lay_loader;

    List<VpnListModel> arrayList = new ArrayList<>();
    int timer;
    TextView vpn_country_name;
    CircleImageView cnt_image;
    String currentVPN = "US";
    TextView btn_con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVnactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList.add(new VpnListModel("US", "United States", R.drawable.us_flag));
        arrayList.add(new VpnListModel("CA", "Canada", R.drawable.canada_flag));
        arrayList.add(new VpnListModel("DE", "Germany", R.drawable.germany_flag));
        arrayList.add(new VpnListModel("GB", "United Kingdom", R.drawable.uk_flag));

        recyclerView = findViewById(R.id.select_country);
        vpn_country_name = findViewById(R.id.vpn_country_name);
        cnt_image = findViewById(R.id.flag);
        lay_loader = findViewById(R.id.lay_loader);

        vpn_country_name.setText(PowerPreference.getDefaultFile().getString(Utils.Default_Vpn_Country));

        btn_connect1 = (ImageView) findViewById(R.id.btn_connect1);
        vpn_status = (TextView) findViewById(R.id.vpn_status);
        currentVPN = PowerPreference.getDefaultFile().getString(Utils.VpnCountryMain, PowerPreference.getDefaultFile().getString(Utils.Default_Country_code, "us"));

        btn_connect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpn_status.setText("Connecting..");
                vpn_status.setTextColor(ContextCompat.getColor(context, R.color.blue));
                PowerPreference.getDefaultFile().putString(Utils.VpnCountryMain, currentVPN);
                lay_loader.setVisibility(View.VISIBLE);
                login();
            }
        });


        itemClickListener = new VNMainAdapter.ItemClickListener() {
            @Override
            public void onClick(String s) {
                // Notify adapter
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                currentVPN = s;
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VNMainAdapter(this, arrayList, itemClickListener);
        recyclerView.setAdapter(adapter);

        try {
            btn_con = (TextView) findViewById(R.id.vcontinue);
            btn_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lay_loader.setVisibility(View.VISIBLE);
                    gotoSkip();
                }
            });
        } catch (Exception e) {
            Log.w("Catch", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void gotoSkip() {
        if (PowerPreference.getDefaultFile().getInt(Utils.VpnDialogTime, 0) == 1) {
            PowerPreference.getDefaultFile().putBoolean(Utils.Vpnaccept, true);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                lay_loader.setVisibility(View.VISIBLE);

                if (PowerPreference.getDefaultFile().getBoolean(Utils.StartActivityOnOff)) {
                    startActivity(new Intent(VNActivity.this, StartActivity.class));
                } else {
                    startActivity(new Intent(VNActivity.this, GabMainActivity.class));
                }
            }
        }, 2000);
    }

    public void updateUI() {
        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState vpnState) {
                if (vpnState == VpnState.CONNECTED) {
                    btn_connect1.setImageResource(R.drawable.vp_connected);
                    vpn_status.setText("Connected");
                    vpn_status.setTextColor(ContextCompat.getColor(context, R.color.connect_vpn));

                    if (btn_con != null) {
                        btn_con.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_connect1.setImageResource(R.drawable.vp_connectedn);
                    vpn_status.setText("Not Connected");
                    vpn_status.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    btn_con.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                btn_connect1.setImageResource(R.drawable.vp_connectedn);
                vpn_status.setText("Not Connected");
                vpn_status.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                btn_con.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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
                Log.e("TAG", e.toString());
                gotoSkip();
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
                .withVirtualLocation(PowerPreference.getDefaultFile().getString(Utils.VpnCountryMain, currentVPN))
                .build(), new CompletableCallback() {
            @Override
            public void complete() {
                btn_connect1.setImageResource(R.drawable.vp_connected);

                vpn_status.setText("Connected");
                vpn_status.setTextColor(ContextCompat.getColor(context, R.color.connect_vpn));
                lay_loader.setVisibility(View.VISIBLE);
                gotoSkip();

                Toast.makeText(VNActivity.this, "VPN Connected Successfully. You can continue to application", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(@NonNull VpnException e) {
                Log.e("TAG", e.toString());
                gotoSkip();
            }
        });
    }
}