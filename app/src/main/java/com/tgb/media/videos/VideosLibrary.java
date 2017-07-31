package com.tgb.media.videos;

import android.util.Log;

import com.tgb.media.database.CastRelationModel;
import com.tgb.media.database.CastRelationModelDao;
import com.tgb.media.database.CrewRelationModel;
import com.tgb.media.database.CrewRelationModelDao;
import com.tgb.media.database.DaoSession;
import com.tgb.media.database.GenreModel;
import com.tgb.media.database.GenreModelDao;
import com.tgb.media.database.KeywordModel;
import com.tgb.media.database.KeywordModelDao;
import com.tgb.media.database.MovieGenreRelation;
import com.tgb.media.database.MovieGenreRelationDao;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.database.MovieOverviewModelDao;
import com.tgb.media.database.PersonModel;
import com.tgb.media.database.PersonModelDao;
import com.tgb.media.helper.MovieObservableResult;
import com.tgb.media.server.models.MovieFile;

import java.util.List;

import hugo.weaving.DebugLog;
import tgb.tmdb.TmdbAPI;
import tgb.tmdb.models.CastPerson;
import tgb.tmdb.models.CrewPerson;
import tgb.tmdb.models.Genre;
import tgb.tmdb.models.MovieOverview;
import timber.log.Timber;

public class VideosLibrary {

    //Tags
    private static final String TAG = VideosLibrary.class.getName();

    //Database models
    private MovieOverviewModelDao movieModelDao;
    private KeywordModelDao keywordModelDao;
    private GenreModelDao genreModelDao;
    private MovieGenreRelationDao movieGenreRelationDao;
    private PersonModelDao personModelDao;
    private CastRelationModelDao castRelationModelDao;
    private CrewRelationModelDao crewRelationModelDao;

    //TMDB
    private TmdbAPI tmdbAPI;

    //Jobs
    public static final String DIRECTOR = "director";

    public VideosLibrary(DaoSession daoSession, TmdbAPI tmdbAPI){
        //Database models
        this.movieModelDao = daoSession.getMovieOverviewModelDao();
        this.keywordModelDao = daoSession.getKeywordModelDao();
        this.genreModelDao = daoSession.getGenreModelDao();
        this.movieGenreRelationDao = daoSession.getMovieGenreRelationDao();
        this.personModelDao = daoSession.getPersonModelDao();
        this.castRelationModelDao = daoSession.getCastRelationModelDao();
        this.crewRelationModelDao = daoSession.getCrewRelationModelDao();

        //TMDB
        this.tmdbAPI = tmdbAPI;
    }

    @DebugLog
    public MovieOverviewModel searchMovieById(long id){
        MovieOverviewModel movie = movieModelDao.queryBuilder()
                .where(MovieOverviewModelDao.Properties.Id.eq(id))
                .unique();

        return movie;
    }

    @DebugLog
    private MovieOverviewModel searchMovieByKeyword(final String searchedKeyword) throws Exception{
        KeywordModel keyword = keywordModelDao.queryBuilder()
                .where(KeywordModelDao.Properties.Keyword.eq(searchedKeyword))
                .unique();

        //Search movie by id
        return keyword == null ? null : keyword.getMovie();
    }

    @DebugLog
    public List<CrewRelationModel> searchPersonByJob(long movieId, String job){
        return crewRelationModelDao.queryBuilder()
                .where(
                        CrewRelationModelDao.Properties.MovieId.eq(movieId),
                        CrewRelationModelDao.Properties.Job.like(job))
                .list();
    }

    @DebugLog
    private void addKeyword(String keyword, long movieId) throws Exception
    {
        keywordModelDao.insertOrReplace(
                new KeywordModel(keyword, movieId)
        );
    }

    @DebugLog
    private MovieOverviewModel insertMovieToDb(MovieOverview movieOverview, MovieFile movie)
            throws Exception{

        //Insert the movie to the database
        MovieOverviewModel movieOverviewModel = new MovieOverviewModel(movieOverview, movie);

        movieModelDao.insertOrReplace(
                movieOverviewModel
        );

        //Cast
        for(CastPerson cast : movieOverview.credits.cast){
            //Create or update person
            PersonModel person = new PersonModel(cast.id, cast.profilePath, cast.name);
            personModelDao.insertOrReplace(person);

            //Create relation
            CastRelationModel castRelation = new CastRelationModel(
                    movieOverview.id,
                    cast.id,
                    cast.character,
                    cast.order
            );

            castRelationModelDao.insertOrReplace(castRelation);
        }

        //Crew
        for(CrewPerson crew : movieOverview.credits.crew){
            //Create or update person
            PersonModel person = new PersonModel(crew.id, crew.profilePath, crew.name);
            personModelDao.insertOrReplace(person);

            //Create relation
            CrewRelationModel crewRelation = new CrewRelationModel(
                    movieOverview.id,
                    crew.id,
                    crew.department,
                    crew.job.toLowerCase()
            );

            crewRelationModelDao.insertOrReplace(crewRelation);
        }

        //Add genres to db
        for(Genre genre : movieOverview.genres) {

            genreModelDao.insertOrReplace(new GenreModel(
                    genre.id,
                    genre.name
            ));

            //Add relation
            MovieGenreRelation movieGenreRelation = new MovieGenreRelation();

            movieGenreRelation.setGenreId(genre.id);
            movieGenreRelation.setMovieId(movieOverview.id);

            movieGenreRelationDao.insertOrReplace(movieGenreRelation);
        };


        return movieOverviewModel;
    }

    @DebugLog
    public MovieObservableResult videoDetails(final int position, final MovieFile movie){
        try{
            //Search item by keyword
            MovieOverviewModel movieOverview = searchMovieByKeyword(movie.title);

            if(movieOverview != null)
            {
                if(!movieOverview.getServerId().equals(movie.id))
                {
                    movieOverview.setServerId(movie.id);
                    movieOverview.update();

                    Timber.tag(TAG).wtf("VideosLibrary->videoDetails, %s New movie id: %s",
                            movie.title, movie.id);
                }

                return new MovieObservableResult(position, movieOverview);
            }
            else
            {
                //Search item in TMDB
                int movieYear = (movie.year == null) ? 0 : movie.year;

                MovieOverview overview = tmdbAPI.searchMovieByName(movie.title, movieYear);

                if(overview == null)
                    Timber.tag(TAG).wtf("No data for: %s", movie.title);
                else
                {
                    //Insert movie to database
                    movieOverview = insertMovieToDb(overview, movie);
                    addKeyword(movie.title, movieOverview.getId());

                    return new MovieObservableResult(position, movieOverview);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Log.wtf("videoLibraries", "Returns null for: " + movie.title);

        return new MovieObservableResult(position, null);
    }
}
