package com.jungel.base.widget;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by lion on 2017/5/15.
 */

public class ListItemAnimator {

    private long mLastPosition = -1;

    public void startAnimation(final View rootView, long position) {
        if (position > mLastPosition) {
            long animTime = 400;

            ObjectAnimator translateAnimator =
                    ObjectAnimator.ofFloat(rootView, "translationY", 300, 0);
            translateAnimator.setDuration(animTime);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rootView,
                    "scaleX", 0.7f, 1.0f);
            scaleXAnimator.setDuration(animTime);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rootView,
                    "scaleY", 0.7f, 1.0f);
            scaleYAnimator.setDuration(animTime);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rootView,
                    "alpha", 0.4f, 1.0f);
            scaleYAnimator.setDuration(animTime);

            translateAnimator.start();
            scaleXAnimator.start();
            scaleYAnimator.start();
            alphaAnimator.start();

            mLastPosition = position;
        }
    }

    public void resetAnimatorPosition() {
        mLastPosition = -1;
    }
}
