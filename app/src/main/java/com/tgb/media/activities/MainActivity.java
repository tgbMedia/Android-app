package com.tgb.media.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.LoadingDialog;
import com.tgb.media.helper.SpacesItemDecoration;
import com.tgb.media.listener.RecyclerItemClickListener;
import com.tgb.media.videos.VideosLibrary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loading_dialog) LoadingDialog loadingDialog;

    private ArrayList<MovieOverviewModel> movies;
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
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        MovieOverviewModel movie = mAdapter.getItem(position);

                        Intent detailsActivityIntent = new Intent(getBaseContext(),
                                DetailsActivity.class);

                        detailsActivityIntent.setAction(DetailsActivity.MOVIE_ACTION);
                        detailsActivityIntent.putExtra(DetailsActivity.MOVIE_ID, movie.getId());

                        ActivityOptionsCompat options = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(
                                        MainActivity.this,
                                        view.findViewById(R.id.thumbnail),
                                        getString(R.string.gallery_transition)
                                );

                        startActivity(detailsActivityIntent, options.toBundle());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        List<String> requestedMovies = new ArrayList<>();

        requestedMovies.add("Logan");
        requestedMovies.add("Kong");
        requestedMovies.add("Beauty and the Beast");
        requestedMovies.add("Life");
        requestedMovies.add("John wick 2");
        requestedMovies.add("Rings");
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

        int index = 0;

        for(String movieName : requestedMovies)
        {
            videosLibrary.movieDetails(index++, movieName)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(result -> {
                        movies.add(result.movie);
                        mAdapter.notifyItemChanged(result.position);
                    })
                    .doOnError(throwable -> {
                        throwable.printStackTrace();
                    })
                    .doOnComplete(() -> loadingDialog.hide())
                    .subscribe();
        }

    }

}
