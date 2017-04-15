package com.tgb.media.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

import tgb.tmdb.models.MovieOverview;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class MovieOverviewModel implements Parcelable {
    @Id private Long id;
    private Boolean adult;
    private String backdropPath;
    private Long budget;
    private String homepage;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private Double popularity;
    private String posterPath;
    private Long releaseDate;
    private Long revenue;
    private Long runtime;
    private String status;
    private String tagline;
    private String title;
    private Boolean video;
    private Double voteAverage;
    private Long voteCount;
    private String youtubeTrailer;
    private String serverTitle;

    @ToMany
    @JoinEntity(
            entity = MovieGenreRelation.class,
            sourceProperty = "movieId",
            targetProperty = "genreId"
    )
    private List<GenreModel> genres;

    public MovieOverviewModel(MovieOverview movieOverview, String serverTitle){
        this.id = movieOverview.id;
        this.adult = movieOverview.adult;
        this.backdropPath = movieOverview.backdropPath;
        this.budget = movieOverview.budget;
        this.homepage = movieOverview.homepage;
        this.imdbId = movieOverview.imdbId;
        this.originalLanguage = movieOverview.originalLanguage;
        this.originalTitle = movieOverview.originalTitle;
        this.overview = movieOverview.overview;
        this.popularity = movieOverview.popularity;
        this.posterPath = movieOverview.posterPath;
        this.releaseDate = movieOverview.releaseDate.getDate().getTime();
        this.revenue = movieOverview.revenue;
        this.runtime = movieOverview.runtime;
        this.status = movieOverview.status;
        this.tagline = movieOverview.tagline;
        this.title = movieOverview.title;
        this.video = movieOverview.video;
        this.voteAverage = movieOverview.voteAverage;
        this.voteCount = movieOverview.voteCount;
        this.youtubeTrailer = movieOverview.trailer == null ? "" : movieOverview.trailer.key;
        this.serverTitle = serverTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.adult);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.budget);
        dest.writeString(this.homepage);
        dest.writeString(this.imdbId);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeValue(this.releaseDate);
        dest.writeValue(this.revenue);
        dest.writeValue(this.runtime);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeValue(this.voteCount);
        dest.writeString(this.youtubeTrailer);
        dest.writeString(this.serverTitle);
        dest.writeList(this.genres);
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getAdult() {
        return this.adult;
    }


    public void setAdult(Boolean adult) {
        this.adult = adult;
    }


    public String getBackdropPath() {
        return this.backdropPath;
    }


    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }


    public Long getBudget() {
        return this.budget;
    }


    public void setBudget(Long budget) {
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


    public Double getPopularity() {
        return this.popularity;
    }


    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }


    public String getPosterPath() {
        return this.posterPath;
    }


    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public Long getReleaseDate() {
        return this.releaseDate;
    }


    public void setReleaseDate(Long releaseDate) {
        this.releaseDate = releaseDate;
    }


    public Long getRevenue() {
        return this.revenue;
    }


    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }


    public Long getRuntime() {
        return this.runtime;
    }


    public void setRuntime(Long runtime) {
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


    public Double getVoteAverage() {
        return this.voteAverage;
    }


    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }


    public Long getVoteCount() {
        return this.voteCount;
    }


    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }


    public String getYoutubeTrailer() {
        return this.youtubeTrailer;
    }


    public void setYoutubeTrailer(String youtubeTrailer) {
        this.youtubeTrailer = youtubeTrailer;
    }


    public String getServerTitle() {
        return this.serverTitle;
    }


    public void setServerTitle(String serverTitle) {
        this.serverTitle = serverTitle;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1620741698)
    public List<GenreModel> getGenres() {
        if (genres == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GenreModelDao targetDao = daoSession.getGenreModelDao();
            List<GenreModel> genresNew = targetDao._queryMovieOverviewModel_Genres(id);
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


    protected MovieOverviewModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.backdropPath = in.readString();
        this.budget = (Long) in.readValue(Long.class.getClassLoader());
        this.homepage = in.readString();
        this.imdbId = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.releaseDate = (Long) in.readValue(Long.class.getClassLoader());
        this.revenue = (Long) in.readValue(Long.class.getClassLoader());
        this.runtime = (Long) in.readValue(Long.class.getClassLoader());
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Long) in.readValue(Long.class.getClassLoader());
        this.youtubeTrailer = in.readString();
        this.serverTitle = in.readString();
        this.genres = new ArrayList<GenreModel>();
        in.readList(this.genres, GenreModel.class.getClassLoader());
    }


    @Generated(hash = 1051266377)
    public MovieOverviewModel(Long id, Boolean adult, String backdropPath, Long budget, String homepage,
            String imdbId, String originalLanguage, String originalTitle, String overview,
            Double popularity, String posterPath, Long releaseDate, Long revenue, Long runtime,
            String status, String tagline, String title, Boolean video, Double voteAverage,
            Long voteCount, String youtubeTrailer, String serverTitle) {
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
        this.youtubeTrailer = youtubeTrailer;
        this.serverTitle = serverTitle;
    }


    @Generated(hash = 1270234590)
    public MovieOverviewModel() {
    }

    public static final Creator<MovieOverviewModel> CREATOR = new Creator<MovieOverviewModel>() {
        @Override
        public MovieOverviewModel createFromParcel(Parcel source) {
            return new MovieOverviewModel(source);
        }

        @Override
        public MovieOverviewModel[] newArray(int size) {
            return new MovieOverviewModel[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2000367368)
    private transient MovieOverviewModelDao myDao;

    
}
