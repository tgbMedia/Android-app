package com.tgb.media;

import android.util.Log;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.tgb.media.component.AppComponent;
import com.tgb.media.component.DaggerAppComponent;
import com.tgb.media.module.AppModule;
import com.tgb.media.module.NetModule;

import java.util.Locale;

public class TgbApp extends android.app.Application {

    //Dagger components
    private AppComponent appComponent;

    //Instance
    private static TgbApp instance;

    //User identifier
    private String deviceIdentifier;

    //TGB Server
    private String tgbServerAddress = "http://192.168.1.13:8081/";

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        deviceIdentifier = createUserAgent();

        Log.i("yoni", "Identifier: " + deviceIdentifier);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
        //appComponent = DaggerAppComponent.builder()
        //        .build();
    }

    private String createUserAgent(){
        String deviceId = android.provider.Settings.System.getString(super.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        return Util.getUserAgent(
                this,
                getString(R.string.app_name) + ";" + deviceId
        );
    }

    public static TgbApp getInstance(){
        return instance;
    }

    public String getTgbServerAddress(){
        return tgbServerAddress;
    }

    public String getDeviceIdentifier(){
        return deviceIdentifier;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(deviceIdentifier, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

    public String getDeviceLanguage(){
        return Locale.getDefault().getISO3Language();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
