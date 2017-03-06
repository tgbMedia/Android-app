package com.tgb.media.component;

import com.tgb.media.MainActivity;
import com.tgb.media.module.AppModule;
import com.tgb.media.module.MoviesModule;
import com.tgb.media.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={
        AppModule.class,
        NetModule.class,
        MoviesModule.class
})

public interface AppComponent {
    void inject(MainActivity activity);

    //AppModule appModule();
    //CommunicationModule communication();
}
