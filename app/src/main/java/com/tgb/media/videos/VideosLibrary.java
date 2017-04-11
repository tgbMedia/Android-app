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
import com.tgb.media.helper.MovieObesrvableResult;

import io.reactivex.Observable;
import tgb.tmdb.TmdbAPI;
import tgb.tmdb.models.MovieOverview;

public class VideosLibrary {


    //Database models
    private MovieOverviewModelDao movieModelDao;
    private KeywordModelDao keywordModelDao;
    private GenreModelDao genreModelDao;
    private MovieGenreRelationDao movieGenreRelationDao;

    //TMDB
    private TmdbAPI tmdbAPI;

    public VideosLibrary(DaoSession daoSession, TmdbAPI tmdbAPI){
        //Database models
        this.movieModelDao = daoSession.getMovieOverviewModelDao();
        this.keywordModelDao = daoSession.getKeywordModelDao();
        this.genreModelDao = daoSession.getGenreModelDao();
        this.movieGenreRelationDao = daoSession.getMovieGenreRelationDao();

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

    private void addKeyword(String keyword, long movieId) throws Exception
    {
        keywordModelDao.insertOrReplace(
                new KeywordModel(keyword, movieId)
        );
    }

    private MovieOverviewModel insertMovieToDb(MovieOverview movieOverview, String serverTitle)
            throws Exception{

        //Insert the movie to the databse
        MovieOverviewModel movieOverviewModel = new MovieOverviewModel(movieOverview, serverTitle);

        movieModelDao.insertOrReplace(
                movieOverviewModel
        );

        //Add genres to db
        movieOverview.genres.forEach(genre -> {

            genreModelDao.insertOrReplace(new GenreModel(
                    genre.id,
                    genre.name
            ));

            //Add relation
            MovieGenreRelation movieGenreRelation = new MovieGenreRelation();

            movieGenreRelation.setGenreId(genre.id);
            movieGenreRelation.setMovieId(movieOverview.id);

            movieGenreRelationDao.insertOrReplace(movieGenreRelation);
        });


        return movieOverviewModel;
    }

    public Observable<MovieObesrvableResult> movieDetails(final int position, final String movieName){
        return Observable.create(emitter -> {
            try{
                //Search item by keyword
                MovieOverviewModel movieOverview = searchMovieByKeyword(movieName);

                if(movieOverview != null)
                    emitter.onNext(new MovieObesrvableResult(position, movieOverview));
                else
                {
                    //Search item in TMDB
                    MovieOverview overview = tmdbAPI.searchMovieByName(movieName);

                    if(overview == null)
                    {
                        Log.wtf("videoLibraries", "No data for: " + movieName);
                        //emitter.onError(new RuntimeException());
                    }
                    else
                    {
                        //Insert movie to database
                        movieOverview = insertMovieToDb(overview, movieName);
                        addKeyword(movieName, movieOverview.getId());

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
