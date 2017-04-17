package tgb.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("cast")
    @Expose
    public List<CastPerson> cast;


    @SerializedName("crew")
    @Expose
    public List<CrewPerson> crew;

}
