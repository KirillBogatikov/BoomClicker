package org.boomgames.boomclick.anim;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import org.boomgames.boomclick.R;

public class BalloonAnimation {
    public static final int NEXT_LEVEL_STEP = 250;

    private Animation moveUpAnimation;
    private View balloonView;
    private RelativeLayout.LayoutParams defaultLayoutParams;
    private MediaPlayer player;
    private int maxWidth;

    public BalloonAnimation(Context context, View balloonView, int maxWidth) {
        this.moveUpAnimation = AnimationUtils.loadAnimation(context, R.anim.balloon_movement);
        this.balloonView = balloonView;

        defaultLayoutParams = clone((RelativeLayout.LayoutParams)balloonView.getLayoutParams());

        this.maxWidth = (maxWidth - defaultLayoutParams.width);

        this.player = MediaPlayer.create(context, R.raw.balloon_boom);
    }

    public void moveUp() {
        balloonView.startAnimation(moveUpAnimation);
    }

    public void grow(long score) {
        if(score % NEXT_LEVEL_STEP == 0) {
            balloonView.setLayoutParams(clone(defaultLayoutParams));
            player.start();
        } else {
            ViewGroup.LayoutParams params = balloonView.getLayoutParams();
            params.width += (maxWidth / NEXT_LEVEL_STEP);
            params.height += (maxWidth / NEXT_LEVEL_STEP);
            balloonView.setLayoutParams(params);
        }
    }

    private RelativeLayout.LayoutParams clone(RelativeLayout.LayoutParams currentLayoutParams) {
        RelativeLayout.LayoutParams defaultLayoutParams = new RelativeLayout.LayoutParams(currentLayoutParams.width, currentLayoutParams.height);

        defaultLayoutParams.alignWithParent = currentLayoutParams.alignWithParent;
        defaultLayoutParams.layoutAnimationParameters = currentLayoutParams.layoutAnimationParameters;
        defaultLayoutParams.topMargin = currentLayoutParams.topMargin;
        defaultLayoutParams.leftMargin = currentLayoutParams.leftMargin;
        defaultLayoutParams.rightMargin = currentLayoutParams.rightMargin;
        defaultLayoutParams.bottomMargin = currentLayoutParams.bottomMargin;

        int[] rulesToCopy = currentLayoutParams.getRules();
        for (int verb = 0; verb < rulesToCopy.length; verb++) {
            int subject = rulesToCopy[verb];
            defaultLayoutParams.addRule(verb, subject);
        }

        return defaultLayoutParams;
    }
}
