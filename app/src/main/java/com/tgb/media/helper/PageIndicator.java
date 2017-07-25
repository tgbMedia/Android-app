package com.tgb.media.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

public class PageIndicator extends ExtensiblePageIndicator {

    private ViewPager mViewPager;

    public PageIndicator(Context context) {
        super(context);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void initViewPager(ViewPager viewPager) {
        super.initViewPager(viewPager);
        this.mViewPager = viewPager;
    }

    @Override
    protected void onAttachedToWindow() {
        if(mViewPager != null)
            mViewPager.addOnPageChangeListener(this);

        super.onAttachedToWindow();
    }
}
