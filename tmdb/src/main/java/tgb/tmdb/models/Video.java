package tgb.tmdb.models;

import com.google.gson.annotations.SerializedName;

public class Video {

    public String name;
    @SerializedName("iso_639_1") public String language;
    @SerializedName("key") public String key;
    public String site;
    public Integer size;
    public String type;

}
