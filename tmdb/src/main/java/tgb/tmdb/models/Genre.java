
package tgb.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("name")
    @Expose
    public String name;

}
