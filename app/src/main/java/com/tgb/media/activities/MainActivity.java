package com.tgb.media.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.GalleryAdapter;
import com.tgb.media.database.MovieModel;
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
    @BindView(R.id.overlay_loading) View overlayLoading;

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
                        startActivity(new Intent(getBaseContext(), DetailsActivity.class));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        List<String> requestedMovies = new ArrayList<>();

        requestedMovies.add("Minions");
        requestedMovies.add("Hacksaw Ridge");
        requestedMovies.add("Now you see me 2");
        requestedMovies.add("The Magnificent Seven");
        requestedMovies.add("Doctor strange");
        requestedMovies.add("Ted");
        requestedMovies.add("Ted 2");
        requestedMovies.add("Sausage Party");
        requestedMovies.add("American Sniper");
        requestedMovies.add("The Godfather");
        requestedMovies.add("The Shawshank Redemption");
        requestedMovies.add("Schindler's List");
        requestedMovies.add("Raging Bull");
        requestedMovies.add("Casablanca");
        requestedMovies.add("Citizen Kane");
        requestedMovies.add("Gone with the Wind");
        requestedMovies.add("The Wizard of Oz");
        requestedMovies.add("One Flew Over the Cuckoo's Nest");
        requestedMovies.add("Lawrence of Arabia");
        requestedMovies.add("Vertigo");
        requestedMovies.add("Psycho");
        requestedMovies.add("The Godfather: Part II");
        requestedMovies.add("On the Waterfront");
        requestedMovies.add("Forrest Gump");
        requestedMovies.add("The Sound of Music");
        requestedMovies.add("12 Angry Men");
        requestedMovies.add("West Side Story");
        requestedMovies.add("Star Wars");
        requestedMovies.add("2001: A Space Odyssey");
        requestedMovies.add("E.T. the Extra-Terrestrial");
        requestedMovies.add("The Silence of the Lambs");
        requestedMovies.add("Chinatown");
        requestedMovies.add("The Bridge on the River Kwai");
        requestedMovies.add("Singin' in the Rain");
        requestedMovies.add("It's a Wonderful Life");
        requestedMovies.add("Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb");
        requestedMovies.add("Some Like It Hot");
        requestedMovies.add("Ben-Hur");
        requestedMovies.add("Apocalypse Now");
        requestedMovies.add("Amadeus");
        requestedMovies.add("The Lord of the Rings: The Return of the King");
        requestedMovies.add("Gladiator");
        requestedMovies.add("Titanic");
        requestedMovies.add("From Here to Eternity");
        requestedMovies.add("Saving Private Ryan");
        requestedMovies.add("Unforgiven");
        requestedMovies.add("Raiders of the Lost Ark");
        requestedMovies.add("Rocky");
        requestedMovies.add("A Streetcar Named Desire");
        requestedMovies.add("The Philadelphia Story");
        requestedMovies.add("To Kill a Mockingbird");
        requestedMovies.add("An American in Paris");
        requestedMovies.add("The Best Years of Our Lives");
        requestedMovies.add("My Fair Lady");
        requestedMovies.add("A Clockwork Orange");
        requestedMovies.add("Doctor Zhivago");
        requestedMovies.add("The Searchers");
        requestedMovies.add("Jaws");
        requestedMovies.add("Patton");
        requestedMovies.add("Butch Cassidy and the Sundance Kid");
        requestedMovies.add("The Treasure of the Sierra Madre");
        requestedMovies.add("Il buono, il brutto, il cattivo");
        requestedMovies.add("The Apartment");
        requestedMovies.add("Platoon");
        requestedMovies.add("High Noon");
        requestedMovies.add("Braveheart");
        requestedMovies.add("Dances with Wolves");
        requestedMovies.add("Jurassic Park");
        requestedMovies.add("The Exorcist");
        requestedMovies.add("The Pianist");
        requestedMovies.add("Goodfellas");
        requestedMovies.add("The Deer Hunter");
        requestedMovies.add("All Quiet on the Western Front");
        requestedMovies.add("Bonnie and Clyde");
        requestedMovies.add("The French Connection");
        requestedMovies.add("City Lights");
        requestedMovies.add("It Happened One Night");
        requestedMovies.add("A Place in the Sun");
        requestedMovies.add("Midnight Cowboy");
        requestedMovies.add("Mr. Smith Goes to Washington");
        requestedMovies.add("Rain Man");
        requestedMovies.add("Annie Hall");
        requestedMovies.add("Fargo");
        requestedMovies.add("Giant");
        requestedMovies.add("Shane");
        requestedMovies.add("The Grapes of Wrath");
        requestedMovies.add("The Green Mile");
        requestedMovies.add("Close Encounters of the Third Kind");
        requestedMovies.add("Nashville");
        requestedMovies.add("Network");
        requestedMovies.add("The Graduate");
        requestedMovies.add("American Graffiti");
        requestedMovies.add("Pulp Fiction");
        requestedMovies.add("Terms of Endearment");
        requestedMovies.add("Good Will Hunting");
        requestedMovies.add("The African Queen");
        requestedMovies.add("Stagecoach");
        requestedMovies.add("Mutiny on the Bounty");
        requestedMovies.add("The Great Dictator");
        requestedMovies.add("Double Indemnity");
        requestedMovies.add("The Maltese Falcon");
        requestedMovies.add("Wuthering Heights");
        requestedMovies.add("Taxi Driver");
        requestedMovies.add("Rear Window");
        requestedMovies.add("The Third Man");
        requestedMovies.add("Rebel Without a Cause");
        requestedMovies.add("North by Northwest");

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

                    //Loading layout - fade out
                    Animation fadeOut = new AlphaAnimation(1, 0);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setStartOffset(500); // Start fading out after 500 milli seconds
                    fadeOut.setDuration(1200); // Fadeout duration should be 1000 milli seconds
                    fadeOut.setFillAfter(true);

                    overlayLoading.startAnimation(fadeOut);
                })
                .subscribe();
    }

}
