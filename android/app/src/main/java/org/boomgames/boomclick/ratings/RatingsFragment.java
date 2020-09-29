package org.boomgames.boomclick.ratings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.boomgames.boomclick.FragmentActionListener;
import org.boomgames.boomclick.R;
import org.boomgames.boomclick.api.BoomClickerApi;
import org.boomgames.boomclick.api.TableApi;
import org.boomgames.boomclick.utils.Cache;
import org.boomgames.boomclick.utils.NetworkUtils;

public class RatingsFragment extends Fragment implements View.OnClickListener {
    private FragmentActionListener listener;
    private TableApi tableApi;

    public RatingsFragment() {
        this.tableApi = BoomClickerApi.getInstance().getTableApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ratings, container, false);
        rootView.findViewById(R.id.scores_table_ok).setOnClickListener(this);

        RecyclerView ratingsView = rootView.findViewById(R.id.ratingsView);
        ratingsView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        if(NetworkUtils.isConnected(getContext())) {
            tableApi.top().async(new RatingsCallback(ratingsView));
        } else {
            ratingsView.setAdapter(new RatingsAdapter(Cache.getInstance().restoreTableTop()));
        }

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FragmentActionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        listener.onFragmentShouldBeHidden(false);
    }
}