package com.tgb.media.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import tgb.tmdb.models.Movie;


@Entity(
        indexes = {
                @Index(value = "id, title", unique = true)
        }
)
public class MovieModel implements Parcelable {

    public long id;
    public String title;
    public String posterPath;
    public boolean adult;
    public String overview;
    public String originalTitle;
    public String originalLanguage;
    public String relaseDate;
    public String backdropPath;
    public float popularity;
    public int voteCount;
    public boolean video;
    public float voteAverage;

    @Generated(hash = 1518686642)
    public MovieModel(long id, String title, String posterPath, boolean adult,
            String overview, String originalTitle, String originalLanguage,
            String relaseDate, String backdropPath, float popularity, int voteCount,
            boolean video, float voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.relaseDate = relaseDate;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    @Generated(hash = 1776155561)
    public MovieModel() {
    }

        public void createFromGsonModel(Movie movie){
            this.id = movie.id;
            this.title = movie.title;
            this.posterPath = movie.posterPath;
            this.adult = movie.adult;
            this.overview = movie.overview;
            this.relaseDate = movie.relaseDate;
            //this.genreIds = movie.genreIds;
            this.originalTitle = movie.originalTitle;
            this.originalLanguage = movie.originalLanguage;
            this.backdropPath = movie.backdropPath;
            this.popularity = movie.popularity;
            this.voteCount = movie.voteCount;
            this.video = movie.video;
            this.voteAverage = movie.voteAverage;
        }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean getAdult() {
        return this.adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return this.originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getRelaseDate() {
        return this.relaseDate;
    }

    public void setRelaseDate(String relaseDate) {
        this.relaseDate = relaseDate;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return this.popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return this.voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean getVideo() {
        return this.video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.relaseDate);
        dest.writeString(this.backdropPath);
        dest.writeFloat(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.voteAverage);
    }

    protected MovieModel(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.relaseDate = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readFloat();
        this.voteCount = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readFloat();
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
