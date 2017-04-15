package com.tgb.media.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.helper.OverlayMessageView;
import com.tgb.media.helper.SpacesItemDecoration;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.server.models.Response;
import com.tgb.media.videos.VideosLibrary;

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

    //Adapters
    private GalleryAdapter mAdapter;

    //TGB Api
    private TgbAPI tgbAPI;
    private Throwable lastServerError;

    //Tmdb API
    @Inject VideosLibrary videosLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butter knife
        ButterKnife.bind(this);

        //TGB Api
        tgbAPI = new TgbAPI("http://192.168.1.10:8081/", "");

        //Toolbar
        setSupportActionBar(toolbar);

        //Gallery adapter
        mAdapter = new GalleryAdapter(this);

        //Recycler view grid
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.gallery_columns));

        appbarParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(false);
            loadingDialog.show(view -> refresh());
        });

        refresh();
    }

    private void refresh(){
        lastServerError = null;

        overlayMessageView.hide();
        appbarParams.setScrollFlags(0);

        mAdapter.clear();

        tgbAPI.call().moviesList()
                .subscribeOn(Schedulers.newThread())
                .doOnError(t -> lastServerError = t)
                .onErrorReturn(throwable -> new Response<>())
                .flatMap(response -> response.results == null
                        ? Observable.empty()
                        : Observable.fromArray(response.results))
                .flatMap(video -> videosLibrary.movieDetails(mAdapter.getItemCount(), video.title))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mAdapter::addItem)
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(() -> {
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
                })
                .subscribe();
    }

}
