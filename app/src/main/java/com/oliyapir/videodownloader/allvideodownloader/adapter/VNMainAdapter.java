package com.oliyapir.videodownloader.allvideodownloader.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.oliyapir.videodownloader.allvideodownloader.R;
import com.oliyapir.videodownloader.allvideodownloader.model.VpnListModel;
import com.oliyapir.videodownloader.allvideodownloader.util.Utils;
import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.List;

public class VNMainAdapter extends RecyclerView.Adapter<VNMainAdapter.ViewHolder> {

    Activity activity;
    // Initialize variable
    List<VpnListModel> arrayList = new ArrayList<>();
    ItemClickListener itemClickListener;
    int selectedPosition = -1;

    // Create constructor

    public interface ItemClickListener {
        void onClick(String s);
    }

    public VNMainAdapter(Activity context, List<VpnListModel> arrayList,
                       ItemClickListener itemClickListener) {
        this.activity = context;
        this.arrayList = arrayList;
        this.itemClickListener = itemClickListener;

        for (int i = 0; i < arrayList.size(); i++) {
            if (PowerPreference.getDefaultFile().getString(Utils.VpnCountryMain, PowerPreference.getDefaultFile().getString(Utils.Default_Country_code, "us")).equalsIgnoreCase(arrayList.get(i).code)) {
                selectedPosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        // Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent,
                        false);
        // Pass holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {


        holder.txtName.setText(arrayList.get(position).name);
        // Checked selected radio button
        holder.radioButton.setChecked(position == selectedPosition);

        if (!activity.isFinishing()) {
            Glide.with(activity)
                    .load(arrayList.get(position).flag)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("LOADDATA-->", "e : " + e.toString());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.ivImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(true);
            }
        });

        // set listener on radio button
        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton,
                            boolean b) {
                        // check condition
                        if (b) {
                            // When checked
                            // update selected position
                            selectedPosition = holder.getAdapterPosition();
                            // Call listener
                            itemClickListener.onClick(arrayList.get(position).code);
                        }
                    }
                });
    }

    @Override
    public long getItemId(int position) {
        // pass position
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // pass position
        return position;
    }

    @Override
    public int getItemCount() {
        // pass total list size
        return arrayList.size();
    }

    public void refreshData(List<VpnListModel> arrayList) {
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        // Initialize variable
        RadioButton radioButton;
        TextView txtName;
        TextView code;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Assign variable
            radioButton
                    = itemView.findViewById(R.id.radio_button);
            txtName = itemView.findViewById(R.id.vpn_country_name);
            code = itemView.findViewById(R.id.code);
            ivImage = itemView.findViewById(R.id.flag);
        }
    }
}
