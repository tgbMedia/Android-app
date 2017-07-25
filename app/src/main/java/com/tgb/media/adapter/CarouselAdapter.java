package com.tgb.media.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tgb.media.R;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.MovieObservableResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarouselAdapter extends PagerAdapter{

    private Context context;
    private List<MovieOverviewModel> videos;

    public CarouselAdapter(Context context){
        this.context = context;
        this.videos = new ArrayList<>();
    }

    public CarouselAdapter(Context context, List<MovieOverviewModel> videos){
        this.context = context;
        this.videos = videos;
    }

    public void clear() {
        videos.clear();
        notifyDataSetChanged();
    }

    public void addItem(MovieObservableResult movieObservableResult){
        videos.add(movieObservableResult.movie);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.carousel_item,null);

        ImageView imageView = (ImageView) view.findViewById(R.id.video_thumb);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        MovieOverviewModel movie = videos.get(position);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w640/" + movie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        //Video title
        title.setText(movie.getTitle());

        //Release year
        Calendar movieReleaseDate = Calendar.getInstance();
        movieReleaseDate.setTimeInMillis(movie.getReleaseDate());

        subtitle.setText(movieReleaseDate.get(Calendar.YEAR) + "");

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
