package com.tgb.media.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tgb.media.R;
import com.tgb.media.activities.DetailsActivity;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.MovieObesrvableResult;

import java.util.LinkedList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<MovieOverviewModel> movies;
    private Activity mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View container;
        public ImageView thumbnail;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.container);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public GalleryAdapter(Activity context) {
        this.mContext = context;
        this.movies = new LinkedList<>();
    }

    public void addItem(MovieObesrvableResult movieObesrvableResult){
        movies.add(movieObesrvableResult.movie);
        notifyItemChanged(movieObesrvableResult.position);
    }

    public MovieOverviewModel getItem(int position){
        return movies.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MovieOverviewModel movie = movies.get(position);

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w640/" + movie.getPosterPath())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

        holder.title.setText(movie.getTitle());

        holder.container.setOnClickListener(v -> {
            Intent detailsActivityIntent = new Intent(mContext, DetailsActivity.class);

            detailsActivityIntent.setAction(DetailsActivity.MOVIE_ACTION);
            detailsActivityIntent.putExtra(DetailsActivity.MOVIE_ID, movie.getId());

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            mContext,
                            holder.thumbnail,
                            mContext.getString(R.string.gallery_transition)
                    );

            mContext.startActivity(detailsActivityIntent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}