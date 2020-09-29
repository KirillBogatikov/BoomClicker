package org.boomgames.boomclick.auth;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.boomgames.boomclick.FragmentActionListener;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclick.R;
import org.boomgames.boomclick.utils.ViewUtils;
import org.boomgames.boomclicker.User;
import org.boomgames.boomclicker.UserError;
import org.cuba.neofit.NeoCall;
import org.cuba.neofit.NeoCallback;
import org.cuba.neofit.NeoResponse;
import org.cuba.neofit.exceptions.NeofitException;

public class SignupCallback implements NeoCallback {
    private static final String TAG_NAME = SignupCallback.class.getName();
    private TextView passwordView;
    private FragmentActionListener listener;

    public SignupCallback(TextView passwordView, FragmentActionListener listener) {
        this.passwordView = passwordView;
        this.listener = listener;
    }

    private void show(int resId) {
        Snackbar.make(passwordView, resId, Snackbar.LENGTH_LONG).show();
    }

    private void show(int resId, Object... args) {
        String message = passwordView.getContext().getString(resId, args);
        Snackbar.make(passwordView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(NeoCall call, NeoResponse response) {
        try {
            Log.i(TAG_NAME, "Response code: " + response.code());
            switch(response.code()) {
                case 200: {
                    User user = response.to(User.class);
                    user.setPassword(ViewUtils.getText(passwordView));
                    show(R.string.auth_signup_success);
                    Cache.getInstance().saveUser(user);
                    listener.onFragmentShouldBeHidden(true);
                } break;
                case 400: {
                    UserError error = response.to(UserError.class);
                    if(error.isNickBusy()) {
                        Log.i(TAG_NAME, "nick busy");
                        show(R.string.auth_error_nick_busy);
                    } else if(!error.isNickValid()) {
                        Log.i(TAG_NAME, "nick invalid");
                        show(R.string.auth_error_nick_invalid, 4);
                    } else if(!error.isPasswordValid()) {
                        Log.i(TAG_NAME, "password invalid");
                        show(R.string.auth_error_password_invalid, User.MIN_PASSWORD_LENGTH);
                    }
                    listener.onFragmentShouldBeHidden(true);
                } break;
            }
        } catch(NeofitException e) {
            Log.e(TAG_NAME, "Failed to process response", e);
            listener.onFragmentShouldBeHidden(false);
        }
    }

    @Override
    public void onFailure(NeoCall call, Throwable throwable) {
        Log.e(TAG_NAME, "Failure at executing request", throwable);
    }
}
