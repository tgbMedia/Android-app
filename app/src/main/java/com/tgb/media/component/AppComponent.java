package com.tgb.media.component;

import com.tgb.media.activities.DiscoverActivity;
import com.tgb.media.activities.MainActivity;
import com.tgb.media.activities.OverviewActivity;
import com.tgb.media.activities.VideoPlayerActivity;
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
    void inject(DiscoverActivity activity);
    void inject(OverviewActivity activity);
    void inject(VideoPlayerActivity activity);

    //AppModule appModule();
    //CommunicationModule communication();
}
