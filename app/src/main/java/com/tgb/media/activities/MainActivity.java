package com.tgb.media.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.database.MovieModel;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.helper.SpacesItemDecoration;
import com.tgb.media.listener.RecyclerItemClickListener;
import com.tgb.media.videos.VideosLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;

    private ArrayList<MovieModel> movies;
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
        movies = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), movies);

        //Recycler view grid
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.gallery_columns));

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        MovieModel movieMdel = mAdapter.getItem(position);

                        Intent detailsActivityIntent = new Intent(
                                getBaseContext(),
                                DetailsActivity.class);

                        detailsActivityIntent.putExtra(DetailsActivity.MOVIE, movieMdel);

                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.thumbnail), getString(R.string.gallery_transition));

                        startActivity(detailsActivityIntent, options.toBundle());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        List<String> requestedMovies = new ArrayList<>();

        requestedMovies.add("Moonlight");
        requestedMovies.add("Zootopia");
        requestedMovies.add("Minions");
        requestedMovies.add("Hell or High Water");
        requestedMovies.add("Manchester by the Sea");
        requestedMovies.add("Hacksaw Ridge");
        requestedMovies.add("Arrival");
        requestedMovies.add("La La Land");
        requestedMovies.add("The Magnificent Seven");
        requestedMovies.add("Sausage Party");
        requestedMovies.add("The Jungle Book");
        requestedMovies.add("Love & Friendship");
        requestedMovies.add("Ted 2");
        requestedMovies.add("Finding Dory");
        requestedMovies.add("Kubo and the Two Strings");
        requestedMovies.add("Hunt for the Wilderpeople");
        requestedMovies.add("Deadpool");
        requestedMovies.add("American Sniper");
        requestedMovies.add("Moana");
        requestedMovies.add("Captain America: Civil War");
        requestedMovies.add("Sing Street");
        requestedMovies.add("Tower");
        requestedMovies.add("The Nice Guys");
        requestedMovies.add("Ted");
        requestedMovies.add("Eye In The Sky");
        requestedMovies.add("Embrace Of The Serpent");
        requestedMovies.add("The Witch");
        requestedMovies.add("Doctor Strange");
        requestedMovies.add("Kung Fu Panda 3");
        requestedMovies.add("The Edge of Seventeen");
        requestedMovies.add("Fences");
        requestedMovies.add("10 Cloverfield Lane");
        requestedMovies.add("Jackie");


        final AtomicInteger counter = new AtomicInteger();

        videosLibrary.details(requestedMovies)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(movieModel -> {
                    movies.add(movieModel);
                    mAdapter.notifyItemChanged(counter.getAndIncrement());
                })
                .doOnError(throwable -> {
                    Log.e("tmdbError", "Oh no");
                })
                .doOnComplete(() -> {
                    mAdapter.notifyDataSetChanged();
                    loadingDialog.hide();
                })
                .subscribe();
    }

}
