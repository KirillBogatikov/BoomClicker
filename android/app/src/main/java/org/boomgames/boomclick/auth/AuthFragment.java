package org.boomgames.boomclick.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.boomgames.boomclick.FragmentActionListener;
import org.boomgames.boomclick.PauseFragment;
import org.boomgames.boomclick.R;
import org.boomgames.boomclick.api.BoomClickerApi;
import org.boomgames.boomclick.api.UserApi;
import org.boomgames.boomclick.utils.ViewUtils;
import org.boomgames.boomclicker.User;

public class AuthFragment extends Fragment implements View.OnClickListener {
    private UserApi userApi;
    private View root;
    private EditText loginView, passwordView;
    private SignupCallback signupCallback;
    private LoginCallback loginCallback;
    private FragmentActionListener listener;

    public AuthFragment() {
        userApi = BoomClickerApi.getInstance().getUserApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_auth, container, false);
        loginView = root.findViewById(R.id.field_nick);
        passwordView = root.findViewById(R.id.field_password);
        signupCallback = new SignupCallback(passwordView, listener);
        loginCallback = new LoginCallback(passwordView, listener);

        root.findViewById(R.id.login).setOnClickListener(this);
        root.findViewById(R.id.signup).setOnClickListener(this);
        root.findViewById(R.id.back).setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        listener = (FragmentActionListener)ctx;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login: {
                userApi.login(ViewUtils.getText(loginView), ViewUtils.getText(passwordView)).async(loginCallback);
            } break;
            case R.id.signup: {
                User user = new User();
                user.setNick(ViewUtils.getText(loginView));
                user.setPassword(ViewUtils.getText(passwordView));
                userApi.signup(user).async(signupCallback);
            } break;
            case R.id.back: {
                listener.onFragmentShouldBeShown(new PauseFragment());
            } break;
        }
    }

}
