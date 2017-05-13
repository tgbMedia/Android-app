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
import com.tgb.media.helper.MovieObesrvableResult;
import com.tgb.media.server.models.MovieFile;

import java.util.List;

import io.reactivex.Observable;
import tgb.tmdb.TmdbAPI;
import tgb.tmdb.models.CastPerson;
import tgb.tmdb.models.CrewPerson;
import tgb.tmdb.models.Genre;
import tgb.tmdb.models.MovieOverview;

public class VideosLibrary {


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

    public MovieOverviewModel searchMovieById(long id){
        MovieOverviewModel movie = movieModelDao.queryBuilder()
                .where(MovieOverviewModelDao.Properties.Id.eq(id))
                .unique();

        return movie;
    }

    private MovieOverviewModel searchMovieByKeyword(final String searchedKeyword) throws Exception{
        KeywordModel keyword = keywordModelDao.queryBuilder()
                .where(KeywordModelDao.Properties.Keyword.eq(searchedKeyword))
                .unique();

        //Search movie by id
        return keyword == null ? null : keyword.getMovie();
    }

    public List<CrewRelationModel> searchPersonByJob(long movieId, String job){
        return crewRelationModelDao.queryBuilder()
                .where(
                        CrewRelationModelDao.Properties.MovieId.eq(movieId),
                        CrewRelationModelDao.Properties.Job.like(job))
                .list();
    }

    private void addKeyword(String keyword, long movieId) throws Exception
    {
        keywordModelDao.insertOrReplace(
                new KeywordModel(keyword, movieId)
        );
    }

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

    public Observable<MovieObesrvableResult> movieDetails(final int position, final MovieFile movie){
        return Observable.create(emitter -> {
            try{
                //Search item by keyword
                MovieOverviewModel movieOverview = searchMovieByKeyword(movie.title);

                if(movieOverview != null)
                    emitter.onNext(new MovieObesrvableResult(position, movieOverview));
                else
                {
                    //Search item in TMDB
                    MovieOverview overview = tmdbAPI.searchMovieByName(movie.title, movie.year);

                    if(overview == null)
                    {
                        Log.wtf("videoLibraries", "No data for: " + movie.title);
                        //emitter.onError(new RuntimeException());
                    }
                    else
                    {
                        //Insert movie to database
                        movieOverview = insertMovieToDb(overview, movie);
                        addKeyword(movie.title, movieOverview.getId());

                        emitter.onNext(new MovieObesrvableResult(position, movieOverview));
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
                emitter.onError(e);
            }

            emitter.onComplete();
        });
    }
}
