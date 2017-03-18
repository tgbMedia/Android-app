package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MovieGenreRelation {

    @Id private Long id;
    public Long movieId;
    public Long genreId;
    @Generated(hash = 1591858491)
    public MovieGenreRelation(Long id, Long movieId, Long genreId) {
        this.id = id;
        this.movieId = movieId;
        this.genreId = genreId;
    }
    @Generated(hash = 1292453685)
    public MovieGenreRelation() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMovieId() {
        return this.movieId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public Long getGenreId() {
        return this.genreId;
    }
    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }


}
