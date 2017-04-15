package com.tgb.media.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tgb.media.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverlayMessageView extends FrameLayout{

    @BindView(R.id.message) TextView tvMessage;

    public OverlayMessageView(Context context) {
        super(context);
        init();
    }

    public OverlayMessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayMessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.overlay_message, this);
        ButterKnife.bind(this);
    }

    public void setMessage(String message){
        tvMessage.setText(message);
    }

    public void show(){
        setVisibility(VISIBLE);
    }

    public void hide(){
        setVisibility(GONE);
    }

}
