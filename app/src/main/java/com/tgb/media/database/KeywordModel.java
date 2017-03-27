package com.tgb.media.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class KeywordModel{

    public @Index(unique = false)String keyword;
    public long movieId;

    @ToOne(joinProperty = "movieId")
    public MovieOverviewModel movie;

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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1289461985)
    public MovieOverviewModel getMovie() {
        long __key = this.movieId;
        if (movie__resolvedKey == null || !movie__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MovieOverviewModelDao targetDao = daoSession.getMovieOverviewModelDao();
            MovieOverviewModel movieNew = targetDao.load(__key);
            synchronized (this) {
                movie = movieNew;
                movie__resolvedKey = __key;
            }
        }
        return movie;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 70566292)
    public void setMovie(@NotNull MovieOverviewModel movie) {
        if (movie == null) {
            throw new DaoException(
                    "To-one property 'movieId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.movie = movie;
            movieId = movie.getId();
            movie__resolvedKey = movieId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public KeywordModel() {
    }

    @Generated(hash = 1543743071)
    public KeywordModel(String keyword, long movieId) {
        this.keyword = keyword;
        this.movieId = movieId;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 969768800)
    private transient KeywordModelDao myDao;
    @Generated(hash = 708760245)
    private transient Long movie__resolvedKey;

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 552997055)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getKeywordModelDao() : null;
    }
}
