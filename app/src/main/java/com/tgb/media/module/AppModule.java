package com.tgb.media.module;

import android.database.sqlite.SQLiteDatabase;

import com.tgb.media.TgbApp;
import com.tgb.media.database.DaoMaster;
import com.tgb.media.database.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private TgbApp app;

    public AppModule(TgbApp app){
        this.app = app;
    }

    @Provides @Singleton TgbApp providesApplication(){
        return app;
    }

    @Provides @Singleton DaoSession provideDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app, "movies-db-" + app.getDeviceLanguage(), null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        return daoMaster.newSession();
    }
}
