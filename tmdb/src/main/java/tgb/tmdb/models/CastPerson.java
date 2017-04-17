package tgb.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastPerson {

    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("character")
    @Expose
    public String character;

    @SerializedName("profile_path")
    @Expose
    public String profilePath;

    @SerializedName("order")
    @Expose
    public Long order;

    @SerializedName("name")
    @Expose
    public String name;

}
