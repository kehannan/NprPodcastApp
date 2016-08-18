package com.hannan.kevin.api;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.hannan.kevin.login.SessionManager;
import com.hannan.kevin.model.ItemsList;
import com.hannan.kevin.model.PodcastItem;
import com.hannan.kevin.provider.DatabaseContract;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by khannan on 7/2/16.
 */
public class PodcastFetchService extends IntentService{

    private static final String TAG = "PodcastFetchService";
    private static final String CHANNEL_SHOWS = "shows";

    SessionManager manager;

    public PodcastFetchService() {
        super("PodcastFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent");
        manager = new SessionManager(getApplicationContext());
        getApiData();
    }

    private void getApiData() {

        String token = manager.getToken();
        //prepend "Bearer "
        token = SessionManager.BEARER_TAG + token;

        LoginService client = ServiceGenerator.createService(LoginService.class);

        Call<ItemsList> call = client.getRecommendations(token, CHANNEL_SHOWS);

        call.enqueue(new Callback<ItemsList>() {

            @Override
            public void onResponse(Call<ItemsList> call, Response<ItemsList> response) {

                Log.v(TAG, "onResponse()");

                if (response.isSuccessful()) {
                    Log.v(TAG, "success!");

                    //iterate through objects
                    ArrayList<PodcastItem> podcastItems = response.body().getItemsList();
                    Log.v(TAG, "podcastItems size " + podcastItems.size());

                    //ContentValues to be inserted
                   ContentValues[] values = new ContentValues[podcastItems.size()];
                    Log.v(TAG, "size of values " + values.length);
                    Log.v(TAG, "size of podcast items " + podcastItems.size());

                    for(int i = 0; i< podcastItems.size(); i++) {
                        Log.v(TAG, " i " + i);

                        values[i] = createPodcast(podcastItems.get(i));
                    }

                    //insert data in db with bulk insert
                    Log.v(TAG, DatabaseContract.PodcastTable.allPodcasts().toString());
                    int inserted_data = getApplicationContext().getContentResolver()
                            .bulkInsert(DatabaseContract.PodcastTable.allPodcasts(), values);
                }
            }

            @Override
            public void onFailure(Call<ItemsList> call, Throwable t) {
                Log.v(TAG, "onFailure ");
            }
        });
    }

    private ContentValues createPodcast(PodcastItem podcastItem) {

        ContentValues podcast_values = new ContentValues();

        //insert podcast title
        podcast_values.put(DatabaseContract.PodcastTable.TITLE, podcastItem.getAttributes().getTitle());

        //insert audio href
        podcast_values.put(DatabaseContract.PodcastTable.AUDIO_HREF, podcastItem.getLinks().getAudio().
                get(0).getHref());

        //insert image hrefs
        if (podcastItem.getLinks().getImage().size() != 0) {

            podcast_values.put(DatabaseContract.PodcastTable.IMAGE_HREF,
                    podcastItem.getLinks().getImage().get(0).getHref());

//            ArrayList<Image> imageList = (ArrayList<Image>) podcastItem.getLinks().getImage();
//
//            for (Image image : imageList) {
//
//                String rel = image.getRel();
//
//                if (rel.equals("square")) {
//                    podcast_values.put(DatabaseContract.PodcastTable.IMAGE_HREF, image.getHref());
//                }
//
//                if (rel.equals("brick")) {
//                    podcast_values.put(DatabaseContract.PodcastTable.BRICK_HREF, image.getHref());
//                }
//            }
        } else {
            podcast_values.putNull(DatabaseContract.PodcastTable.IMAGE_HREF);
            Log.v(TAG, "image href empty");
        }

        //insert program
        podcast_values.put(DatabaseContract.PodcastTable.PROGRAM,
                podcastItem.getAttributes().getProgram());

        //insert description
        podcast_values.put(DatabaseContract.PodcastTable.DESCRIPTION,
                podcastItem.getAttributes().getDescription());

        //insert duration
        podcast_values.put(DatabaseContract.PodcastTable.DURATION,
                podcastItem.getAttributes().getDuration());

        //insert date
        podcast_values.put(DatabaseContract.PodcastTable.PROG_DATE,
                podcastItem.getAttributes().getDate());

        Log.v(TAG, "prog " +  podcastItem.getAttributes().getProgram());

        return podcast_values;
    }
}
