package com.tgb.media.module;

import com.tgb.media.database.DaoSession;
import com.tgb.media.videos.VideosLibrary;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tgb.tmdb.TmdbAPI;

@Module
public class MoviesModule {

    @Provides @Singleton VideosLibrary provideMoviesManagement(DaoSession database, TmdbAPI tmdbAPI){
        return new VideosLibrary(database, tmdbAPI);
    }

}
