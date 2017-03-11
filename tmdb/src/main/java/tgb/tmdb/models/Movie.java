package tgb.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
    public long id;
    public @SerializedName("poster_path") String posterPath;
    public boolean adult;
    public String overview;
    public @SerializedName("genre_ids") List<Integer> genreIds;
    public @SerializedName("original_title") String originalTitle;
    public @SerializedName("original_language") String originalLanguage;
    public String title;
    public @SerializedName("backdrop_path") String backdropPath;
    public @SerializedName("release_date") String relaseDate;
    public float popularity;
    public @SerializedName("vote_count") int voteCount;
    public boolean video;
    public @SerializedName("vote_average") float voteAverage;
}
