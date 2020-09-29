package org.boomgames.boomclick.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.boomgames.boomclicker.User;

public class Cache {
    private static Cache instance;

    public static Cache newInstance(Context context) {
        return instance = new Cache(context);
    }

    public static Cache getInstance() {
        return instance;
    }

    private static final String KEY_USER = "user-info";
    private static final String KEY_TABLE_TOP = "table-top";
    private static final String KEY_SCORE = "user-score";
    private static final String KEY_NEW_GAME = "user-new-game";

    private Gson gson;
    private SharedPreferences preferences;

    private Cache(Context context) {
        this.gson = new Gson();
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        Log.i("Cache", gson.toJson(user));
        preferences.edit()
            .putString(KEY_USER, gson.toJson(user))
            .apply();
    }

    public User restoreUser() {
        String json = preferences.getString(KEY_USER, null);
        if(json == null) {
            return null;
        }
        return gson.fromJson(json, User.class);
    }

    public boolean hasUser() {
        return restoreUser() != null;
    }

    public void saveTableTop(User[] tableTop) {
        preferences.edit()
            .putString(KEY_TABLE_TOP, gson.toJson(tableTop))
            .apply();
    }

    public User[] restoreTableTop() {
        String json = preferences.getString(KEY_TABLE_TOP, null);
        if(json == null) {
            return null;
        }
        return gson.fromJson(json, User[].class);
    }

    public long restoreUserScore() {
        User user = restoreUser();
        if(user != null) {
            Log.i("Cache", "loading from user");
            return restoreUser().getScore();
        }
        Log.i("Cache", "loading from prefs");
        return preferences.getLong(KEY_SCORE, 0L);
    }

    public void saveUserScore(long score) {
        if(hasUser()) {
            User user = restoreUser();
            user.setScore(score);
            saveUser(user);
        }

        Log.i("Cache", "saving to prefs");
        preferences.edit()
            .putLong(KEY_SCORE, score)
            .apply();
    }

    public boolean isNewGame() {
        return preferences.getBoolean(KEY_NEW_GAME, false);
    }

    public void markAsNewGame(boolean newGame) {
        preferences.edit().putBoolean(KEY_NEW_GAME, newGame).apply();
    }
}
