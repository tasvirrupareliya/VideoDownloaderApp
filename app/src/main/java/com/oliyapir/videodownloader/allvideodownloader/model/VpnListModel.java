package com.oliyapir.videodownloader.allvideodownloader.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VpnListModel {
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("flag")
    @Expose
    public int flag;

    public VpnListModel(String code, String name, int flag) {
        this.code = code;
        this.name = name;
        this.flag = flag;
    }
}
