package org.boomgames.boomclick;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.boomgames.boomclick.anim.BalloonAnimation;
import org.boomgames.boomclick.anim.FragmentAnimation;
import org.boomgames.boomclick.ratings.RatingsFragment;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclick.utils.NetworkUtils;
import org.boomgames.boomclick.utils.ViewUtils;

public class MainActivity extends AppCompatActivity implements FragmentActionListener {
    private static final String TAG_NAME = MainActivity.class.getName();

    private TextView scoreCounterView;
    private BalloonAnimation balloonAnim;
    private FragmentAnimation fragmentAnim;
    private boolean fragmentShown = false;
    private Cache cache;
    private long score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeCache();

        ImageView balloonView = findViewById(R.id.balloon_view);
        scoreCounterView = findViewById(R.id.score_counter_view);

        DisplayMetrics displayMetrics = ViewUtils.getDisplayMetrics(this);

        balloonAnim = new BalloonAnimation(this, balloonView, displayMetrics.widthPixels);
        fragmentAnim = new FragmentAnimation(this, findViewById(R.id.fragment_shadow));

        onPauseIconClick(null);
        syncDataWithServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        moveUpBalloon();
        score = cache.restoreUserScore();
        showScore(false);
    }

    @Override
    protected void onStop() {
        cache.saveUserScore(score);
        syncDataWithServer();
        super.onStop();
    }

    private void syncDataWithServer() {
        if(NetworkUtils.isConnected(this) && cache.hasUser()) {
            try {
                NetworkUtils.synchronize();
                Log.i(TAG_NAME, "Synchronized with server");
            } catch(Exception e) {
                Log.e(TAG_NAME, "Failed to sync", e);
            }
        }
    }

    private void initializeCache() {
        cache = Cache.newInstance(this);
        if(cache.restoreTableTop() == null) {
            Log.i(TAG_NAME, "Table top not found in cache");
        }
        if(cache.restoreUser() == null) {
            Log.i(TAG_NAME, "User not found in cache");
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragmentAnim.show();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        fragmentShown = true;
    }

    private void hideFragment() {
        fragmentShown = false;
        fragmentAnim.hide();
    }

    @Override
    public void onBackPressed() {
        if(fragmentShown) {
            Log.i(TAG_NAME, "hide current fragment");
            hideFragment();
        } else {
            super.onBackPressed();
        }
    }

    private void moveUpBalloon() {
        Log.i(TAG_NAME, "move up balloon");
        balloonAnim.moveUp();
    }

    private void showScore(boolean increase) {
        if(increase) {
            score++;
        }
        Log.i(TAG_NAME, "Show score: " + score);
        scoreCounterView.setText(String.valueOf(score));
    }

    public void onFragmentShadowClick(View view) {
        hideFragment();
    }

    public void onRatingsIconClick(View view) {
        if(cache.restoreTableTop() == null && !NetworkUtils.isConnected(this)) {
            Snackbar.make(scoreCounterView, R.string.no_connection, Snackbar.LENGTH_LONG).show();
        } else {
            Log.i(TAG_NAME, "show ratings");
            showFragment(new RatingsFragment());
        }
    }

    public void onPauseIconClick(View view) {
        Log.i(TAG_NAME, "show pause");
        showFragment(new PauseFragment());
    }

    public void onBalloonClick(View view) {
        showScore(true);
        balloonAnim.grow(score);
        if(score % BalloonAnimation.NEXT_LEVEL_STEP == 0) {
            moveUpBalloon();
        }
    }

    @Override
    public void onFragmentShouldBeShown(Fragment fragment) {
        showFragment(fragment);
    }

    @Override
    public void onFragmentShouldBeHidden(boolean showScore) {
        if(showScore) {
            score = cache.restoreUserScore();
            showScore(false);
        }
        hideFragment();
    }
}
