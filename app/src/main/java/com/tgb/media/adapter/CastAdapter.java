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
import com.tgb.media.database.PersonModel;
import com.tgb.media.helper.MovieObesrvableResult;

import java.util.LinkedList;
import java.util.List;



public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {
    private List<PersonModel> personsList;
    private Activity mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePhoto;
        public TextView name;

        public MyViewHolder(View view) {
            super(view);

            profilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

    public CastAdapter(Activity context, List<PersonModel> personsList) {
        this.mContext = context;
        this.personsList = personsList;
    }

    @Override
    public CastAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_thumbnail, parent, false);

        return new CastAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CastAdapter.MyViewHolder holder, int position) {
        final PersonModel person = personsList.get(position);

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w640/" + person.getPhoto())
                .thumbnail(1)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profilePhoto);

        holder.name.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

}
