package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeywordModel {

    @Index(unique = true)
    public String keyword;

    public long movieId;

    @Generated(hash = 1543743071)
    public KeywordModel(String keyword, long movieId) {
        this.keyword = keyword;
        this.movieId = movieId;
    }

    @Generated(hash = 381496246)
    public KeywordModel() {
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getMovieId() {
        return this.movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

}
