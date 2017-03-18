package com.tgb.media.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.tgb.media.R;

import butterknife.BindView;

public class PlayerActivity extends AppCompatActivity
    implements OnPreparedListener{

    //@BindView(R.id.container) ConstraintLayout container;
    @BindView(R.id.exo_player_view) EMVideoView emVideoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


    }
    private void setupVideoView() {
        emVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
    }

    @Override
    public void onPrepared() {

    }
}
