package com.tgb.media.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.database.CastRelationModel;
import com.tgb.media.database.CrewRelationModel;
import com.tgb.media.database.GenreModel;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.ExpandableTextView;
import com.tgb.media.videos.VideosLibrary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    //Views
    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_collapsing) CollapsingToolbarLayout mainCollapsing;
    @BindView(R.id.theme_poster) ImageView theme;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.runtime) TextView runtime;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.genes) TextView genres;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.director) TextView director;
    @BindView(R.id.overview) ExpandableTextView overview;
    @BindView(R.id.read_more) TextView readMore;
    @BindView(R.id.like_button) ShineButton likeButton;
    @BindView(R.id.theme_play_container) View themePlayButtonContainer;
    @BindView(R.id.play_button) FloatingActionButton playButton;

    //Tmdb API
    @Inject VideosLibrary videosLibrary;

    //Finals
    public static final String MOVIE_ACTION = "com.tgb.media.details.movie";
    public static final String MOVIE_ID = "movieId";

    private static final String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";

    //Youtube
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

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
        //First all set poster image for the shared element
        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        //Load movie backdrop
        Glide.with(getBaseContext()).load(
                "https://image.tmdb.org/t/p/w1300_and_h730_bestv2/" + movie.getBackdropPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(theme);

        //Cast
        /*List<CastRelationModel> cast = movie.getCast();

        for(CastRelationModel castRelation : cast){
            Log.i("yoni", castRelation.getCharacter() + " is " + castRelation.getPerson().getName());
        }*/

        //Crew
        /*List<CrewRelationModel> crew = movie.getCrew();

        for(CrewRelationModel crewRelation : crew){
            Log.i("yoni", crewRelation.getPerson().getName() + " - " + crewRelation.getJob());
        }*/

        //Runtime
        runtime.setText(getString(R.string.runtime, (int)(movie.getRuntime() / 60),
                (int)(movie.getRuntime() % 60)));

        //Rating
        rating.setText(String.format(Locale.ROOT, "%.1f", movie.getVoteAverage()));

        //Like button
        likeButton.setChecked(movie.getLike());

        likeButton.setOnClickListener(v -> {
            movie.setLike(likeButton.isChecked());
            movie.update();
        });

        //Genres
        int totalGenres = 0;

        for(GenreModel genreModel : movie.getGenres()){
            if(genres.length() > 0)
                genres.append(", ");

            genres.append(genreModel.getName());

            if(++totalGenres > 3)
                break;
        }

        //Release date
        Calendar movieReleaseDate = Calendar.getInstance();
        movieReleaseDate.setTimeInMillis(movie.getReleaseDate());

        subtitle.setText(movieReleaseDate.get(Calendar.YEAR) + "");
        releaseDate.setText(getFormattedDate(movieReleaseDate));

        //Director/s
        List<CrewRelationModel> movieDirectors =
                videosLibrary.searchPersonByJob(movie.getId(), VideosLibrary.DIRECTOR);

        for(CrewRelationModel crewRelationModel: movieDirectors){
            if(director.length() > 0)
                director.append(", ");

            director.append(crewRelationModel.getPerson().getName());
        }

        //Overview
        readMore.setOnClickListener(v -> {
            overview.toggle();

            readMore.setText(overview.isExpanded()
                    ? getString(R.string.read_more)
                    : getString(R.string.collapse));
        });



        //Set movie details
        title.setText(movie.getTitle());

        overview.setText(movie.getOverview());
        overview.setInterpolator(new OvershootInterpolator());

        if(!TextUtils.isEmpty(movie.getYoutubeTrailer())){
            themePlayButtonContainer.setVisibility(View.VISIBLE);

            themePlayButtonContainer.setOnClickListener(view -> {

                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                        this, getString(R.string.google_api_key),
                        movie.getYoutubeTrailer(), 0, true, false);

                if (canResolveIntent(intent)) {
                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                } else {
                    // Could not resolve the intent - must need to install or update the YouTube API service.
                    YouTubeInitializationResult.SERVICE_MISSING
                            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
                }
            });
        }

        //Set play button event(Play movie)
        playButton.setOnClickListener(v -> {
            startActivity(buildVideoPlayerIntent(this, movie.getServerTitle()));
        });
    }

    private String getFormattedDate(Calendar c) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    private Intent buildVideoPlayerIntent(Context context, String videoTitle) {

        Log.i("yoni", "Stream url; " + "http://192.168.1.10:8081/video/" + videoTitle);

        Intent intent = new Intent(context, PlayerActivity.class);
        intent.setAction(PlayerActivity.ACTION_VIEW_LIST);
        intent.putExtra(PlayerActivity.URI_LIST_EXTRA, new String[]{"http://192.168.1.10:8081/video/" + videoTitle});

        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
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
