package tgb.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrewPerson {

    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("department")
    @Expose
    public String department;

    @SerializedName("job")
    @Expose
    public String job;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("profile_path")
    @Expose
    public String profilePath;

}