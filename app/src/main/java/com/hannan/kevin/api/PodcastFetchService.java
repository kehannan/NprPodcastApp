package com.hannan.kevin.api;

import android.app.IntentService;
import android.content.Intent;

import android.util.Log;


import com.hannan.kevin.login.SessionManager;
import com.hannan.kevin.model.ItemsList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by khannan on 7/2/16.
 */
public class PodcastFetchService extends IntentService{

    private static final String TAG = "PodcastFetchService";

    SessionManager manager;

    public PodcastFetchService() {
        super("PodcastFetchService");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        manager = new SessionManager(getApplicationContext());
        getApiData();
    }

    private void getApiData() {

        String token = manager.getToken();

        LoginService client = ServiceGenerator.createService(LoginService.class);

        Call<ItemsList> call = client.getRecommendations(token);

        call.enqueue(new Callback<ItemsList>() {

            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                Log.v(TAG, "onResponse()");

                if (response.isSuccessful()) {
                    Log.v(TAG, "success!");
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {
                Log.v(TAG, "onFailure ");
            }
        });

    }
}
