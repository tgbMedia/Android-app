package com.tgb.media.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tgb.media.R;
import com.tgb.media.activities.OverviewActivity;
import com.tgb.media.database.MovieOverviewModel;
import com.tgb.media.helper.MovieObesrvableResult;

import java.util.LinkedList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<MovieOverviewModel> movies;
    private Activity mContext;

    private int columnHeight;

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

    public GalleryAdapter(Activity context, Point screenDimen, int orientation) {
        this.mContext = context;
        this.movies = new LinkedList<>();

        if(orientation == Configuration.ORIENTATION_PORTRAIT)
            columnHeight = (int)(screenDimen.y * 0.27);
        else
            columnHeight = (int)(screenDimen.y * 0.5);
    }

    public void clear() {
        int size = movies.size();
        movies.clear();

        notifyDataSetChanged();
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

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, columnHeight);

        holder.container.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}