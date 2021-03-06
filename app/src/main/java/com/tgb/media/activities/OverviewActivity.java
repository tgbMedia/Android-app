package com.tgb.media.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.tgb.media.R;
import com.tgb.media.TgbApp;
import com.tgb.media.adapter.CastAdapter;
import com.tgb.media.database.CastRelationModel;
import com.tgb.media.database.CrewRelationModel;
import com.tgb.media.database.GenreModel;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.database.PersonModel;
import com.tgb.media.helper.ExpandableTextView;
import com.tgb.media.server.TgbAPI;
import com.tgb.media.videos.VideosLibrary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class OverviewActivity extends AppCompatActivity {

    //Tags
    private static final String TAG = OverviewActivity.class.getName();

    //Tmdb API
    @Inject VideosLibrary videosLibrary;
    @Inject TgbAPI tgbAPI;

    //Elements
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.theme_poster) ImageView theme;
//    @BindView(R.id.trailer_button) View themePlayButtonContainer;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;
    @BindView(R.id.runtime) TextView runtime;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.like_button) ShineButton likeButton;
    @BindView(R.id.genes) TextView genres;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.director) TextView director;
    @BindView(R.id.overview_content) ExpandableTextView overviewContent;
    @BindView(R.id.read_more) TextView readMore;
    @BindView(R.id.cast_list) RecyclerView castList;
    @BindView(R.id.play_button) FloatingActionButton playButton;
    @BindView(R.id.container) ConstraintLayout container;
    @BindView(R.id.cast_container) RelativeLayout castContainer;

    //Properties
    private boolean alreadyPushedDown = false;

    //Finals
    public static final String MOVIE_ACTION = "com.tgb.media.details.movie";
    public static final String MOVIE_ID = "movieId";

    //Youtube
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.overview_activity);

        //Dagger
        ((TgbApp) getApplication()).getAppComponent().inject(this);

        //Butterknife
        ButterKnife.bind(this);

        //Toolbar
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        //Movie poster
        Glide.with(getBaseContext()).load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

//        Timber.tag(TAG)
//                .i("Title: %s, Server id: %s", movie.getTitle(), movie.getServerId());


        //Movie backdrop poster
        Glide.with(getBaseContext()).load(
                "https://image.tmdb.org/t/p/w1300_and_h730_bestv2/" + movie.getBackdropPath())
                .thumbnail(1)
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(theme);
//
//        //Movie trailer
//        if(!TextUtils.isEmpty(movie.getYoutubeTrailer())){
//            themePlayButtonContainer.setVisibility(View.VISIBLE);
//
//            themePlayButtonContainer.setOnClickListener(view -> {
//
//                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
//                        this, getString(R.string.google_api_key),
//                        movie.getYoutubeTrailer(), 0, true, false);
//
//                if (canResolveIntent(intent)) {
//                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//                } else {
//                    // Could not resolve the intent - must need to install or update the YouTube API service.
//                    YouTubeInitializationResult.SERVICE_MISSING
//                            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
//                }
//            });
//        }
//        if(!TextUtils.isEmpty(movie.getYoutubeTrailer())){
//            themePlayButtonContainer.setVisibility(View.VISIBLE);
//
//            themePlayButtonContainer.setOnClickListener(view -> {
//
//                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
//                        this, getString(R.string.google_api_key),
//                        movie.getYoutubeTrailer(), 0, true, false);
//
//                if (canResolveIntent(intent)) {
//                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//                } else {
//                    // Could not resolve the intent - must need to install or update the YouTube API service.
//                    YouTubeInitializationResult.SERVICE_MISSING
//                            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
//                }
//            });
//        }
//
//        //Play button
        playButton.setOnClickListener(v -> {
            startActivity(buildVideoPlayerIntent(this, movie.getServerId(), movie.getId()));
        });

        //Movie title
        title.setText(movie.getTitle());

//        //Release year
        Calendar movieReleaseDate = Calendar.getInstance();
        movieReleaseDate.setTimeInMillis(movie.getReleaseDate());

        subtitle.setText(movieReleaseDate.get(Calendar.YEAR) + "");

        //Runtime
        runtime.setText(getString(R.string.runtime, (int)(movie.getRuntime() / 60),
                (int)(movie.getRuntime() % 60)));

//        //Rating
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
        releaseDate.setText(getFormattedDate(movieReleaseDate));

        //Director/s
        List<CrewRelationModel> movieDirectors =
                videosLibrary.searchPersonByJob(movie.getId(), VideosLibrary.DIRECTOR);

        for(CrewRelationModel crewRelationModel: movieDirectors){
            if(director.length() > 0)
                director.append(", ");

            director.append(crewRelationModel.getPerson().getName());
        }

        //Movie overview
        overviewContent.setText(movie.getOverview());
        overviewContent.setInterpolator(new OvershootInterpolator());

        overviewContent.post(() -> {
            if(overviewContent.getLineCount() >= overviewContent.getMaxLines()){
                readMore.setOnClickListener(v -> {
                    overviewContent.toggle();

                    readMore.setText(overviewContent.isExpanded()
                            ? getString(R.string.read_more)
                            : getString(R.string.collapse));
                });

                readMore.setVisibility(View.VISIBLE);
            }
        });


        //Cast
        List<PersonModel> cast = new LinkedList<>();

        for(CastRelationModel castRelation : movie.getCast())
            cast.add(castRelation.getPerson());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        castList.setNestedScrollingEnabled(false);
        castList.setLayoutManager(layoutManager);

        CastAdapter castAdapter = new CastAdapter(OverviewActivity.this, cast);

        castList.setAdapter(castAdapter);

        container.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(alreadyPushedDown)
                return;

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            //int width = size.x;

            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)
                    castContainer.getLayoutParams();

            int screenHeight = size.y - lp.topMargin;
            int containerHeight = container.getHeight();
            int topMargin = screenHeight - containerHeight - 50;

            ((ConstraintLayout.LayoutParams) castContainer.getLayoutParams())
                    .setMargins(0, topMargin < 60 ? 60 : topMargin, 0, 0);

            alreadyPushedDown = true;
        });
    }

    private Intent buildVideoPlayerIntent(Context context, String videoServerId, long videoId) {

        String url = tgbAPI.getM3u8Url(videoServerId);

        Intent intent = new Intent(context, VideoPlayerActivity.class);

        //Details
        intent.setAction(OverviewActivity.MOVIE_ACTION);
        intent.putExtra(OverviewActivity.MOVIE_ID, videoId);

        //Video source
        intent.putExtra(VideoPlayerActivity.MEDIA_SOURCE_URL, url);

        return intent;
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    private String getFormattedDate(Calendar c) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
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
}
