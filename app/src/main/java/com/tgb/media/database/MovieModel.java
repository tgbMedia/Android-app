package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import tgb.tmdb.models.Movie;

@Entity
public class MovieModel {
    @Id
    public long id;

    @Index(unique = true)
    public String title;

    public String posterPath;
    public boolean adult;
    public String overview;
    //public List<Integer> genreIds;
    public String originalTitle;
    public String originalLanguage;
    public String backdropPath;
    public float popularity;
    public int voteCount;
    public boolean video;
    public float voteAverage;
    @Generated(hash = 1840626006)
    public MovieModel(long id, String title, String posterPath, boolean adult,
            String overview, String originalTitle, String originalLanguage,
            String backdropPath, float popularity, int voteCount, boolean video,
            float voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }
    @Generated(hash = 1776155561)
    public MovieModel() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(int id) {
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
    public void createFromGsonModel(Movie movie){
        this.id = movie.id;
        this.title = movie.title;
        this.posterPath = movie.posterPath;
        this.adult = movie.adult;
        this.overview = movie.overview;
        //this.genreIds = movie.genreIds;
        this.originalTitle = movie.originalTitle;
        this.originalLanguage = movie.originalLanguage;
        this.backdropPath = movie.backdropPath;
        this.popularity = movie.popularity;
        this.voteCount = movie.voteCount;
        this.video = movie.video;
        this.voteAverage = movie.voteAverage;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", adult=" + adult +
                ", overview='" + overview + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                '}';
    }
    public void setId(long id) {
        this.id = id;
    }
}
