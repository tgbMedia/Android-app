package com.tgb.media.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.helper.SpacesItemDecoration;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.videos.VideosLibrary;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;

    private GalleryAdapter mAdapter;

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

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Gallery adapter
        mAdapter = new GalleryAdapter(this);

        //Recycler view grid
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.gallery_columns));

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(mAdapter);

        TgbAPI tgbAPI = new TgbAPI("http://192.168.1.10:8081/", "");

        tgbAPI.call().moviesList()
                .subscribeOn(Schedulers.newThread())
                .flatMap(response -> Observable.fromArray(response.results))
                .flatMap(video -> videosLibrary.movieDetails(mAdapter.getItemCount(), video.title))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mAdapter::addItem)
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(() -> loadingDialog.hide(null))
                .subscribe();
    }

}
