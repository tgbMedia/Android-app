package com.tgb.media.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaControlIntent;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.RemotePlaybackClient;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.server.models.Response;
import com.tgb.media.videos.VideosLibrary;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

public class VideoPlayerActivity extends AppCompatActivity
        implements AdaptiveMediaSourceEventListener, ExoPlayer.EventListener{

    //Tags
    private static final String TAG = VideoPlayerActivity.class.getName();

    //TGB API
    @Inject VideosLibrary videosLibrary;
    @Inject TgbAPI tgbAPI;

    //Elements
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;

    //ExoPlayer elements
    private Toolbar toolbar;
    private ImageView backdrop;
    private ImageView playerPoster;

    //ExoPlayer
    private SimpleExoPlayer player;
    private Handler mainHandler;
    private DataSource.Factory dataSourceFactory;

    //Media route
    private MediaRouteSelector mSelector;
    private MediaRouter mMediaRouter;

    // Variables to hold the currently selected route and its playback client
    private MediaRouter.RouteInfo mRoute;
    private RemotePlaybackClient mRemotePlaybackClient;

    // Define the Callback object and its methods, save the object in a class variable
    private final MediaRouter.Callback mMediaRouterCallback =
            new MediaRouter.Callback() {

                @Override
                public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
                    Log.d(TAG, "onRouteSelected: route=" + route);

                    if (route.supportsControlCategory(
                            MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)){
                        // Stop local playback (if necessary)
                        // ...

                        // Save the new route
                        mRoute = route;

                        // Attach a new playback client
                        mRemotePlaybackClient = new RemotePlaybackClient(getBaseContext(), mRoute);

                        // Start remote playback (if necessary)
                        // ...
                    }
                }

                @Override
                public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route, int reason) {
                    Log.d(TAG, "onRouteUnselected: route=" + route);

                    if (route.supportsControlCategory(
                            MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)){

                        // Changed route: tear down previous client
                        if (mRoute != null && mRemotePlaybackClient != null) {
                            mRemotePlaybackClient.release();
                            mRemotePlaybackClient = null;
                        }

                        // Save the new route
                        mRoute = route;

                        if (reason != MediaRouter.UNSELECT_REASON_ROUTE_CHANGED) {
                            // Resume local playback  (if necessary)
                            // ...
                        }
                    }
                }
            };


    //Finals
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    public static final String MOVIE_ACTION = "com.tgb.media.details.movie";
    public static final String MOVIE_ID = "movieId";
    public static final String MEDIA_SOURCE_URL = "media_source_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butterknife
        ButterKnife.bind(this);

        // Get the media router service.
        mMediaRouter = MediaRouter.getInstance(this);

        //Elements
        toolbar = playerView.findViewById(R.id.toolbar);
        backdrop = playerView.findViewById(R.id.backdrop);
        playerPoster = playerView.findViewById(R.id.poster);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(toolbar);

        //ExoPlayer
        mainHandler = new Handler();
        dataSourceFactory = ((TgbApp) getApplication()).buildHttpDataSourceFactory(BANDWIDTH_METER);

        // Create a route selector for the type of routes your app supports.
        mSelector = new MediaRouteSelector.Builder()
                // These are the framework-supported intents
                .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
                .build();

        //Create new player instance
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //Associate player to view
        playerView.setPlayer(player);

        //Player settings
        player.addListener(this);
        player.setPlayWhenReady(true);

        playerView.showController();
        playerView.setControllerHideOnTouch(false);


        initializeUI();
    }

    private void initializeUI(){
        Intent intent = getIntent();

        if(intent.getAction() == MOVIE_ACTION){
            MovieOverviewModel movie = videosLibrary.searchMovieById(
                    intent.getLongExtra(MOVIE_ID, -1)
            );

            if(movie != null)
                updateMovieDetails(movie);
        }
    }

    private void initializePlayer(){
        //Media source
        Intent intent = getIntent();

        if(intent == null){
            finish();
            return;
        }

        final String sourceUrl = intent.getStringExtra(MEDIA_SOURCE_URL);

        if(TextUtils.isEmpty(sourceUrl))
        {
            finish();
            return;
        }

        //Create media ExoPlayer Media source
        Uri uri = Uri.parse(sourceUrl);
        MediaSource mediaSource = buildMediaSource(uri);

        //Subtitles
//        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
//                null, Format.NO_VALUE, Format.NO_VALUE, "iw", null);
//
//        MediaSource subtitleSource =
//                new SingleSampleMediaSource(Uri.parse(),
//                dataSourceFactory, textFormat, C.TIME_UNSET);
//
//        MergingMediaSource mergedSource = new MergingMediaSource(mediaSource, subtitleSource);

        //Player prepare
        player.prepare(mediaSource, true, false);
//        player.prepare(mergedSource, true, false);
    }

    private void updateMovieDetails(MovieOverviewModel movie){
        //Update player poster
        Glide.with(getBaseContext())
                .load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(playerPoster);

        //Movie backdrop poster
        Glide.with(getBaseContext()).load(
                "https://image.tmdb.org/t/p/w1300_and_h730_bestv2/" + movie.getBackdropPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(backdrop);

        //Player titles
        getSupportActionBar().setTitle(movie.getTitle());
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new HlsMediaSource(
                uri,
                dataSourceFactory,
                mainHandler,
                this
        );
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
//            playWhenReady = player.getPlayWhenReady();
            player.removeListener(this);
            player.release();
            player = null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu and configure the media router action provider.
        getMenuInflater().inflate(R.menu.media_route_menu_item, menu);

        // Attach the MediaRouteSelector to the menu item
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider)MenuItemCompat.getActionProvider(
                        mediaRouteMenuItem);
        // Attach the MediaRouteSelector that you built in onCreate()
        mediaRouteActionProvider.setRouteSelector(mSelector);

        // Return true to show the menu.
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        mMediaRouter.addCallback(mSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);

        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mMediaRouter.removeCallback(mMediaRouterCallback);

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

        tgbAPI.call().killLastProcess().enqueue(new Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {

            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {

            }
        });
    }

    // AdaptiveMediaSourceEventListener
    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat,
                              int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs,
                              long mediaEndTimeMs, long elapsedRealtimeMs) {
        Timber.tag(TAG).i("onLoadStarted");
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat,
                            int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs,
                            long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded,
                            IOException error, boolean wasCanceled) {

        Timber.tag(TAG).i("loadError %s", error.getMessage());
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat,
                               int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs,
                               long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        Timber.tag(TAG).i("onLoadCanceled");
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat,
                                int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs,
                                long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        Timber.tag(TAG).i("onLoadCompleted");
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
        Timber.tag(TAG).i("onUpstreamDiscarded");
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason,
                                          Object trackSelectionData, long mediaTimeMs) {
        Timber.tag(TAG).i("onDownstreamFormatChanged");
    }

    //ExoPlayer.EventListener
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Timber.tag(TAG).i("onTimeLineChanged");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            case ExoPlayer.STATE_BUFFERING:
                /*loadingDialog.show(v -> {
                    playerView.setUseController(false);
                    playerView.hideController();
                });*/
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;
            case ExoPlayer.STATE_READY:
                loadingDialog.hide(v -> {
                    playerView.setControllerHideOnTouch(true);

                    backdrop.setVisibility(View.GONE);
//                    playerView.setUseController(true);
//                    playerView.showController();
                });
                stateString = "ExoPlayer.STATE_READY     -";
                break;
            case ExoPlayer.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        Timber.tag(TAG).d("changed state to " + stateString
                + " playWhenReady: " + playWhenReady);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
