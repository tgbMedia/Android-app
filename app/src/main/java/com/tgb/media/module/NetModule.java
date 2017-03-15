package com.tgb.media.module;

import com.tgb.media.R;
import com.tgb.media.TgbApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tgb.tmdb.TmdbAPI;

@Module
public class NetModule {

    @Provides
    @Singleton
    TmdbAPI providesTmdbAPI(TgbApp application) {
        return new TmdbAPI(
                application.getBaseContext().getString(R.string.tmdb_api_key),
                application.getDeviceLanguage()
        );
    }

}
