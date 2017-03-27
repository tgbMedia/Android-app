package com.tgb.media.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.database.GenreModel;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.videos.VideosLibrary;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    //Views
    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.theme_poster) ImageView theme;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.play_button) FloatingActionButton playButton;

    //Tmdb API
    @Inject VideosLibrary videosLibrary;

    //Finals
    public static final String MOVIE_ACTION = "com.tgb.media.details.movie";
    public static final String MOVIE_ID = "movieId";

    private static final String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butterknife
        ButterKnife.bind(this);

        if(savedInstanceState != null
                && savedInstanceState.getBoolean(CONFIGURATION_CHANGED, false)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //Bug fix, remove poster transition if configuration changed...
            poster.setTransitionName(null);
        }

        initialize();
    }

    private void initialize(){
        Intent intent = getIntent();

        if(intent.getAction() == MOVIE_ACTION){
            MovieOverviewModel movie = videosLibrary.searchMovieById(
                    intent.getLongExtra(MOVIE_ID, -1)
            );

            if(movie != null)
                movieDetails(movie);
        }
    }

    private void movieDetails(MovieOverviewModel movie){
        StringBuilder genres = new StringBuilder();

        List<GenreModel> movieGenres = movie.getGenres();

        movieGenres.forEach(genreModel -> {
            if(genres.length() > 0)
                genres.append(", ");

            genres.append(genreModel.getName());
        });

        title.setText(movie.getTitle());
        subtitle.setText(genres.toString());
        overview.setText(movie.getOverview());

        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w1300_and_h730_bestv2/" + movie.getBackdropPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(theme);

        playButton.setOnClickListener(v -> {
            startActivity(buildVideoPlayerIntent(this));
        });
    }

    private Intent buildVideoPlayerIntent(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.setAction(PlayerActivity.ACTION_VIEW_LIST);
        intent.putExtra(PlayerActivity.URI_LIST_EXTRA, new String[]{"http://www.html5videoplayer.net/videos/toystory.mp4"});

        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Bug fix, fab blink after back button is pressed - WTF?
        coordinatorLayout.removeView(playButton);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(CONFIGURATION_CHANGED, true);
    }


}
