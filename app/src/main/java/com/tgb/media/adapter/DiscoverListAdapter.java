package com.tgb.media.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import com.tgb.media.R;
import com.tgb.media.activities.OverviewActivity;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.MovieObservableResult;

import java.util.LinkedList;
import java.util.List;

public class DiscoverListAdapter extends RecyclerView.Adapter<DiscoverListAdapter.ThumbHolder> {

    private List<MovieOverviewModel> movies;
    private Activity mContext;

    private int columnHeight;
    private int lastClickedItem = -1;

    public class ThumbHolder extends RecyclerView.ViewHolder {
        public View container;
        public ImageView thumbnail;
        public TextView title;

        public ThumbHolder(View view) {
            super(view);

            container = view.findViewById(R.id.container);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public DiscoverListAdapter(Activity context, Point screenDimen, int orientation) {
        this.mContext = context;
        this.movies = new LinkedList<>();

        if(orientation == Configuration.ORIENTATION_PORTRAIT)
            columnHeight = (int)(screenDimen.y * 0.27);
        else
            columnHeight = (int)(screenDimen.y * 0.5);
    }

    public void setList(List<MovieOverviewModel> list){
        this.movies = list;
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void addItem(MovieObservableResult movieObservableResult){
        movies.add(movieObservableResult.movie);
        //notifyDataSetChanged();
        notifyItemChanged(movieObservableResult.position);
    }

    public MovieOverviewModel getItem(int position){
        return movies.get(position);
    }

    @Override
    public ThumbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new ThumbHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThumbHolder holder, int position) {
        final MovieOverviewModel movie = movies.get(position);

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                .thumbnail(1)
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(holder.thumbnail);

        holder.title.setText(movie.getTitle());

        holder.container.setOnClickListener(v -> {
            lastClickedItem = position;
            Intent detailsActivityIntent = new Intent(mContext, OverviewActivity.class);

            detailsActivityIntent.setAction(OverviewActivity.MOVIE_ACTION);
            detailsActivityIntent.putExtra(OverviewActivity.MOVIE_ID, movie.getId());

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            mContext,
                            holder.thumbnail,
                            mContext.getString(R.string.gallery_transition)
                    );

            mContext.startActivity(detailsActivityIntent, options.toBundle());
        });

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                (int)(columnHeight * 0.66), columnHeight);

        holder.container.setLayoutParams(params);
    }

    public int getLastClickedItem() {
        return lastClickedItem;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}