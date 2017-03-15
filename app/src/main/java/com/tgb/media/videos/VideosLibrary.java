package com.tgb.media.videos;

import android.util.Log;

import com.tgb.media.database.DaoSession;
import com.tgb.media.database.KeywordModel;
import com.tgb.media.database.KeywordModelDao;
import com.tgb.media.database.MovieModel;
import com.tgb.media.database.MovieModelDao;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import tgb.tmdb.TmdbAPI;
import tgb.tmdb.models.Movie;
import tgb.tmdb.models.Search;

public class VideosLibrary {

    //Database
    private DaoSession daoSession;

    //Database models
    private MovieModelDao movieModelDao;
    private KeywordModelDao keywordModelDao;

    //TMDB
    private TmdbAPI tmdbAPI;

    public VideosLibrary(DaoSession daoSession, TmdbAPI tmdbAPI){
        //Database
        this.daoSession = daoSession;

        //Database models
        this.movieModelDao = daoSession.getMovieModelDao();
        this.keywordModelDao = daoSession.getKeywordModelDao();

        //TMDB
        this.tmdbAPI = tmdbAPI;
    }

    private MovieModel searchMovieById(long id){
        List<MovieModel> movie = movieModelDao.queryBuilder()
                .where(MovieModelDao.Properties.Id.eq(id))
                .limit(1)
                .list();

        if(movie.size() == 0)
            return null;

        return movie.get(0);
    }

    private MovieModel searchMovieByKeyword(final String searchedKeyword){
        List<KeywordModel> keyword = keywordModelDao.queryBuilder()
                .where(KeywordModelDao.Properties.Keyword.eq(searchedKeyword))
                .limit(1)
                .list();

        if(keyword.size() == 0)
            return null;

        //Search movie by id
        return searchMovieById(keyword.get(0).movieId);
    }

    public Observable<MovieModel> details(final List<String> moviesName){
        return Observable.create(emitter -> {

            MovieModel movieModel = null;

            for(String movieName : moviesName){
                //Search by keyword
                movieModel = searchMovieByKeyword(movieName);

                if(movieModel != null)
                {
                    emitter.onNext(movieModel);
                    continue;
                }

                Log.i("videosLibrary", "Load " + movieName + " From the API");

                //Load from TMDB
                Response<Search> searchResponse = tmdbAPI.call()
                        .search(movieName, 1)
                        .execute();

                if(!searchResponse.isSuccessful() || searchResponse.body().results.size() == 0){
                    //emitter.onError(new RuntimeException());
                    continue;
                }

                Movie movieGson = searchResponse.body().results.get(0);

                //Load movie overview
                if(!searchResponse.isSuccessful())
                    continue;

                /*Response<MovieOverview> movieDiscoverResponse = tmdbAPI.call()
                        .movie(movieGson.id)
                        .execute();

                MovieOverview movieOverview = movieDiscoverResponse.body();

                Log.i("yoni", movieOverview.toString());*/

                movieModel = searchMovieById(movieGson.id);

                try{
                    //Begin transaction
                    daoSession.getDatabase().beginTransaction();

                    //Insert new keyword
                    KeywordModel keyword = new KeywordModel(movieName, movieGson.id);

                    keywordModelDao.insertOrReplace(keyword);

                    if(movieModel != null){
                        //The movie is already exists in db

                        daoSession.getDatabase().setTransactionSuccessful();
                        emitter.onNext(movieModel);

                        continue;
                    }

                    //The movie does not exists in the database...
                    movieModel = new MovieModel();
                    movieModel.createFromGsonModel(movieGson);

                    movieModelDao.insertOrReplace(movieModel);
                    daoSession.getDatabase().setTransactionSuccessful();

                    emitter.onNext(movieModel);
                }
                catch (Exception e){
                    emitter.onError(e);
                }
                finally{
                    daoSession.getDatabase().endTransaction();
                }

            }

            emitter.onComplete();
        });
    }




}
