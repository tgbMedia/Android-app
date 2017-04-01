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
    }

    public void show(){
        Animation fadeOut = new AlphaAnimation(0, 1);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(1200);
        fadeOut.setFillAfter(true);

        startAnimation(fadeOut);
    }

    public void hide(OnLoadingScreenIsGone listener){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(1200);
        fadeOut.setFillAfter(true);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener != null)
                    listener.loadingScreenIsGone(LoadingDialog.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        startAnimation(fadeOut);
    }
}
