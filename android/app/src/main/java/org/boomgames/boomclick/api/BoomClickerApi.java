package org.boomgames.boomclick.api;

import android.util.Log;

import com.google.gson.Gson;

import org.cuba.neofit.Android;
import org.cuba.neofit.Neofit;
import org.cuba.neofit.exceptions.NeofitException;
import org.cuba.neofit.gson.GsonBodyFactory;
import org.cuba.neofit.gson.GsonPartsFactory;
import org.cuba.neofit.gson.GsonQueryFactory;

public class BoomClickerApi {
    private static final String TAG_NAME = BoomClickerApi.class.getName();

    private static BoomClickerApi instance;

    public static BoomClickerApi getInstance() {
        if(instance == null) {
            instance = new BoomClickerApi();
        }
        return instance;
    }

    private TableApi tableApi;
    private UserApi userApi;

    private BoomClickerApi() {
        Gson gson = new Gson();
        Neofit neofit = new Neofit.Builder()
            .baseUrl("https://boom-clicker.herokuapp.com/")
            .platform(new Android())
            .addBodyFactory(new GsonBodyFactory(gson))
            .addPartsFactory(new GsonPartsFactory(gson))
            .addQueryFactory(new GsonQueryFactory(gson))
            .build();

        try {
            this.tableApi = neofit.create(TableApi.class);
            this.userApi = neofit.create(UserApi.class);
        } catch (NeofitException e) {
            Log.e(TAG_NAME, "Failed to create API", e);
        }
    }

    public boolean isCorrect() {
        return tableApi != null;
    }

    public TableApi getTableApi() {
        return tableApi;
    }

    public UserApi getUserApi() {
        return userApi;
    }
}
