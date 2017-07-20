package com.tgb.media.activities;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.WindowManager;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.helper.MovieObservableResult;
import com.tgb.media.helper.OverlayMessageView;
import com.tgb.media.helper.SpacesItemDecoration;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.server.models.Response;
import com.tgb.media.videos.VideosLibrary;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //Elements
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.overlay_message) OverlayMessageView overlayMessageView;

    //Params
    private AppBarLayout.LayoutParams appbarParams;
    private Point screenDimensions;

    //Adapters
    private GalleryAdapter mAdapter;

    //TGB Api
    @Inject TgbAPI tgbAPI;
    private Throwable lastServerError;
    private MovieObservableResult[] videos;

    //Tmdb API
    @Inject VideosLibrary videosLibrary;

    //Finals
    private static final int REQUEST_TIMEOUT = 5; //Seconds
    private static final String VIDEOS_KEY = "videos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            videos = (MovieObservableResult[]) savedInstanceState.getParcelableArray(VIDEOS_KEY);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butter knife
        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(toolbar);

        //Gallery adapter
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        screenDimensions = new Point();
        display.getSize(screenDimensions);

        mAdapter = new GalleryAdapter(
                this,
                screenDimensions,
                getResources().getConfiguration().orientation
        );

        //Recycler view grid
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.gallery_columns));

        appbarParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            videos = null;
            loadingDialog.show(view -> refresh());
        });

        refresh();
    }

    private void refresh(){
        //Disable refresh
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);

        //GUI Actions
        overlayMessageView.hide();
        appbarParams.setScrollFlags(0);

        //Restart values
        lastServerError = null;
        final AtomicInteger currentPosition = new AtomicInteger();

        //Adapters actions
        mAdapter.clear();

        Observable<MovieObservableResult> observable = (videos == null)
                ? loadFromServer(currentPosition)
                : loadLocalList();

        observable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(item -> {
                videos[item.position] = item;

                if(currentPosition.get() == item.position){
                    mAdapter.addItem(videos[currentPosition.get()]);

                    while(currentPosition.incrementAndGet() < videos.length && videos[currentPosition.get()] != null)
                        mAdapter.addItem(videos[currentPosition.get()]);
                }
            })
            .doOnComplete(this::onCompleted)
            .subscribe();

//        tgbAPI.call()
//            .moviesList()
//            .subscribeOn(Schedulers.newThread())
//            .timeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
//            .doOnError(t -> lastServerError = t)
//            .onErrorReturn(throwable -> new Response<>())
//            .flatMap(response -> {
//                currentPosition.set(response.results == null ? 0 : response.results.length);
//                videos = new MovieObservableResult[currentPosition.get()];
//
//                return Observable.range(0, currentPosition.getAndSet(0))
//                        .flatMap(position -> Observable.just(position)
//                            .subscribeOn(Schedulers.computation())
//                            .map(i -> videosLibrary.videoDetails(i, response.results[i])));
//            })
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext(item -> {
//                videos[item.position] = item;
//
//                if(currentPosition.get() == item.position){
//                    mAdapter.addItem(videos[currentPosition.get()]);
//
//                    while(currentPosition.incrementAndGet() < videos.length && videos[currentPosition.get()] != null)
//                        mAdapter.addItem(videos[currentPosition.get()]);
//                }
//            })
//            .doOnComplete(this::onCompleted)
//            .subscribe();
    }

    private Observable<MovieObservableResult> loadFromServer(AtomicInteger currentPosition){
        return tgbAPI.call()
            .moviesList()
            .subscribeOn(Schedulers.newThread())
            .timeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .doOnError(t -> lastServerError = t)
            .onErrorReturn(throwable -> new Response<>())
            .flatMap(response -> {
                currentPosition.set(response.results == null ? 0 : response.results.length);
                videos = new MovieObservableResult[currentPosition.get()];

                return Observable.range(0, currentPosition.getAndSet(0))
                        .flatMap(position -> Observable.just(position)
                                .subscribeOn(Schedulers.computation())
                                .map(i -> videosLibrary.videoDetails(i, response.results[i])));
            });
    }

    private Observable<MovieObservableResult> loadLocalList(){
        return Observable.range(0, videos.length)
            .flatMap(position ->
                Observable.just(position)
                    .subscribeOn(Schedulers.computation())
                    .map(i -> videos[i])
            );
    }

    private void onCompleted(){
        loadingDialog.hide(null);

        if(lastServerError == null){
            //Successfully refresh
            appbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        else
        {
            overlayMessageView.setMessage(getString(R.string.no_online_servers));
            overlayMessageView.show();
        }

        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(VIDEOS_KEY, videos);
    }
}
