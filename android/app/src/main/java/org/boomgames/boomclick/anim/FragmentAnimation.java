package org.boomgames.boomclick.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FragmentAnimation implements Animation.AnimationListener {
    private Animation fadeIn, fadeOut;
    private View containerShadow;

    public FragmentAnimation(Context context, View containerShadow) {
        fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeIn.setAnimationListener(this);
        fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        fadeOut.setAnimationListener(this);
        this.containerShadow = containerShadow;
    }

    public void show() {
        containerShadow.setClickable(true);
        containerShadow.startAnimation(fadeIn);
        containerShadow.setVisibility(View.VISIBLE);
    }

    public void hide() {
        containerShadow.startAnimation(fadeOut);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation.equals(fadeOut)) {
            containerShadow.setVisibility(View.GONE);
            containerShadow.setClickable(false);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }
}
