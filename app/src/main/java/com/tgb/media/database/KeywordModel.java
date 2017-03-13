package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeywordModel {

    @Index(unique = true)
    public String keyword;
    public long movieId;
    public String apiLanguage;

    @Generated(hash = 1766529829)
    public KeywordModel(String keyword, long movieId, String apiLanguage) {
        this.keyword = keyword;
        this.movieId = movieId;
        this.apiLanguage = apiLanguage;
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

    public String getApiLanguage() {
        return this.apiLanguage;
    }

    public void setApiLanguage(String apiLanguage) {
        this.apiLanguage = apiLanguage;
    }

}
