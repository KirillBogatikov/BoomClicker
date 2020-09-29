package org.boomgames.boomclick.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.boomgames.boomclick.api.BoomClickerApi;
import org.boomgames.boomclick.api.UserApi;
import org.boomgames.boomclicker.User;
import org.cuba.neofit.NeoCall;
import org.cuba.neofit.NeoCallback;
import org.cuba.neofit.NeoResponse;

public class NetworkUtils {
    private static final String TAG_NAME = NetworkUtils.class.getName();

    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if(activeNetworkInfo != null) {
            Log.i(TAG_NAME, "Active network found");
            return activeNetworkInfo.isConnectedOrConnecting();
        }

        Log.i(TAG_NAME, "No active network");
        return false;
    }

    public static void synchronize() throws Exception  {
        final Cache cache = Cache.getInstance();
        UserApi userApi = BoomClickerApi.getInstance().getUserApi();

        User local = cache.restoreUser();
        User server = userApi.get(local.getId()).sync().to(User.class);

        if(server.getScore() > local.getScore() && !cache.isNewGame()) {
            Log.i(TAG_NAME, "Synchronizing from server");
            cache.saveUser(server);
        } else {
            cache.markAsNewGame(false);
            Log.i(TAG_NAME, "Uploading to server");
            userApi.update(local).async(new NeoCallback() {
                @Override
                public void onResponse(NeoCall call, NeoResponse response) {
                    Log.i(TAG_NAME, "Uploading status: " + response.code());
                }

                @Override
                public void onFailure(NeoCall call, Throwable throwable) {
                    Log.e(TAG_NAME, "Uploading failure", throwable);
                    cache.saveUser(null);
                }
            });
        }
    }

}
