package com.tgb.media.server.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieFile {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("year")
    @Expose
    public Integer year;

}
