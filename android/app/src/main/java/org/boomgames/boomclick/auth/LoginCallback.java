package org.boomgames.boomclick.auth;

import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.boomgames.boomclick.FragmentActionListener;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclick.R;
import org.boomgames.boomclick.utils.ViewUtils;
import org.boomgames.boomclicker.User;
import org.cuba.neofit.NeoCall;
import org.cuba.neofit.NeoCallback;
import org.cuba.neofit.NeoResponse;
import org.cuba.neofit.exceptions.NeofitException;

public class LoginCallback implements NeoCallback {
    private static final String TAG_NAME = LoginCallback.class.getName();
    private TextView passwordView;
    private FragmentActionListener listener;

    public LoginCallback(TextView passwordView, FragmentActionListener listener) {
        this.passwordView = passwordView;
        this.listener = listener;
    }

    private void show(int resId) {
        Snackbar.make(passwordView, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(NeoCall call, NeoResponse response) {
        try {
            Log.i(TAG_NAME, "Response code: " + response.code());
            switch(response.code()) {
                case 200: {
                    User user = response.to(User.class);
                    user.setPassword(ViewUtils.getText(passwordView));
                    show(R.string.auth_login_success);
                    Cache.getInstance().saveUser(user);
                    listener.onFragmentShouldBeHidden(true);
                } break;
                case 404: {
                    show(R.string.auth_error_not_found);
                } break;
                case 403: {
                    show(R.string.auth_error_forbidden);
                } break;
                case 500: {
                    show(R.string.auth_error_server);
                } break;
            }
        } catch(NeofitException e) {
            Log.e(TAG_NAME, "Failed to process response", e);
        }
    }

    @Override
    public void onFailure(NeoCall call, Throwable throwable) {
        Log.e(TAG_NAME, "Failure at executing request", throwable);
        listener.onFragmentShouldBeHidden(false);
    }
}
