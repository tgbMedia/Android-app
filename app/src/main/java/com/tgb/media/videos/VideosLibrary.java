package com.tgb.media.videos;

import android.util.Log;

import com.tgb.media.database.DaoSession;
import com.tgb.media.database.GenreModel;
import com.tgb.media.database.GenreModelDao;
import com.tgb.media.database.KeywordModel;
import com.tgb.media.database.KeywordModelDao;
import com.tgb.media.database.MovieGenreRelation;
import com.tgb.media.database.MovieGenreRelationDao;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.database.MovieOverviewModelDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import tgb.tmdb.TmdbAPI;
import tgb.tmdb.models.Genre;
import tgb.tmdb.models.MovieOverview;

public class VideosLibrary {

    //Database
    private DaoSession daoSession;

    //Database models
    private MovieOverviewModelDao movieModelDao;
    private KeywordModelDao keywordModelDao;
    private GenreModelDao genreModelDao;
    private MovieGenreRelationDao movieGenreRelationDao;

    //TMDB
    private TmdbAPI tmdbAPI;

    public VideosLibrary(DaoSession daoSession, TmdbAPI tmdbAPI){
        //Database
        this.daoSession = daoSession;

        //Database models
        this.movieModelDao = daoSession.getMovieOverviewModelDao();
        this.keywordModelDao = daoSession.getKeywordModelDao();
        this.genreModelDao = daoSession.getGenreModelDao();
        this.movieGenreRelationDao = daoSession.getMovieGenreRelationDao();

        //TMDB
        this.tmdbAPI = tmdbAPI;
    }

    private MovieOverviewModel searchMovieById(long id){
        List<MovieOverviewModel> movie = movieModelDao.queryBuilder()
                .where(MovieOverviewModelDao.Properties.Id.eq(id))
                .limit(1)
                .list();

        if(movie.size() == 0)
            return null;

        return movie.get(0);
    }

    private MovieOverviewModel searchMovieByKeyword(final String searchedKeyword){
        List<KeywordModel> keyword = keywordModelDao.queryBuilder()
                .where(KeywordModelDao.Properties.Keyword.eq(searchedKeyword))
                .limit(1)
                .list();

        if(keyword.size() == 0)
            return null;

        //Search movie by id
        return searchMovieById(keyword.get(0).movieId);
    }

    private void addKeyword(String keyword, long movieId) throws Exception
    {
        keywordModelDao.insertOrReplace(
                new KeywordModel(keyword, movieId)
        );
    }

    private void addGenre(Genre genre){
        genreModelDao.insertOrReplace(new GenreModel(genre.id, genre.name));
    }

    private MovieOverviewModel inserMovieToDb(MovieOverview movieOverview) throws Exception{

        //Add genres to db & create list of genre relations
        movieOverview.genres.forEach(genre ->{
            addGenre(genre);

            movieGenreRelationDao.insertOrReplace(
                    new MovieGenreRelation(genre.id, movieOverview.id)
            );
        });

        //Cast MovieOverview(GSON) to MovieOverviewModel & insert movie to database
        movieModelDao.insertOrReplace(
                new MovieOverviewModel(movieOverview)
        );

        return searchMovieById(movieOverview.id);
    }

    public Observable<MovieOverviewModel> details(final List<String> moviesName){
        return Observable.create(emitter -> {

            MovieOverviewModel movieOverview = null;

            for(String movieName : moviesName){
                try{
                    //Search item by keyword
                    movieOverview = searchMovieByKeyword(movieName);

                    if(movieOverview != null)
                    {
                        emitter.onNext(movieOverview);
                        continue;
                    }

                    //Search item in TMDB
                    MovieOverview overview = tmdbAPI.searchMovieByName(movieName);

                    if(overview == null)
                    {
                        Log.wtf("videoLibraries", "No data for: " + movieName);
                        //emitter.onError(new RuntimeException());
                        continue;
                    }

                    //Insert movie to database
                    movieOverview = inserMovieToDb(overview);
                    addKeyword(movieName, movieOverview.id);

                    emitter.onNext(movieOverview);

                }
                catch(Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }

            }

            emitter.onComplete();

        });
    }

}
