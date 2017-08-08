package com.tgb.media.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tgb.media.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PersonView extends PercentRelativeLayout {

    private Context context;

    @BindView(R.id.profile_photo) ImageView profilePhoto;
    @BindView(R.id.name) TextView personName;

    public PersonView(Context context) {
        super(context);
        init(context);
    }

    public PersonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.person_layout, this);
        ButterKnife.bind(this);

        this.context = context;
    }

    public void setProfilePhoto(String personPhoto){
        Glide.with(context).load("https://image.tmdb.org/t/p/w640/" + personPhoto)
                .thumbnail(1)
                .transition(withCrossFade())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(profilePhoto);
    }

    public void setName(String name){
        personName.setText(name);
    }

}
