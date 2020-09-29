package org.boomgames.boomclick.ratings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boomgames.boomclick.R;
import org.boomgames.boomclicker.User;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ItemViewHolder> {
    private User[] users;

    public RatingsAdapter(User[] users) {
        this.users = users;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView positionView;
        private TextView nickView;
        private TextView scoreView;

        ItemViewHolder(View root) {
            super(root);
            positionView = root.findViewById(R.id.ratings_item_position);
            nickView = root.findViewById(R.id.ratings_item_nick);
            scoreView = root.findViewById(R.id.ratings_item_score);
        }

        void setPosition(int position) {
            positionView.setText(String.valueOf(position));
        }

        void setNick(String nick) {
            nickView.setText(nick);
        }

        void setScore(long score) {
            scoreView.setText(String.valueOf(score));
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setPosition(position + 1);
        holder.setNick(users[position].getNick());
        holder.setScore(users[position].getScore());
    }

    @Override
    public int getItemCount() {
        return users.length;
    }
}