package com.tgb.media.helper;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressWarnings("unused")
public class CoordinatorFlingBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "CoordinatorFling";

    public CoordinatorFlingBehavior() {
    }

    public CoordinatorFlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        // Passing false for consumed will make the AppBarLayout fling everything and pull down the expandable stuff
        if (target instanceof NestedScrollView && velocityY < 0) {
            final NestedScrollView scrollView = (NestedScrollView) target;
            int scrollY = scrollView.getScrollY();

            // Note the ! in front
            consumed = !(scrollY < 3 // if below threshold, fling
                    || isScrollingUpFast(scrollY, velocityY)); // Or if moving quickly, fling

            Log.v(TAG, "onNestedFling: scrollY = " + scrollY + ", velocityY = " + velocityY + ", flinging = " + !consumed);
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    /**
     * This uses the log of the velocity because constants make it too easy to uncouple the CoordinatorLayout - the AppBarLayout and the NestedScrollView - when scrollPosition is small.
     *
     * @param scrollPosition - of the NestedScrollView target
     * @param velocityY      - Y velocity. Should be negative, because scrolling up is negative. However, a positive value won't crash this method.
     * @return true if scrolling up fast
     */
    private boolean isScrollingUpFast(int scrollPosition, float velocityY) {
        float positiveVelocityY = Math.abs(velocityY);

        double calculation = scrollPosition * Math.log(positiveVelocityY);

        return positiveVelocityY > calculation;
    }

}