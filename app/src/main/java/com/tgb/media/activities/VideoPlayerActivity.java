package com.tgb.media.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

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
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.server.models.Response;

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
    @Inject TgbAPI tgbAPI;

    //Elements
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;

    //ExoPlayer
    private SimpleExoPlayer player;
    private Handler mainHandler;

    //Finals
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    public static final String MEDIA_SOURCE_URL = "media_source_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butterknife
        ButterKnife.bind(this);

        //ExoPlayer
        mainHandler = new Handler();

        //Create new player instance
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //Associate player to view
        playerView.setPlayer(player);

        //Player settings
        player.addListener(this);
        player.setPlayWhenReady(true);
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

        //Player prepare
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {


        return new HlsMediaSource(
                uri,
                ((TgbApp) getApplication()).buildHttpDataSourceFactory(BANDWIDTH_METER),
                mainHandler,
                this
        );

//        return new ExtractorMediaSource(uri,
//                new DefaultHttpDataSourceFactory("ua"),
//                new DefaultExtractorsFactory(), null, null);
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
    public void onStart() {
        super.onStart();
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
                loadingDialog.hide(v -> playerView.setUseController(true));
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
