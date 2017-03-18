package com.tgb.media.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import tgb.tmdb.models.MovieOverview;

@Entity
public class MovieOverviewModel implements Parcelable {
    public @Id long id;
    public boolean adult;
    public String backdropPath;
    public long budget;
    public String homepage;
    public String imdbId;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public double popularity;
    public String posterPath;
    public String releaseDate;
    public long revenue;
    public long runtime;
    public String status;
    public String tagline;
    public String title;
    public Boolean video;
    public double voteAverage;
    public long voteCount;

    @ToMany @JoinEntity(
            entity = MovieGenreRelation.class,
            sourceProperty = "movieId",
            targetProperty = "genreId"
    )
    List<GenreModel> genres;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2000367368)
    private transient MovieOverviewModelDao myDao;

    public MovieOverviewModel(MovieOverview movieOverview){
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
        this.releaseDate = movieOverview.releaseDate;
        this.revenue = movieOverview.revenue;
        this.runtime = movieOverview.runtime;
        this.status = movieOverview.status;
        this.tagline = movieOverview.tagline;
        this.title = movieOverview.title;
        this.video = movieOverview.video;
        this.voteAverage = movieOverview.voteAverage;
        this.voteCount = movieOverview.voteCount;
    }

    @Generated(hash = 384437110)
    public MovieOverviewModel(long id, boolean adult, String backdropPath,
            long budget, String homepage, String imdbId, String originalLanguage,
            String originalTitle, String overview, double popularity,
            String posterPath, String releaseDate, long revenue, long runtime,
            String status, String tagline, String title, Boolean video,
            double voteAverage, long voteCount) {
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
            List<GenreModel> genresNew = targetDao
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);
        dest.writeLong(this.budget);
        dest.writeString(this.homepage);
        dest.writeString(this.imdbId);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeLong(this.revenue);
        dest.writeLong(this.runtime);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeValue(this.video);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.voteCount);
        dest.writeTypedList(this.genres);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 719929430)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMovieOverviewModelDao() : null;
    }

    protected MovieOverviewModel(Parcel in) {
        this.id = in.readLong();
        this.adult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.budget = in.readLong();
        this.homepage = in.readString();
        this.imdbId = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.revenue = in.readLong();
        this.runtime = in.readLong();
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
        this.genres = in.createTypedArrayList(GenreModel.CREATOR);
    }

    public static final Parcelable.Creator<MovieOverviewModel> CREATOR = new Parcelable.Creator<MovieOverviewModel>() {
        @Override
        public MovieOverviewModel createFromParcel(Parcel source) {
            return new MovieOverviewModel(source);
        }

        @Override
        public MovieOverviewModel[] newArray(int size) {
            return new MovieOverviewModel[size];
        }
    };
}
