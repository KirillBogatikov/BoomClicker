package org.boomgames.boomclick.ratings;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.boomgames.boomclick.R;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclicker.User;
import org.cuba.neofit.NeoCall;
import org.cuba.neofit.NeoCallback;
import org.cuba.neofit.NeoResponse;

public class RatingsCallback implements NeoCallback {
    private static final String TAG_NAME = RatingsCallback.class.getName();
    private RecyclerView ratingsView;

    public RatingsCallback(RecyclerView ratingsView) {
        this.ratingsView = ratingsView;
    }

    @Override
    public void onResponse(NeoCall call, NeoResponse response) {
        Log.i(TAG_NAME, "Response with status " + response.code() + " received");
        switch(response.code()) {
            case 200: {
                try {
                    User[] users = response.to(User[].class);
                    Cache.getInstance().saveTableTop(users);
                    ratingsView.setAdapter(new RatingsAdapter(users));
                } catch(Throwable t) {
                    Log.e(TAG_NAME, "Exception at parsing", t);
                    Snackbar.make(ratingsView, R.string.ratings_error_parsing, Snackbar.LENGTH_SHORT).show();
                }
            } break;
            case 404: {
                Snackbar.make(ratingsView, R.string.ratings_error_not_found, Snackbar.LENGTH_SHORT).show();
            } break;
            case 500: {
                Snackbar.make(ratingsView, R.string.ratings_error_internal_server, Snackbar.LENGTH_SHORT).show();
            } break;
        }
    }

    @Override
    public void onFailure(NeoCall call, Throwable throwable) {
        Log.e(TAG_NAME, "Failure", throwable);
    }
}
