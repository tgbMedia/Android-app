package com.tgb.media.server.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieFile {

    @SerializedName("year")
    @Expose
    public Long year;

    @SerializedName("resolution")
    @Expose
    public String resolution;

    @SerializedName("quality")
    @Expose
    public String quality;

    @SerializedName("codec")
    @Expose
    public String codec;

    @SerializedName("group")
    @Expose
    public String group;

    @SerializedName("container")
    @Expose
    public String container;

    @SerializedName("title")
    @Expose
    public String title;

}
