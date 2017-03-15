package com.tgb.media.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(
        indexes = {
                @Index(value = "id, apiLanguage", unique = true)
        }
)
public class MovieOverviewModel {
    @Id
    long id;
    boolean adult;
    String backdropPath;
    long budget;
    @ToMany(referencedJoinProperty = "movieId")
    List<MovieGenreRelation> genres = null;
    String homepage;
    String imdbId;
    String originalLanguage;
    String originalTitle;
    String overview;
    double popularity;
    String posterPath;
    String releaseDate;
    long revenue;
    long runtime;
    String status;
    String tagline;
    String title;
    Boolean video;
    double voteAverage;
    long voteCount;
    @Index String apiLanguage;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 2000367368)
private transient MovieOverviewModelDao myDao;
@Generated(hash = 1646726244)
public MovieOverviewModel(long id, boolean adult, String backdropPath,
        long budget, String homepage, String imdbId, String originalLanguage,
        String originalTitle, String overview, double popularity,
        String posterPath, String releaseDate, long revenue, long runtime,
        String status, String tagline, String title, Boolean video,
        double voteAverage, long voteCount, String apiLanguage) {
    this.id = id;
    this.adult = adult;
    this.backdropPath = backdropPath;
    this.budget = budget;
    this.homepage = homepage;
    this.imdbId = imdbId;
    this.originalLanguage = originalLanguage;
    this.originalTitle = originalTitle;
    this.overview = overview;
    this.popularity = popularity;
    this.posterPath = posterPath;
    this.releaseDate = releaseDate;
    this.revenue = revenue;
    this.runtime = runtime;
    this.status = status;
    this.tagline = tagline;
    this.title = title;
    this.video = video;
    this.voteAverage = voteAverage;
    this.voteCount = voteCount;
    this.apiLanguage = apiLanguage;
}
@Generated(hash = 1270234590)
public MovieOverviewModel() {
}
public long getId() {
    return this.id;
}
public void setId(long id) {
    this.id = id;
}
public boolean getAdult() {
    return this.adult;
}
public void setAdult(boolean adult) {
    this.adult = adult;
}
public String getBackdropPath() {
    return this.backdropPath;
}
public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
}
public long getBudget() {
    return this.budget;
}
public void setBudget(long budget) {
    this.budget = budget;
}
public String getHomepage() {
    return this.homepage;
}
public void setHomepage(String homepage) {
    this.homepage = homepage;
}
public String getImdbId() {
    return this.imdbId;
}
public void setImdbId(String imdbId) {
    this.imdbId = imdbId;
}
public String getOriginalLanguage() {
    return this.originalLanguage;
}
public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
}
public String getOriginalTitle() {
    return this.originalTitle;
}
public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
}
public String getOverview() {
    return this.overview;
}
public void setOverview(String overview) {
    this.overview = overview;
}
public double getPopularity() {
    return this.popularity;
}
public void setPopularity(double popularity) {
    this.popularity = popularity;
}
public String getPosterPath() {
    return this.posterPath;
}
public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
}
public String getReleaseDate() {
    return this.releaseDate;
}
public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
}
public long getRevenue() {
    return this.revenue;
}
public void setRevenue(long revenue) {
    this.revenue = revenue;
}
public long getRuntime() {
    return this.runtime;
}
public void setRuntime(long runtime) {
    this.runtime = runtime;
}
public String getStatus() {
    return this.status;
}
public void setStatus(String status) {
    this.status = status;
}
public String getTagline() {
    return this.tagline;
}
public void setTagline(String tagline) {
    this.tagline = tagline;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public Boolean getVideo() {
    return this.video;
}
public void setVideo(Boolean video) {
    this.video = video;
}
public double getVoteAverage() {
    return this.voteAverage;
}
public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
}
public long getVoteCount() {
    return this.voteCount;
}
public void setVoteCount(long voteCount) {
    this.voteCount = voteCount;
}
public String getApiLanguage() {
    return this.apiLanguage;
}
public void setApiLanguage(String apiLanguage) {
    this.apiLanguage = apiLanguage;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1723882281)
public List<MovieGenreRelation> getGenres() {
    if (genres == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        MovieGenreRelationDao targetDao = daoSession.getMovieGenreRelationDao();
        List<MovieGenreRelation> genresNew = targetDao
                ._queryMovieOverviewModel_Genres(id);
        synchronized (this) {
            if (genres == null) {
                genres = genresNew;
            }
        }
    }
    return genres;
}
/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1988821389)
public synchronized void resetGenres() {
    genres = null;
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
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 719929430)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getMovieOverviewModelDao() : null;
}
}
