package com.tgb.media;

import com.tgb.media.component.AppComponent;
import com.tgb.media.component.DaggerAppComponent;
import com.tgb.media.module.AppModule;
import com.tgb.media.module.NetModule;

import java.util.Locale;

public class TgbApp extends android.app.Application {

    //Dagger components
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
        //appComponent = DaggerAppComponent.builder()
        //        .build();
    }

    public String getDeviceLanguage(){
        return Locale.getDefault().getISO3Language();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
