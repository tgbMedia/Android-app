package com.tgb.media.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.tgb.media.R;
import com.tgb.media.listener.OnLoadingScreenIsGone;

import butterknife.ButterKnife;

public class LoadingDialog extends RelativeLayout {

    public LoadingDialog(Context context) {
        super(context);
        init();
    }

    public LoadingDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.overlay_loading_gallery, this);
        ButterKnife.bind(this);
    }

    public void show(OnLoadingScreenIsGone listener){
        clearAnimation();
        setVisibility(VISIBLE);

        Animation fadeIn = new AlphaAnimation(0, 1);

        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener != null)
                    listener.onAnimationFinish(LoadingDialog.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        startAnimation(fadeIn);
    }

    public void hide(OnLoadingScreenIsGone listener){
        Animation fadeOut = new AlphaAnimation(1, 0);

        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener != null)
                    listener.onAnimationFinish(LoadingDialog.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        startAnimation(fadeOut);
    }
}
