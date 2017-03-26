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
import com.tgb.media.database.MovieOverviewModel;

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


    //Finals
    public static final String MOVIE = "movie";
    private static final String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if(savedInstanceState != null
                && savedInstanceState.getBoolean(CONFIGURATION_CHANGED, false)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //Bug fix, remove poster transition if configuration changed...
            poster.setTransitionName(null);
        }

        MovieOverviewModel movie = getIntent().getParcelableExtra(MOVIE);
        //List<GenreModel> genres = movie.getGenres();
        //Log.i("yoni", "genres: " + genres);

        title.setText(movie.title);
        //subtitle.setText(movie.relaseDate);
        overview.setText(movie.overview);

        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w640/" + movie.posterPath)
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w1300_and_h730_bestv2/" + movie.backdropPath)
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(theme);

        playButton.setOnClickListener(v -> {
            startActivity(buildIntent(this));
        });
    }

    public Intent buildIntent(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(PlayerActivity.URI_LIST_EXTRA, new String[]{"http://www.html5videoplayer.net/videos/toystory.mp4"});

        /*if (drmSchemeUuid != null) {
            intent.putExtra(PlayerActivity.DRM_SCHEME_UUID_EXTRA, drmSchemeUuid.toString());
            intent.putExtra(PlayerActivity.DRM_LICENSE_URL, drmLicenseUrl);
            intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES, drmKeyRequestProperties);
        }*/

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
