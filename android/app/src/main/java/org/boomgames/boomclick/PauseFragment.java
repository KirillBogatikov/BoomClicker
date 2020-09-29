package org.boomgames.boomclick;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.boomgames.boomclick.auth.AuthFragment;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclick.utils.NetworkUtils;

public class PauseFragment extends Fragment implements View.OnClickListener {
    private Cache cache;
    private FragmentActionListener listener;

    public PauseFragment() {
        cache = Cache.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pause, container, false);
        root.findViewById(R.id.pause_new_game).setOnClickListener(this);
        root.findViewById(R.id.pause_continue).setOnClickListener(this);
        root.findViewById(R.id.pause_auth).setOnClickListener(this);
        root.findViewById(R.id.pause_exit).setOnClickListener(this);

        if(!NetworkUtils.isConnected(requireContext()) || cache.hasUser()) {
            root.findViewById(R.id.pause_auth).setVisibility(View.GONE);
        } else {
            root.findViewById(R.id.pause_auth).setVisibility(View.VISIBLE);
        }

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
            case R.id.pause_new_game:
                cache.saveUserScore(0L);
                cache.markAsNewGame(true);
                listener.onFragmentShouldBeHidden(true);
            break;
            case R.id.pause_continue:
                listener.onFragmentShouldBeHidden(false);
            break;
            case R.id.pause_auth:
                listener.onFragmentShouldBeShown(new AuthFragment());
            break;
            case R.id.pause_exit:
                requireActivity().finish();
            break;
        }
    }
}
