package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MovieGenreRelation {

    public @NotNull @Index(unique = false) long movieId;
    public @NotNull long genreId;
    @Generated(hash = 521213356)
    public MovieGenreRelation(long movieId, long genreId) {
        this.movieId = movieId;
        this.genreId = genreId;
    }
    @Generated(hash = 1292453685)
    public MovieGenreRelation() {
    }
    public long getMovieId() {
        return this.movieId;
    }
    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
    public long getGenreId() {
        return this.genreId;
    }
    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    
}
